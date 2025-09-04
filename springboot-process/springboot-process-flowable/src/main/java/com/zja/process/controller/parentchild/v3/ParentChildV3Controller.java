package com.zja.process.controller.parentchild.v3;

import com.zja.process.service.parentchild.v2.ParentChildSyncServiceV2;
import com.zja.process.service.parentchild.v3.ParentChildSyncServiceV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 父子流程控制器 V2 (通过业务自主实现，不使用任何监听器实现)
 * 提供父子流程的启动、任务完成、状态查询、日志查看等功能
 *
 * @Author: zhengja
 * @Date: 2025-09-04 18:36
 */
@Slf4j
@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class ParentChildV3Controller {

    private final RuntimeService runtimeService;
    private final ParentChildSyncServiceV3 syncServiceV3;

    private final TaskService taskService;
    private final HistoryService historyService;
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;

    private static final String VAR_PARENT_BIZ_KEY = "PARENT_BIZ_KEY";

    private static final String PARENT_PROCESS_KEY = "parentProcessV3";
    private static final String CHILD_PROCESS_KEY = "childProcessV3";

    /**
     * 启动父流程和若干子流程
     */
    @PostMapping("/start")
    public Map<String, Object> start(@RequestParam String bizKey,
                                     @RequestParam(defaultValue = "2") int childCount) {
        // 启动父流程
        ProcessInstance parent = runtimeService.startProcessInstanceByKey(
                PARENT_PROCESS_KEY, bizKey,
                new HashMap<String, Object>() {{
                    put("bizKey", bizKey);
                }}
        );
        log.info("[CTRL] 启动父流程: bizKey={}, procId={}", bizKey, parent.getProcessInstanceId());

        // 启动子流程
        for (int i = 0; i < childCount; i++) {
            ProcessInstance child = runtimeService.startProcessInstanceByKey(
                    CHILD_PROCESS_KEY,
                    new HashMap<String, Object>() {{
                        put(VAR_PARENT_BIZ_KEY, bizKey);
                    }}
            );
            log.info("[CTRL] 启动子流程: parentBizKey={}, childProcId={}", bizKey, child.getProcessInstanceId());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("bizKey", bizKey);
        result.put("parentProcId", parent.getId());
        result.put("childCount", childCount);
        return result;
    }

    /**
     * 推进父子流程（审批通过）
     */
    @PostMapping("/complete")
    public Map<String, Object> complete(@RequestParam String parentBizKey,
                                        @RequestParam(defaultValue = "APPROVE") String decision) {
        syncServiceV3.completeParentAndChildren(parentBizKey, decision);

        Map<String, Object> result = new HashMap<>();
        result.put("bizKey", parentBizKey);
        result.put("action", "COMPLETE");
        result.put("decision", decision);
        return result;
    }

    /**
     * 退回父子流程
     */
    @PostMapping("/rollback")
    public Map<String, Object> rollback(@RequestParam String parentBizKey,
                                        @RequestParam(required = false) String decision) {
        log.info("[CTRL] 开始处理退回操作, parentBizKey={}, decision={}", parentBizKey, decision);

        try {
            // 根据当前父流程任务自动确定退回目标
            Task currentParentTask = taskService.createTaskQuery()
                    .processInstanceBusinessKey(parentBizKey)
                    .singleResult();

            if (currentParentTask == null) {
                throw new IllegalStateException("未找到父流程当前任务: " + parentBizKey);
            }

            String currentTaskKey = currentParentTask.getTaskDefinitionKey();
            log.debug("[CTRL] 当前父流程任务: taskKey={}", currentTaskKey);

            // 映射父流程退回目标
            String parentTargetTaskKey = mapParentRollbackTarget(currentTaskKey);
            if (parentTargetTaskKey == null) {
                throw new IllegalStateException("当前环节不支持退回: " + currentTaskKey);
            }

            // 映射子流程退回目标
            String childTargetTaskKey = mapChildRollbackTarget(currentTaskKey);
            if (childTargetTaskKey == null) {
                throw new IllegalStateException("未配置子流程退回目标: " + currentTaskKey);
            }

            String action = "退回";
            String userId = "admin";
            String opinion = "退回";

            // 添加评论日志（在任务完成前）
            taskService.addComment(currentParentTask.getId(), currentParentTask.getProcessInstanceId(),
                    String.format("【%s】操作: %s, 意见: %s", userId, action, opinion));
            log.debug("[CTRL] 添加退回评论, taskId={}", currentParentTask.getId());

            // 执行退回操作
            syncServiceV3.rollbackParentAndChildren(parentBizKey, parentTargetTaskKey, childTargetTaskKey);

            Map<String, Object> result = new HashMap<>();
            result.put("bizKey", parentBizKey);
            result.put("action", "ROLLBACK");
            result.put("parentFrom", currentTaskKey);
            result.put("parentTo", parentTargetTaskKey);
            result.put("childTo", childTargetTaskKey);
            result.put("message", "退回操作执行成功");

            log.info("[CTRL] 退回操作执行成功, parentBizKey={}, parentFrom={}, parentTo={}, childTo={}",
                    parentBizKey, currentTaskKey, parentTargetTaskKey, childTargetTaskKey);

            return result;
        } catch (Exception e) {
            log.error("[CTRL] 退回操作执行失败, parentBizKey={}", parentBizKey, e);
            throw new RuntimeException("退回操作执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 映射父流程退回目标环节
     *
     * @param currentTaskKey 当前任务Key
     * @return 退回目标任务Key
     */
    private String mapParentRollbackTarget(String currentTaskKey) {
        switch (currentTaskKey) {
            case "approveTask":
                return "applyTask";   // 审批环节退回至申请环节
            case "archiveTask":
                return "approveTask"; // 归档环节退回至审批环节
            default:
                log.warn("[CTRL] 当前环节不支持退回操作: taskKey={}", currentTaskKey);
                return null;
        }
    }

    /**
     * 映射子流程退回目标环节
     *
     * @param parentTaskKey 父流程任务Key
     * @return 子流程目标任务Key
     */
    private String mapChildRollbackTarget(String parentTaskKey) {
        switch (parentTaskKey) {
            case "approveTask":
                return "childApplyTask";   // 父流程审批退回时，子流程退回至申请环节
            case "archiveTask":
                return "childApproveTask"; // 父流程归档退回时，子流程退回至审批环节
            default:
                log.warn("[CTRL] 未配置子流程退回映射: parentTaskKey={}", parentTaskKey);
                return null;
        }
    }

    /**
     * 退回父子流程
     */
    @PostMapping("/rollback/v2")
    public Map<String, Object> rollback(@RequestParam String parentBizKey,
                                        @RequestParam String targetTaskKey,
                                        @RequestParam String childTargetKey) {
        syncServiceV3.rollbackParentAndChildren(parentBizKey, targetTaskKey, childTargetKey);

        Map<String, Object> result = new HashMap<>();
        result.put("bizKey", parentBizKey);
        result.put("action", "ROLLBACK");
        result.put("parentTo", targetTaskKey);
        result.put("childTo", childTargetKey);
        return result;
    }

    /**
     * 查询父子流程当前任务
     */
    @GetMapping("/tasks")
    public Map<String, Object> tasks(@RequestParam String parentBizKey) {
        List<Task> parentTasks = taskService.createTaskQuery()
                .processInstanceBusinessKey(parentBizKey).list();

        List<Task> childTasks = taskService.createTaskQuery()
                .processVariableValueEquals(VAR_PARENT_BIZ_KEY, parentBizKey).list();

        Map<String, Object> result = new HashMap<>();
        result.put("parentTasks", parentTasks.stream().map(this::toMap).collect(Collectors.toList()));
        result.put("childTasks", childTasks.stream().map(this::toMap).collect(Collectors.toList()));
        return result;
    }

    /**
     * 查询父流程和子流程运行状态
     */
    @GetMapping("/processes")
    public Map<String, Object> processes(@RequestParam String parentBizKey) {
        ProcessInstance parent = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(parentBizKey).singleResult();

        List<ProcessInstance> children = runtimeService.createProcessInstanceQuery()
                .variableValueEquals(VAR_PARENT_BIZ_KEY, parentBizKey).list();

        Map<String, Object> result = new HashMap<>();
        result.put("parent", parent != null ? parent.getProcessInstanceId() : null);
        result.put("children", children.stream().map(ProcessInstance::getProcessInstanceId).collect(Collectors.toList()));
        return result;
    }

    private Map<String, Object> toMap(Task task) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("name", task.getName());
        taskMap.put("assignee", task.getAssignee());
        taskMap.put("processInstanceId", task.getProcessInstanceId());
        taskMap.put("createTime", task.getCreateTime());
        return taskMap;
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
