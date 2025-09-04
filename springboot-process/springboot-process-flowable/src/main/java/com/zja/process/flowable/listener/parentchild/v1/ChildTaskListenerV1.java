package com.zja.process.flowable.listener.parentchild.v1;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * 子任务监听器（同步驱动父任务）
 *
 * @Author: zhengja
 * @Date: 2025-09-01 14:09
 */
@Component("childTaskListenerV1")
public class ChildTaskListenerV1 implements TaskListener {

    private static final Logger log = Logger.getLogger(ChildTaskListenerV1.class.getName());

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String event = delegateTask.getEventName(); // 事件名称
        String taskName = delegateTask.getName(); // 任务名称
        String taskDefKey = delegateTask.getTaskDefinitionKey(); // 节点（任务定义键）
        String childProcId = delegateTask.getProcessInstanceId(); // 子流程实例ID

        log.info(() -> String.format("【子流程任务事件-开始】事件=%s, 子任务环节名称=%s, 子环节节点主键ID=%s, 子流程实例ID=%s",
                event, taskName, taskDefKey, childProcId));

        if ("create".equals(event)) {
            log.info(() -> String.format(">>> 创建子任务: %s (实例=%s) -> 节点=%s",
                    taskName, childProcId, taskDefKey));
        }

        if ("assignment".equals(event)) {
            log.info(() -> String.format(">>> 分配子任务: %s (实例=%s) -> 节点=%s",
                    taskName, childProcId, taskDefKey));
        }

        if ("complete".equals(event)) {
            log.info(() -> String.format(">>> 子任务完成: %s (实例=%s) -> 节点=%s",
                    taskName, childProcId, taskDefKey));
        }

        if ("delete".equals(event)) {
            log.info(() -> String.format(">>> 删除子任务: %s (实例=%s) -> 节点=%s",
                    taskName, childProcId, taskDefKey));
        }

        log.info(() -> "【子流程任务事件-结束.】\n");
    }

}