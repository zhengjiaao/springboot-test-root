package com.zja.process.util;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * 通用退回服务类
 *
 * @Author: zhengja
 * @Date: 2025-09-01 15:03
 */
@Service
public class CommonRollbackService {

    private static final Logger log = Logger.getLogger(CommonRollbackService.class.getName());

    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public CommonRollbackService(RuntimeService runtimeService, HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    /**
     * 驳回至上一节点（示例：驳回到上一个 userTask）
     */
    public void rejectToPreviousTask(Task currentTask) {
        String processInstanceId = currentTask.getProcessInstanceId();

        // 查询历史任务，找到上一个完成的任务
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        if (historicTasks.isEmpty()) {
            throw new RuntimeException("无法找到上一个任务");
        }

        HistoricTaskInstance previousTask = historicTasks.get(0);
        String targetActivityId = previousTask.getTaskDefinitionKey();

        // 获取当前执行流
        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .activityId(currentTask.getTaskDefinitionKey())
                .singleResult();

        // 跳转到上一个节点（需确保流程定义支持该跳转）
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(processInstanceId)
                .moveExecutionToActivityId(execution.getId(), targetActivityId)
                .changeState();
    }
}