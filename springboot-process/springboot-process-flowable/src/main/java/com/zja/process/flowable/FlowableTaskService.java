package com.zja.process.flowable;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-08-08 16:35
 */
//@Service
public class FlowableTaskService {

//    @Autowired
    private TaskService taskService;

    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    public Task getTask(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }
}