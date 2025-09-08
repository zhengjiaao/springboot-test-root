package com.zja.process.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用流程控制器
 * 提供流程部署、启动、任务处理、查询等完整功能
 *
 * @Author: zhengja
 * @Date: 2025-09-04 20:44
 */
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
@Slf4j
public class ProcessController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final RepositoryService repositoryService;
    private final ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 部署流程定义（通过BPMN文件）
     */
    @PostMapping("/deploy")
    public Map<String, Object> deploy(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("[CTRL] 开始部署流程定义: fileName={}", file.getOriginalFilename());

        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(file.getOriginalFilename(), file.getInputStream())
                .deploy();

        log.info("[CTRL] 流程定义部署成功: deploymentId={}", deployment.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("deploymentId", deployment.getId());
        result.put("deploymentName", deployment.getName());
        result.put("deploymentTime", deployment.getDeploymentTime());
        result.put("deploymentKey", deployment.getKey());
        result.put("deploymentCategory", deployment.getCategory());
        result.put("deploymentTenantId", deployment.getTenantId());
        result.put("deploymentResources", deployment.getResources());
        return result;
    }

    /**
     * 查询流程定义列表
     */
    @GetMapping("/definitions")
    public List<Map<String, Object>> listProcessDefinitions() {
        log.debug("[CTRL] 查询流程定义列表");

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();

        return processDefinitions.stream()
                .map(pd -> {
                    Map<String, Object> definitionMap = new HashMap<>();
                    definitionMap.put("id", pd.getId());
                    definitionMap.put("key", pd.getKey());
                    definitionMap.put("name", pd.getName());
                    definitionMap.put("version", pd.getVersion());
                    definitionMap.put("deploymentId", pd.getDeploymentId());
                    definitionMap.put("description", pd.getDescription());
                    definitionMap.put("suspended", pd.isSuspended());
                    definitionMap.put("tenantId", pd.getTenantId());
                    definitionMap.put("category", pd.getCategory());
                    definitionMap.put("diagramResourceName", pd.getDiagramResourceName());
                    definitionMap.put("hasStartFormKey", pd.hasStartFormKey());
                    return definitionMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * 启动流程
     */
    @PostMapping("/start")
    public Map<String, Object> start(@RequestParam String procDefKey,
                                     @RequestParam String bizKey,
                                     @RequestParam(required = false) Map<String, Object> vars) {
        log.info("[CTRL] 启动流程: procDefKey={}, bizKey={}", procDefKey, bizKey);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(procDefKey, bizKey, vars);
        log.info("[CTRL] 流程启动成功: procDefKey={}, bizKey={}, procId={}", procDefKey, bizKey, pi.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("procInstId", pi.getId());
        result.put("bizKey", bizKey);
        result.put("processDefinitionId", pi.getProcessDefinitionId());
        result.put("startTime", pi.getStartTime());
        result.put("message", "启动流程成功");
        return result;
    }

    /**
     * 根据业务主键启动流程
     */
    @PostMapping("/start-by-bizkey")
    public Map<String, Object> startByBizKey(@RequestParam String procDefKey,
                                            @RequestParam String bizKey,
                                            @RequestParam(required = false) Map<String, Object> vars) {
        log.info("[CTRL] 根据业务主键启动流程: procDefKey={}, bizKey={}", procDefKey, bizKey);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(procDefKey, bizKey, vars);
        log.info("[CTRL] 流程启动成功: procDefKey={}, bizKey={}, procId={}", procDefKey, bizKey, pi.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("procInstId", pi.getId());
        result.put("bizKey", bizKey);
        result.put("processDefinitionId", pi.getProcessDefinitionId());
        result.put("startTime", pi.getStartTime());
        result.put("message", "启动流程成功");
        return result;
    }

    /**
     * 完成任务（推进）
     */
    @PostMapping("/task/complete")
    public Map<String, Object> complete(@RequestParam String taskId,
                                        @RequestParam(required = false) String userId,
                                        @RequestParam(required = false) String comment,
                                        @RequestParam(required = false) String decision) {
        log.info("[CTRL] 开始完成任务: taskId={}, userId={}, decision={}", taskId, userId, decision);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) throw new IllegalStateException("任务不存在: " + taskId);

        if (userId != null) taskService.setAssignee(taskId, userId);

        Map<String, Object> vars = new HashMap<>();
        if (decision != null) vars.put("decision", decision);

        if (comment != null) {
            taskService.addComment(taskId, task.getProcessInstanceId(),
                    String.format("【%s】操作: %s, 意见: %s",
                            userId != null ? userId : "系统",
                            decision != null ? (decision.contains("REJECT") ? "退回" : "审批通过") : "提交",
                            comment));
        }

        taskService.complete(taskId, vars);
        log.info("[CTRL] 任务完成: taskId={}, name={}, assignee={}, decision={}", taskId, task.getName(), userId, decision);

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("action", "COMPLETE");
        result.put("decision", decision);
        result.put("message", "任务完成成功");
        return result;
    }

    /**
     * 退回任务（迁移到指定环节）
     */
    @PostMapping("/task/rollback")
    public Map<String, Object> rollback(@RequestParam String procInstId,
                                        @RequestParam String currentTaskDefKey,
                                        @RequestParam String targetTaskDefKey) {
        log.info("[CTRL] 开始退回流程: procInstId={}, from={}, to={}", procInstId, currentTaskDefKey, targetTaskDefKey);

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(procInstId)
                .moveActivityIdTo(currentTaskDefKey, targetTaskDefKey)
                .changeState();
        log.info("[CTRL] 流程退回成功: procInstId={}, from={}, to={}", procInstId, currentTaskDefKey, targetTaskDefKey);

        Map<String, Object> result = new HashMap<>();
        result.put("procInstId", procInstId);
        result.put("action", "ROLLBACK");
        result.put("from", currentTaskDefKey);
        result.put("to", targetTaskDefKey);
        result.put("message", "流程退回成功");
        return result;
    }

    /**
     * 查询流程实例
     */
    @GetMapping("/instance")
    public Map<String, Object> instance(@RequestParam String procInstId) {
        log.debug("[CTRL] 查询流程实例: procInstId={}", procInstId);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(procInstId).singleResult();

        Map<String, Object> result = new HashMap<>();
        result.put("procInstId", procInstId);

        if (pi == null) {
            // 查询历史记录
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(procInstId)
                    .singleResult();
            if (hpi != null) {
                result.put("status", "已结束");
                result.put("startTime", hpi.getStartTime());
                result.put("endTime", hpi.getEndTime());
                result.put("durationInMillis", hpi.getDurationInMillis());
            } else {
                result.put("status", "不存在");
            }
        } else {
            result.put("definitionId", pi.getProcessDefinitionId());
            result.put("businessKey", pi.getBusinessKey());
            result.put("status", "运行中");
            result.put("startTime", pi.getStartTime());
            result.put("startUserId", pi.getStartUserId());
        }
        return result;
    }

    /**
     * 根据业务主键查询流程实例
     */
    @GetMapping("/instance-by-bizkey")
    public List<Map<String, Object>> instanceByBizKey(@RequestParam String bizKey) {
        log.debug("[CTRL] 根据业务主键查询流程实例: bizKey={}", bizKey);

        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(bizKey)
                .list();

        List<HistoricProcessInstance> historicInstances = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(bizKey)
                .list();

        List<Map<String, Object>> result = new ArrayList<>();

        // 添加运行中的实例
        instances.forEach(pi -> {
            Map<String, Object> instanceMap = new HashMap<>();
            instanceMap.put("procInstId", pi.getId());
            instanceMap.put("definitionId", pi.getProcessDefinitionId());
            instanceMap.put("businessKey", pi.getBusinessKey());
            instanceMap.put("status", "运行中");
            instanceMap.put("startTime", pi.getStartTime());
            result.add(instanceMap);
        });

        // 添加历史实例（去重）
        historicInstances.forEach(hpi -> {
            if (instances.stream().noneMatch(pi -> pi.getId().equals(hpi.getId()))) {
                Map<String, Object> instanceMap = new HashMap<>();
                instanceMap.put("procInstId", hpi.getId());
                instanceMap.put("definitionId", hpi.getProcessDefinitionId());
                instanceMap.put("businessKey", hpi.getBusinessKey());
                instanceMap.put("status", "已结束");
                instanceMap.put("startTime", hpi.getStartTime());
                instanceMap.put("endTime", hpi.getEndTime());
                result.add(instanceMap);
            }
        });

        return result;
    }

    /**
     * 查询当前任务（通过流程实例ID）
     */
    @GetMapping("/tasks")
    public List<Map<String, Object>> tasks(@RequestParam String procInstId) {
        log.debug("[CTRL] 查询流程实例当前任务: procInstId={}", procInstId);

        return taskService.createTaskQuery()
                .processInstanceId(procInstId)
                .list()
                .stream()
                .map(t -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("id", t.getId());
                    taskMap.put("name", t.getName());
                    taskMap.put("assignee", t.getAssignee());
                    taskMap.put("createTime", t.getCreateTime());
                    taskMap.put("taskDefinitionKey", t.getTaskDefinitionKey());
                    taskMap.put("processInstanceId", t.getProcessInstanceId());
                    return taskMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询用户任务（通过用户ID）
     */
    @GetMapping("/user-tasks")
    public List<Map<String, Object>> userTasks(@RequestParam String userId) {
        log.debug("[CTRL] 查询用户任务: userId={}", userId);

        return taskService.createTaskQuery()
                .taskAssignee(userId)
                .list()
                .stream()
                .map(t -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("id", t.getId());
                    taskMap.put("name", t.getName());
                    taskMap.put("assignee", t.getAssignee());
                    taskMap.put("createTime", t.getCreateTime());
                    taskMap.put("taskDefinitionKey", t.getTaskDefinitionKey());
                    taskMap.put("processInstanceId", t.getProcessInstanceId());
                    taskMap.put("processDefinitionId", t.getProcessDefinitionId());
                    taskMap.put("businessKey", getProcessInstanceBusinessKey(t.getProcessInstanceId()));
                    return taskMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询当前任务（通过业务主键）
     */
    @GetMapping("/tasks-by-bizkey")
    public List<Map<String, Object>> tasksByBizKey(@RequestParam String bizKey) {
        log.debug("[CTRL] 根据业务主键查询当前任务: bizKey={}", bizKey);

        return taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .list()
                .stream()
                .map(t -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("id", t.getId());
                    taskMap.put("name", t.getName());
                    taskMap.put("assignee", t.getAssignee());
                    taskMap.put("createTime", t.getCreateTime());
                    taskMap.put("taskDefinitionKey", t.getTaskDefinitionKey());
                    taskMap.put("processInstanceId", t.getProcessInstanceId());
                    return taskMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * 历史环节记录
     */
    @GetMapping("/history/activities")
    public List<Map<String, Object>> historyActivities(@RequestParam String procInstId) {
        log.debug("[CTRL] 查询流程历史环节: procInstId={}", procInstId);

        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInstId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list()
                .stream()
                .map(a -> {
                    Map<String, Object> activityMap = new HashMap<>();
                    activityMap.put("activityId", a.getActivityId());
                    activityMap.put("activityName", a.getActivityName());
                    activityMap.put("activityType", a.getActivityType());
                    activityMap.put("startTime", a.getStartTime());
                    activityMap.put("endTime", a.getEndTime());
                    activityMap.put("durationInMillis", a.getDurationInMillis());
                    return activityMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * 办理记录（带操作人/意见）
     */
    @GetMapping("/history/tasks")
    public List<Map<String, Object>> historyTasks(@RequestParam String procInstId) {
        log.debug("[CTRL] 查询任务办理记录: procInstId={}", procInstId);

        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInstId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();

        List<Map<String, Object>> result = new ArrayList<>();
        for (HistoricTaskInstance t : tasks) {
            List<Comment> comments = taskService.getTaskComments(t.getId());
            if (!comments.isEmpty()) {
                for (Comment c : comments) {
                    Map<String, Object> commentMap = new HashMap<>();
                    commentMap.put("taskName", t.getName());
                    commentMap.put("assignee", t.getAssignee());
                    commentMap.put("action", parseAction(c.getFullMessage()));
                    commentMap.put("opinion", parseOpinion(c.getFullMessage()));
                    commentMap.put("time", c.getTime());
                    result.add(commentMap);
                }
            } else {
                // 如果没有评论，添加默认记录
                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("taskName", t.getName());
                taskMap.put("assignee", t.getAssignee());
                taskMap.put("action", t.getEndTime() != null ? "完成" : "进行中");
                taskMap.put("opinion", "");
                taskMap.put("time", t.getEndTime() != null ? t.getEndTime() : t.getCreateTime());
                result.add(taskMap);
            }
        }
        return result;
    }

    /**
     * 查询流程实例变量
     */
    @GetMapping("/variables/{procInstId}")
    public Map<String, Object> getVariables(@PathVariable String procInstId) {
        log.debug("[CTRL] 查询流程实例变量: procInstId={}", procInstId);

        return runtimeService.getVariables(procInstId);
    }

    /**
     * 设置流程实例变量
     */
    @PostMapping("/variables/{procInstId}")
    public Map<String, Object> setVariables(@PathVariable String procInstId,
                                          @RequestBody Map<String, Object> variables) {
        log.info("[CTRL] 设置流程实例变量: procInstId={}", procInstId);

        runtimeService.setVariables(procInstId, variables);

        Map<String, Object> result = new HashMap<>();
        result.put("procInstId", procInstId);
        result.put("message", "变量设置成功");
        return result;
    }

    private String parseAction(String msg) {
        if (msg == null) return "提交";
        if (msg.contains("退回")) return "退回";
        if (msg.contains("通过")) return "审批通过";
        return "提交";
    }

    private String parseOpinion(String msg) {
        if (msg == null) return "";
        int idx = msg.indexOf("意见:");
        return idx >= 0 ? msg.substring(idx + 3).trim() : "";
    }

    /**
     * 获取流程实例业务主键
     */
    private String getProcessInstanceBusinessKey(String processInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (pi != null) {
            return pi.getBusinessKey();
        }

        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        return hpi != null ? hpi.getBusinessKey() : null;
    }

    /**
     * 生成流程图 (PNG Base64)
     */
    @GetMapping("/diagram")
    public Map<String, Object> diagram(@RequestParam String procInstId) throws Exception {
        log.debug("[CTRL] 生成流程图: procInstId={}", procInstId);

        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInstId).singleResult();
        if (hpi == null) throw new IllegalStateException("流程不存在: " + procInstId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(hpi.getProcessDefinitionId());

        // 获取高亮活动ID
        List<String> activeActivityIds = runtimeService.createExecutionQuery()
                .processInstanceId(procInstId)
                .list()
                .stream()
                .map(Execution::getActivityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        ProcessDiagramGenerator generator = processEngineConfiguration.getProcessDiagramGenerator();
        try (InputStream in = generator.generateDiagram(
                bpmnModel, "png", activeActivityIds, new ArrayList<>(),
                processEngineConfiguration.getActivityFontName(),
                processEngineConfiguration.getLabelFontName(),
                processEngineConfiguration.getAnnotationFontName(),
                processEngineConfiguration.getClassLoader(), 1.0, true)) {

            // 修复 readAllBytes() 方法不兼容问题
            byte[] buffer = new byte[1024];
            int len;
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] bytes = baos.toByteArray();

            Map<String, Object> result = new HashMap<>();
            result.put("procInstId", procInstId);
            result.put("diagram", Base64.getEncoder().encodeToString(bytes));
            return result;
        }
    }

    /**
     * 删除流程实例（强制删除）
     */
    @DeleteMapping("/instance/{procInstId}")
    public Map<String, Object> deleteProcessInstance(@PathVariable String procInstId,
                                                   @RequestParam(defaultValue = "测试删除") String reason) {
        log.info("[CTRL] 删除流程实例: procInstId={}, reason={}", procInstId, reason);

        runtimeService.deleteProcessInstance(procInstId, reason);

        Map<String, Object> result = new HashMap<>();
        result.put("procInstId", procInstId);
        result.put("message", "流程实例删除成功");
        return result;
    }
}
