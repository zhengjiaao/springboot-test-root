package com.zja.process.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 父子流程服务
 *
 * @Author: zhengja
 * @Date: 2025-09-01 14:10
 */
@Service
@Transactional
public class ParentChildProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    // 启动父流程
    public ProcessInstance startParentProcess(String businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey("parentProcess", businessKey, variables);
    }

    // 获取父流程任务
    public List<Task> getParentTasks(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    // 获取子流程任务
    public List<Task> getChildTasks(String parentInstanceId) {
        // 获取子流程实例ID
        Map<String, Object> variables = runtimeService.getVariables(parentInstanceId);
        String childInstance1 = (String) variables.get("childInstance1");
        String childInstance2 = (String) variables.get("childInstance2");

        List<Task> allChildTasks = new ArrayList<>();

        if (childInstance1 != null) {
            allChildTasks.addAll(taskService.createTaskQuery()
                    .processInstanceId(childInstance1)
                    .list());
        }

        if (childInstance2 != null) {
            allChildTasks.addAll(taskService.createTaskQuery()
                    .processInstanceId(childInstance2)
                    .list());
        }

        return allChildTasks;
    }

    // 完成任务
    public void completeTask(String taskId, Map<String, Object> variables) {
        if (variables != null && !variables.isEmpty()) {
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }
    }

    // 退回操作
    public void moveBackward(String processInstanceId, String currentTaskKey, String targetTaskKey) {
        // 获取当前执行ID
        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .activityId(currentTaskKey)
                .singleResult();

        if (execution != null) {
            // 移动执行到目标节点
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(processInstanceId)
                    .moveExecutionToActivityId(execution.getId(), targetTaskKey)
                    .changeState();
        }
    }

    // 获取流程状态
    public Map<String, Object> getProcessStatus(String parentInstanceId) {
        Map<String, Object> status = new HashMap<>();

        // 获取父流程任务
        List<Task> parentTasks = getParentTasks(parentInstanceId);
        status.put("parentTasks", parentTasks);

        // 获取子流程任务
        List<Task> childTasks = getChildTasks(parentInstanceId);
        status.put("childTasks", childTasks);

        // 获取完成状态
        Map<String, Object> variables = runtimeService.getVariables(parentInstanceId);
        Map<String, Object> completionStatus = new HashMap<>();
        completionStatus.put("child1ApplyCompleted", variables.getOrDefault("child1ApplyCompleted", false));
        completionStatus.put("child2ApplyCompleted", variables.getOrDefault("child2ApplyCompleted", false));
        completionStatus.put("child1ApproveCompleted", variables.getOrDefault("child1ApproveCompleted", false));
        completionStatus.put("child2ApproveCompleted", variables.getOrDefault("child2ApproveCompleted", false));
        completionStatus.put("child1ArchiveCompleted", variables.getOrDefault("child1ArchiveCompleted", false));
        completionStatus.put("child2ArchiveCompleted", variables.getOrDefault("child2ArchiveCompleted", false));
        status.put("completionStatus", completionStatus);

        return status;
    }
}