package com.zja.process.util;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @Author: zhengja
 * @Date: 2025-09-01 14:55
 */
@Component
public class ProcessRollbackUtil {

    private static final Logger log = Logger.getLogger(ProcessRollbackUtil.class.getName());

    private final RuntimeService runtimeService;

    public ProcessRollbackUtil(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    /**
     * 回退到指定节点
     *
     * @param task             当前任务
     * @param targetActivityId 目标节点ID
     */
    public void rollbackTo(Task task, String targetActivityId) {
        String executionId = task.getExecutionId();
        Execution execution = runtimeService.createExecutionQuery()
                .executionId(executionId)
                .singleResult();

        if (execution == null) {
            log.warning("找不到执行实例，无法退回: " + executionId);
            return;
        }

        log.info(() -> String.format("执行退回: 当前任务=%s, 实例=%s, 回退到节点=%s",
                task.getName(), task.getProcessInstanceId(), targetActivityId));

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveExecutionToActivityId(execution.getId(), targetActivityId)
                .changeState();

        log.info("退回操作完成");
    }
}