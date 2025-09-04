package com.zja.process.flowable.listener.parentchild.v1;

import com.zja.process.service.parentchild.v1.ParentChildSyncServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 父流程任务同步监听器
 * 在父流程任务完成时，根据任务决策同步控制子流程的执行位置
 *
 * @Author: zhengja
 * @Date: 2025-09-04 17:04
 */
@Component("parentTaskSyncListenerV1")
@RequiredArgsConstructor
@Slf4j
public class ParentTaskSyncListenerV1 implements TaskListener {

    private final ParentChildSyncServiceV1 syncService;
    private final RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug("[TASK-LISTENER] 开始处理父流程任务事件, taskId={}", delegateTask.getId());

        try {
            // 获取父流程业务主键
            String parentBizKey = getParentBizKey(delegateTask);
            if (!StringUtils.hasText(parentBizKey)) {
                log.warn("[TASK-LISTENER] 父流程业务主键为空, taskId={}", delegateTask.getId());
                return;
            }

            String taskDefKey = delegateTask.getTaskDefinitionKey();
            String decision = (String) delegateTask.getVariable("decision");

            log.info("[TASK-LISTENER] 父流程任务完成: bizKey={}, taskId={}, defKey={}, decision={}",
                    parentBizKey, delegateTask.getId(), taskDefKey, decision);

            // 根据决策类型执行不同的同步操作
            if (isRejectDecision(decision)) {
                handleRejectDecision(parentBizKey, taskDefKey);
            } else {
                handleApproveDecision(parentBizKey, taskDefKey);
            }

            log.debug("[TASK-LISTENER] 父流程任务事件处理完成, taskId={}", delegateTask.getId());
        } catch (Exception e) {
            log.error("[TASK-LISTENER] 处理父流程任务事件时发生异常, taskId={}", delegateTask.getId(), e);
            throw new RuntimeException("处理父流程任务事件失败", e);
        }
    }

    /**
     * 获取父流程业务主键
     *
     * @param delegateTask 代理任务
     * @return 父流程业务主键
     */
    private String getParentBizKey(DelegateTask delegateTask) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
                .singleResult();

        if (processInstance == null) {
            log.warn("[TASK-LISTENER] 未找到流程实例, processInstanceId={}", delegateTask.getProcessInstanceId());
            return null;
        }

        return processInstance.getBusinessKey();
    }

    /**
     * 判断是否为退回决策
     *
     * @param decision 决策值
     * @return true表示退回，false表示通过
     */
    private boolean isRejectDecision(String decision) {
        return "REJECT".equalsIgnoreCase(decision);
    }

    /**
     * 处理退回决策
     *
     * @param parentBizKey 父流程业务主键
     * @param taskDefKey   任务定义键
     */
    private void handleRejectDecision(String parentBizKey, String taskDefKey) {
        String childTarget = mapChildRollbackTarget(taskDefKey);
        if (childTarget != null) {
            log.info("[TASK-LISTENER] 执行子流程退回操作: parentBizKey={}, target={}", parentBizKey, childTarget);
            syncService.rollbackChildrenTo(parentBizKey, childTarget);
        } else {
            log.warn("[TASK-LISTENER] 未找到退回映射关系, taskDefKey={}", taskDefKey);
        }
    }

    /**
     * 处理审批通过决策
     *
     * @param parentBizKey 父流程业务主键
     * @param taskDefKey   任务定义键
     */
    private void handleApproveDecision(String parentBizKey, String taskDefKey) {
        String childTarget = mapChildForwardTarget(taskDefKey);
        if (childTarget != null) {
            log.info("[TASK-LISTENER] 执行子流程推进操作: parentBizKey={}, target={}", parentBizKey, childTarget);
            syncService.moveChildrenTo(parentBizKey, childTarget);
        } else {
            log.warn("[TASK-LISTENER] 未找到推进映射关系, taskDefKey={}", taskDefKey);
        }
    }

    /**
     * 映射父流程环节到子流程推进目标环节
     *
     * @param parentActivityId 父流程环节ID
     * @return 子流程目标环节ID
     */
    private String mapChildForwardTarget(String parentActivityId) {
        switch (parentActivityId) {
            case "applyTask":
                return "childApproveTask";   // 父申请完成 → 子流程跟进到审批
            case "approveTask":
                return "childArchiveTask";   // 父审批完成 → 子流程跟进到归档
            case "archiveTask":
                return "childEndEvent";      // 父归档完成 → 子流程结束
            default:
                return null;
        }
    }

    /**
     * 映射父流程环节到子流程退回目标环节
     *
     * @param parentActivityId 父流程环节ID
     * @return 子流程目标环节ID
     */
    private String mapChildRollbackTarget(String parentActivityId) {
        switch (parentActivityId) {
            case "approveTask":
                return "childApplyTask";   // 父审批退回 → 子回申请
            case "archiveTask":
                return "childApproveTask"; // 父归档退回 → 子回审批
            default:
                return null;
        }
    }
}
