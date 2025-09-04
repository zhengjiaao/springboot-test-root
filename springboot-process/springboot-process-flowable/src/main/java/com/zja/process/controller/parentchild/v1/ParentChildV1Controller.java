package com.zja.process.controller.parentchild.v1;

import com.zja.process.service.parentchild.v1.ParentChildSyncServiceV1;
import com.zja.process.service.parentchild.v2.ParentChildSyncServiceV2;
import com.zja.process.util.FlowableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 父子流程控制器 V1 (通过任务监听器实现)
 * 提供父子流程的启动、任务完成、状态查询、日志查看等功能
 *
 * @Author: zhengja
 * @Date: 2025-09-02 16:32
 */
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
public class ParentChildV1Controller {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ParentChildSyncServiceV1 syncService;

    private final HistoryService historyService;
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;

    private static final String PARENT_PROCESS_KEY = "parentProcessV1";
    private static final String CHILD_PROCESS_KEY = "childProcessV1";

    /**
     * 启动父流程，并以该父业务主键启动 N 个子流程
     *
     * @param parentBizKey 父流程业务主键
     * @param childCount   子流程数量，默认为2
     * @return 启动结果
     */
    @PostMapping("/start")
    public Map<String, Object> start(@RequestParam String parentBizKey,
                                     @RequestParam(defaultValue = "2") int childCount) {
        log.info("[PROCESS] 开始启动父流程和子流程, parentBizKey={}, childCount={}", parentBizKey, childCount);

        try {
            // 启动父流程
            runtimeService.startProcessInstanceByKey(PARENT_PROCESS_KEY, parentBizKey, new HashMap<>());
            log.debug("[PROCESS] 父流程启动成功, parentBizKey={}", parentBizKey);

            // 启动子流程
            List<String> childIds = syncService.startChildren(parentBizKey, CHILD_PROCESS_KEY, childCount);
            log.info("[PROCESS] 子流程启动完成, parentBizKey={}, childCount={}", parentBizKey, childIds.size());

            Map<String, Object> resp = new HashMap<>();
            resp.put("parentBizKey", parentBizKey);
            resp.put("children", childIds);
            return resp;
        } catch (Exception e) {
            log.error("[PROCESS] 启动父子流程失败, parentBizKey={}, childCount={}", parentBizKey, childCount, e);
            throw new RuntimeException("启动流程失败: " + e.getMessage(), e);
        }
    }

    /**
     * 完成父流程当前任务（如：申请/审批/归档），审批环节需要传 decision=APPROVE|REJECT
     *
     * @param bizKey   业务主键,例如：项目id
     * @param decision 操作决策 (APPROVE|REJECT)
     * @param userId   操作人
     * @param comment  操作意见
     * @return 操作结果
     */
    @PostMapping("/parent/{bizKey}/complete")
    public Map<String, Object> completeParentTask(@PathVariable String bizKey,
                                                  @RequestParam(required = false) String decision,
                                                  @RequestParam(required = false) String userId,
                                                  @RequestParam(required = false) String comment) {
        log.info("[TASK] 开始处理父流程任务完成操作, bizKey={}, decision={}, userId={}", bizKey, decision, userId);

        Task task = FlowableUtils.oneTaskByBizKey(taskService, bizKey);
        if (task == null) {
            log.warn("[TASK] 未找到父流程激活任务, bizKey={}", bizKey);
            throw new IllegalStateException("未找到父流程激活任务，bizKey=" + bizKey);
        }

        // 设置操作人
        if (userId != null) {
            taskService.setAssignee(task.getId(), userId);
            log.debug("[TASK] 设置任务处理人, taskId={}, userId={}", task.getId(), userId);
        }

        String opinion = comment != null ? comment : "";
        Map<String, Object> resp = new HashMap<>();
        resp.put("bizKey", bizKey);

        try {
            // === 退回处理 ===
            if ("REJECT".equalsIgnoreCase(decision)) {
                return handleRejectTask(task, bizKey, userId, opinion, resp);
            }

            // === 正常完成任务处理 ===
            return handleApproveTask(task, userId, decision, opinion, resp);
        } catch (Exception e) {
            log.error("[TASK] 处理父流程任务失败, bizKey={}, taskId={}", bizKey, task.getId(), e);
            throw new RuntimeException("处理任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 处理退回任务操作
     */
    private Map<String, Object> handleRejectTask(Task task, String bizKey, String userId,
                                                 String opinion, Map<String, Object> resp) {
        log.info("[CTRL] 父流程退回操作, bizKey={}, taskId={}, name={}", bizKey, task.getId(), task.getName());

        // 找出父流程要退回的目标节点
        String rollbackTarget = mapParentRollbackTarget(task.getTaskDefinitionKey());
        if (rollbackTarget == null) {
            log.warn("[CTRL] 当前环节不支持退回, taskName={}", task.getName());
            throw new IllegalStateException("当前环节不支持退回: " + task.getName());
        }

        String action = "退回";

        // 添加评论日志（在任务完成前）
        taskService.addComment(task.getId(), task.getProcessInstanceId(),
                String.format("【%s】操作: %s, 意见: %s", userId, action, opinion));
        log.debug("[CTRL] 添加退回评论, taskId={}", task.getId());

        // 父流程退回
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(task.getTaskDefinitionKey(), rollbackTarget)
                .changeState();
        log.debug("[CTRL] 父流程状态变更完成, from={}, to={}", task.getTaskDefinitionKey(), rollbackTarget);

        // 子流程退回
        String childTarget = mapChildRollbackTarget(task.getTaskDefinitionKey());
        if (childTarget != null) {
            syncService.rollbackChildrenTo(bizKey, childTarget);
            log.debug("[CTRL] 子流程退回操作完成, childTarget={}", childTarget);
        }

        resp.put("from", task.getTaskDefinitionKey());
        resp.put("to", rollbackTarget);
        resp.put("action", action);
        resp.put("comment", opinion);
        log.info("[CTRL] 退回操作处理完成, bizKey={}, from={}, to={}", bizKey, task.getTaskDefinitionKey(), rollbackTarget);
        return resp;
    }

    /**
     * 处理审批通过任务操作
     */
    private Map<String, Object> handleApproveTask(Task task, String userId, String decision,
                                                  String opinion, Map<String, Object> resp) {
        log.info("[CTRL] 父流程审批通过操作, taskId={}, name={}", task.getId(), task.getName());

        String action = "审批通过";

        Map<String, Object> vars = new HashMap<>();
        if (decision != null) {
            vars.put("decision", decision);
        }

        // 添加评论日志（在任务完成前）
        taskService.addComment(task.getId(), task.getProcessInstanceId(),
                String.format("【%s】操作: %s, 意见: %s", userId, action, opinion));
        log.debug("[CTRL] 添加审批评论, taskId={}", task.getId());

        // 完成父流程任务
        taskService.complete(task.getId(), vars);
        log.debug("[CTRL] 父流程任务完成, taskId={}", task.getId());

        resp.put("completedTask", task.getName());
        resp.put("action", action);
        resp.put("comment", opinion);
        log.info("[CTRL] 审批通过操作处理完成, taskId={}, taskName={}", task.getId(), task.getName());
        return resp;
    }

    // 父流程退回目标映射
    private String mapParentRollbackTarget(String currentTaskId) {
        switch (currentTaskId) {
            case "approveTask":
                return "applyTask";   // 父审批退回 → 回申请
            case "archiveTask":
                return "approveTask"; // 父归档退回 → 回审批
            default:
                return null;
        }
    }

    // 子流程退回目标映射
    private String mapChildRollbackTarget(String parentTaskId) {
        switch (parentTaskId) {
            case "approveTask":
                return "childApplyTask";   // 子退回申请
            case "archiveTask":
                return "childApproveTask"; // 子退回审批
            default:
                return null;
        }
    }

    /**
     * 查看某业务主键下当前激活的父/子任务（便于验证）
     *
     * @param parentBizKey 父流程业务主键
     * @return 当前激活的任务列表
     */
    @GetMapping("/active")
    public Map<String, Object> active(@RequestParam String parentBizKey) {
        log.debug("[QUERY] 查询激活任务, parentBizKey={}", parentBizKey);

        Map<String, Object> map = new HashMap<>();

        // 查询父流程任务
        map.put("parentTasks",
                taskService.createTaskQuery()
                        .processInstanceBusinessKey(parentBizKey)
                        .list()
                        .stream()
                        .map(task -> {
                            Map<String, Object> taskInfo = new HashMap<>();
                            taskInfo.put("id", task.getId());
                            taskInfo.put("name", task.getName());
                            taskInfo.put("processInstanceId", task.getProcessInstanceId());
                            taskInfo.put("assignee", task.getAssignee());
                            taskInfo.put("createTime", task.getCreateTime());
                            return taskInfo;
                        })
                        .collect(Collectors.toList()));

        // 查询子流程任务
        map.put("childTasks",
                taskService.createTaskQuery()
                        .processVariableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                        .list()
                        .stream()
                        .map(task -> {
                            Map<String, Object> taskInfo = new HashMap<>();
                            taskInfo.put("id", task.getId());
                            taskInfo.put("name", task.getName());
                            taskInfo.put("processInstanceId", task.getProcessInstanceId());
                            taskInfo.put("assignee", task.getAssignee());
                            taskInfo.put("createTime", task.getCreateTime());
                            return taskInfo;
                        })
                        .collect(Collectors.toList()));

        log.debug("[QUERY] 激活任务查询完成, parentBizKey={}, parentCount={}, childCount={}",
                parentBizKey,
                ((List<?>) map.get("parentTasks")).size(),
                ((List<?>) map.get("childTasks")).size());

        return map;
    }

    /**
     * 查看某业务主键下所有任务日志
     *
     * @param parentBizKey 父流程业务主键
     * @return 任务日志列表
     */
    @GetMapping("/logs")
    public Map<String, Object> getLogs(@RequestParam String parentBizKey) {
        log.debug("[LOG] 查询任务日志, parentBizKey={}", parentBizKey);

        Map<String, Object> result = new HashMap<>();

        // 父流程日志
        List<Map<String, Object>> parentLogs = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(parentBizKey)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list()
                .stream()
                .map(t -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("processType", "PARENT");
                    m.put("taskName", t.getName());
                    m.put("assignee", t.getAssignee());
                    m.put("createTime", t.getCreateTime());
                    m.put("endTime", t.getEndTime());
                    m.put("status", t.getEndTime() == null ? "未完成" : "已完成");
                    m.put("duration", t.getDurationInMillis());
                    m.put("processInstanceId", t.getProcessInstanceId());
                    m.put("processDefinitionId", t.getProcessDefinitionId());
                    return m;
                }).collect(Collectors.toList());

        // 子流程日志
        List<Map<String, Object>> childLogs = historyService.createHistoricTaskInstanceQuery()
                .processVariableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list()
                .stream()
                .map(t -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("processType", "CHILD");
                    m.put("taskName", t.getName());
                    m.put("assignee", t.getAssignee());
                    m.put("createTime", t.getCreateTime());
                    m.put("endTime", t.getEndTime());
                    m.put("status", t.getEndTime() == null ? "未完成" : "已完成");
                    m.put("duration", t.getDurationInMillis());
                    m.put("processInstanceId", t.getProcessInstanceId());
                    m.put("processDefinitionId", t.getProcessDefinitionId());
                    return m;
                }).collect(Collectors.toList());

        result.put("parentLogs", parentLogs);
        result.put("childLogs", childLogs);

        log.debug("[LOG] 任务日志查询完成, parentBizKey={}, parentLogCount={}, childLogCount={}",
                parentBizKey, parentLogs.size(), childLogs.size());

        return result;
    }

    /**
     * 办理过程日志（基于评论）
     *
     * @param parentBizKey 父流程业务主键
     * @return 操作日志列表
     */
    @GetMapping("/logs/v2")
    public List<Map<String, Object>> getLogsV2(@RequestParam String parentBizKey) {
        log.debug("[LOG] 查询操作日志, parentBizKey={}", parentBizKey);

        List<Map<String, Object>> logs = new ArrayList<>();

        // 父流程任务历史
        List<HistoricTaskInstance> parentTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(parentBizKey)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();

        for (HistoricTaskInstance task : parentTasks) {
            List<Comment> comments = taskService.getTaskComments(task.getId());
            for (Comment c : comments) {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("processType", "PARENT");
                entry.put("taskName", task.getName());
                entry.put("assignee", task.getAssignee());
                entry.put("action", parseActionFromComment(c.getFullMessage()));
                entry.put("opinion", parseOpinionFromComment(c.getFullMessage()));
                entry.put("time", c.getTime());
                logs.add(entry);
            }
        }

        // 子流程任务历史
        List<HistoricTaskInstance> childTasks = historyService.createHistoricTaskInstanceQuery()
                .processVariableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();

        for (HistoricTaskInstance task : childTasks) {
            List<Comment> comments = taskService.getTaskComments(task.getId());
            for (Comment c : comments) {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("processType", "CHILD");
                entry.put("taskName", task.getName());
                entry.put("assignee", task.getAssignee());
                entry.put("action", parseActionFromComment(c.getFullMessage()));
                entry.put("opinion", parseOpinionFromComment(c.getFullMessage()));
                entry.put("time", c.getTime());
                logs.add(entry);
            }
        }

        log.debug("[LOG] 操作日志查询完成, parentBizKey={}, logCount={}", parentBizKey, logs.size());

        return logs;
    }

    /**
     * 从评论中解析操作类型
     */
    private String parseActionFromComment(String msg) {
        if (msg == null) return "提交";
        if (msg.contains("退回")) return "退回";
        if (msg.contains("审批通过")) return "审批通过";
        return "提交";
    }

    /**
     * 从评论中解析操作意见
     */
    private String parseOpinionFromComment(String msg) {
        if (msg == null) return "";
        int idx = msg.indexOf("意见:");
        if (idx >= 0) {
            return msg.substring(idx + 3).trim();
        }
        return "";
    }

    /**
     * 获取流程图
     *
     * @param parentBizKey 父流程业务主键
     * @return 流程图数据
     * @throws Exception 生成流程图异常
     */
    @GetMapping("/diagram")
    public Map<String, Object> getDiagram(@RequestParam String parentBizKey) throws Exception {
        log.debug("[DIAGRAM] 生成流程图, parentBizKey={}", parentBizKey);

        Map<String, Object> result = new HashMap<>();

        // 父流程实例
        HistoricProcessInstance parent = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(parentBizKey)
                .singleResult();

        if (parent != null) {
            byte[] parentDiagram = generateProcessDiagram(parent.getId());
            result.put("parentDiagram", Base64.getEncoder().encodeToString(parentDiagram));
            log.debug("[DIAGRAM] 父流程图生成完成, processInstanceId={}", parent.getId());
        }

        // 子流程实例
        List<HistoricProcessInstance> children = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                .list();

        List<String> childImgs = new ArrayList<>();
        for (HistoricProcessInstance child : children) {
            byte[] childDiagram = generateProcessDiagram(child.getId());
            childImgs.add(Base64.getEncoder().encodeToString(childDiagram));
        }
        result.put("childDiagrams", childImgs);
        log.debug("[DIAGRAM] 子流程图生成完成, parentBizKey={}, childCount={}", parentBizKey, childImgs.size());

        return result;
    }

    /**
     * 生成流程图
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图字节数组
     * @throws IOException IO异常
     */
    private byte[] generateProcessDiagram(String processInstanceId) throws IOException {
        log.debug("[DIAGRAM] 开始生成流程图, processInstanceId={}", processInstanceId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(
                historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult()
                        .getProcessDefinitionId());

        List<String> highLightedActivities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list()
                .stream()
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toList());

        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

        // 设置字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");

        try (InputStream is = diagramGenerator.generateDiagram(
                bpmnModel, "png", highLightedActivities, new ArrayList<>(),
                processEngineConfiguration.getActivityFontName(),
                processEngineConfiguration.getLabelFontName(),
                processEngineConfiguration.getAnnotationFontName(),
                processEngineConfiguration.getClassLoader(), 1.0, true);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            log.debug("[DIAGRAM] 流程图生成成功, processInstanceId={}, size={} bytes", processInstanceId, baos.size());
            return baos.toByteArray();
        }
    }
}
