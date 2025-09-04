package com.zja.process.flowable.listener.parentchild.v2;

import com.zja.process.service.parentchild.v2.ParentChildSyncServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 父流程同步决策监听器
 * 用于在父流程环节完成时，根据决策结果同步控制子流程的执行位置
 *
 * @Author: zhengja
 * @Date: 2025-09-02 16:30
 */
@Slf4j
@Component("parentSyncDecisionListenerV2")
@RequiredArgsConstructor
public class ParentSyncDecisionListenerV2 implements ExecutionListener {
    private final ParentChildSyncServiceV2 syncService;

    @Override
    public void notify(DelegateExecution execution) {
        log.debug("[SYNC-STEP] 开始处理父流程同步决策, executionId={}", execution.getId());

        try {
            String parentBizKey = execution.getProcessInstanceBusinessKey();
            String activityId = execution.getCurrentActivityId();

            // 参数校验
            if (!StringUtils.hasText(parentBizKey)) {
                log.warn("[SYNC-STEP] 父流程业务ID为空, executionId={}", execution.getId());
                return;
            }

            if (!StringUtils.hasText(activityId)) {
                log.warn("[SYNC-STEP] 当前环节ID为空, executionId={}", execution.getId());
                return;
            }

            // 获取决策变量，支持 APPROVE(批准) 或 REJECT(退回)
            Object decision = execution.getVariable("decision");
            boolean isReject = decision != null && "REJECT".equalsIgnoreCase(decision.toString());

            log.info("[SYNC-STEP] 父流程环节完成: 流程业务ID={}, 决策结果={}, 当前环节ID={}",
                    parentBizKey, isReject ? "退回" : "批准", activityId);

            // 根据决策类型执行不同的同步操作
            if (isReject) {
                handleRejectDecision(parentBizKey, activityId);
            } else {
                handleApproveDecision(parentBizKey, activityId);
            }

            log.debug("[SYNC-STEP] 父流程同步决策处理完成, executionId={}", execution.getId());
        } catch (Exception e) {
            log.error("[SYNC-ERROR] 处理父流程同步决策时发生异常", e);
            // 重新抛出运行时异常以确保流程引擎能正确处理
            throw new RuntimeException("处理父流程同步决策失败: " + e.getMessage(), e);
        }
    }

    /**
     * 处理退回决策
     *
     * @param parentBizKey 父流程业务ID
     * @param activityId   当前环节ID
     */
    private void handleRejectDecision(String parentBizKey, String activityId) {
        log.debug("[SYNC-STEP] 开始处理退回决策, parentBizKey={}, activityId={}", parentBizKey, activityId);

        String childTargetId = mapChildRollbackTarget(activityId);
        if (childTargetId == null) {
            log.warn("[SYNC-STEP] 父环节 {} 未配置退回映射关系，跳过子流程同步", activityId);
            return;
        }

        log.info("[SYNC-STEP] 执行子流程退回操作: 父流程业务ID={}, 目标环节ID={}", parentBizKey, childTargetId);
        try {
            syncService.rollbackChildrenTo(parentBizKey, childTargetId);
            log.debug("[SYNC-STEP] 子流程退回操作执行完成, parentBizKey={}, targetId={}", parentBizKey, childTargetId);
        } catch (Exception e) {
            log.error("[SYNC-ERROR] 子流程退回操作执行失败: parentBizKey={}, targetId={}", parentBizKey, childTargetId, e);
            throw new RuntimeException("子流程退回操作执行失败", e);
        }
    }

    /**
     * 处理批准决策
     *
     * @param parentBizKey 父流程业务ID
     * @param activityId   当前环节ID
     */
    private void handleApproveDecision(String parentBizKey, String activityId) {
        log.debug("[SYNC-STEP] 开始处理批准决策, parentBizKey={}, activityId={}", parentBizKey, activityId);

        String childTargetId = mapChildForwardTarget(activityId);
        if (childTargetId == null) {
            log.warn("[SYNC-STEP] 父环节 {} 未配置推进映射关系，跳过子流程同步", activityId);
            return;
        }

        log.info("[SYNC-STEP] 执行子流程推进操作: 父流程业务ID={}, 目标环节ID={}", parentBizKey, childTargetId);
        try {
            syncService.moveChildrenTo(parentBizKey, childTargetId);
            log.debug("[SYNC-STEP] 子流程推进操作执行完成, parentBizKey={}, targetId={}", parentBizKey, childTargetId);
        } catch (Exception e) {
            log.error("[SYNC-ERROR] 子流程推进操作执行失败: parentBizKey={}, targetId={}", parentBizKey, childTargetId, e);
            throw new RuntimeException("子流程推进操作执行失败", e);
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
                log.debug("[SYNC-STEP] 未找到父环节 {} 的推进映射关系", parentActivityId);
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
                log.debug("[SYNC-STEP] 未找到父环节 {} 的退回映射关系", parentActivityId);
                return null;
        }
    }
}
