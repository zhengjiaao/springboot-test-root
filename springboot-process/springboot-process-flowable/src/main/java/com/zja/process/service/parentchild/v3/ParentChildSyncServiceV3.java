package com.zja.process.service.parentchild.v3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2025-09-04 18:35
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ParentChildSyncServiceV3 {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public static final String VAR_PARENT_BIZ_KEY = "PARENT_BIZ_KEY";

    /**
     * 推进父任务，同时推进所有子流程
     */
    @Transactional
    public void completeParentAndChildren(String parentBizKey, String decision) {
        // 完成父流程任务
        Task parentTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(parentBizKey)
                .singleResult();
        if (parentTask == null) {
            throw new IllegalStateException("未找到父流程任务, bizKey=" + parentBizKey);
        }

        String action = "通过";
        String userId = "admin";
        String opinion = "审核通过";

        // 添加评论日志（在任务完成前）
        taskService.addComment(parentTask.getId(), parentTask.getProcessInstanceId(),
                String.format("【%s】操作: %s, 意见: %s", userId, action, opinion));
        log.debug("[CTRL] 添加审核通过评论, taskId={}", parentTask.getId());

        log.info("[FACADE] 完成父任务: {}, decision={}", parentTask.getName(), decision);
        Map<String, Object> vars = new HashMap<>();
        vars.put("decision", decision);
        taskService.complete(parentTask.getId(), vars);

        // 同步完成子流程任务
        List<Task> childTasks = taskService.createTaskQuery()
                .processVariableValueEquals(VAR_PARENT_BIZ_KEY, parentBizKey)
                .list();

        for (Task child : childTasks) {
            log.info("[FACADE] 自动完成子流程任务: {}, procInst={}", child.getName(), child.getProcessInstanceId());
            taskService.complete(child.getId(), vars);
        }
    }

    /**
     * 退回父任务，同时退回所有子流程
     */
    @Transactional
    public void rollbackParentAndChildren(String parentBizKey, String targetTaskKey, String childTargetKey) {
        // 父流程退回
        ProcessInstance parentPi = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(parentBizKey)
                .singleResult();
        if (parentPi == null) {
            throw new IllegalStateException("父流程未找到: " + parentBizKey);
        }

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(parentPi.getProcessInstanceId())
                .moveActivityIdTo(getCurrentActivityId(parentPi.getId()), targetTaskKey)
                .changeState();
        log.info("[FACADE] 父流程退回到 {}", targetTaskKey);

        // 子流程退回
        List<ProcessInstance> children = runtimeService.createProcessInstanceQuery()
                .variableValueEquals(VAR_PARENT_BIZ_KEY, parentBizKey)
                .list();
        for (ProcessInstance child : children) {
            List<String> execIds = runtimeService.createExecutionQuery()
                    .processInstanceId(child.getId())
                    .onlyChildExecutions()
                    .list()
                    .stream().map(Execution::getId)
                    .collect(Collectors.toList());

            runtimeService.createChangeActivityStateBuilder()
                    .moveExecutionsToSingleActivityId(execIds, childTargetKey)
                    .changeState();

            log.info("[FACADE] 子流程 {} 退回到 {}", child.getId(), childTargetKey);
        }
    }

    private String getCurrentActivityId(String procInstId) {
        Execution exec = runtimeService.createExecutionQuery()
                .processInstanceId(procInstId)
                .onlyChildExecutions()
                .singleResult();
        return exec.getActivityId();
    }
}
