package com.zja.process.service.parentchild.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 父子流程同步服务（事务核心）
 * 提供父子流程的启动、状态同步、回退等核心功能
 *
 * @Author: zhengja
 * @Date: 2025-09-02 16:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParentChildSyncServiceV1 {

    /**
     * 父流程业务主键变量名
     */
    public static final String VAR_PARENT_BIZ_KEY = "PARENT_BIZ_KEY";

    private final RuntimeService runtimeService;

    /**
     * 启动一批子流程，绑定到父流程业务主键
     *
     * @param parentBizKey 父流程业务主键
     * @param childDefKey  子流程定义Key
     * @param count        启动子流程数量
     * @return 启动的子流程实例ID列表
     * @throws RuntimeException 启动流程失败时抛出
     */
    @Transactional
    public List<String> startChildren(String parentBizKey, String childDefKey, int count) {
        log.info("[BOOT] 开始启动子流程, parentBizKey={}, childDefKey={}, count={}", parentBizKey, childDefKey, count);

        // 参数校验
        if (parentBizKey == null || parentBizKey.trim().isEmpty()) {
            throw new IllegalArgumentException("父流程业务主键不能为空");
        }
        if (childDefKey == null || childDefKey.trim().isEmpty()) {
            throw new IllegalArgumentException("子流程定义Key不能为空");
        }
        if (count <= 0) {
            log.warn("[BOOT] 子流程数量无效, count={}", count);
            return Collections.emptyList();
        }

        try {
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Map<String, Object> vars = new HashMap<>();
                vars.put(VAR_PARENT_BIZ_KEY, parentBizKey);
                ProcessInstance pi = runtimeService.startProcessInstanceByKey(childDefKey, vars);
                ids.add(pi.getId());
                log.debug("[BOOT] 子流程启动成功, processInstanceId={}, parentBizKey={}", pi.getId(), parentBizKey);
            }

            log.info("[BOOT] 子流程启动完成, parentBizKey={}, 实际启动数量={}", parentBizKey, ids.size());
            return ids;
        } catch (Exception e) {
            log.error("[BOOT] 启动子流程失败, parentBizKey={}, childDefKey={}, count={}", parentBizKey, childDefKey, count, e);
            throw new RuntimeException("启动子流程失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将指定父流程的所有子流程迁移到目标环节
     *
     * @param parentBizKey  父流程业务主键
     * @param childTargetId 子流程目标环节ID
     * @throws RuntimeException 迁移失败时抛出
     */
    @Transactional
    public void moveChildrenTo(String parentBizKey, String childTargetId) {
        log.info("[SYNC-STEP] 开始迁移子流程, parentBizKey={}, childTargetId={}", parentBizKey, childTargetId);

        // 参数校验
        if (parentBizKey == null || parentBizKey.trim().isEmpty()) {
            throw new IllegalArgumentException("父流程业务主键不能为空");
        }
        if (childTargetId == null || childTargetId.trim().isEmpty()) {
            log.warn("[SYNC-STEP] 目标环节ID为空, 跳过迁移操作");
            return;
        }

        try {
            List<ProcessInstance> children = runtimeService.createProcessInstanceQuery()
                    .variableValueEquals(VAR_PARENT_BIZ_KEY, parentBizKey)
                    .active()
                    .list();

            if (CollectionUtils.isEmpty(children)) {
                log.info("[SYNC-STEP] 未找到活跃的子流程, parentBizKey={}", parentBizKey);
                return;
            }

            int successCount = 0;
            for (ProcessInstance child : children) {
                try {
                    List<String> execIds = runtimeService.createExecutionQuery()
                            .processInstanceId(child.getId())
                            .onlyChildExecutions()
                            .list()
                            .stream().map(Execution::getId)
                            .collect(Collectors.toList());

                    if (CollectionUtils.isEmpty(execIds)) {
                        log.warn("[SYNC-STEP] 子流程无执行实例, processInstanceId={}", child.getId());
                        continue;
                    }

                    runtimeService.createChangeActivityStateBuilder()
                            .moveExecutionsToSingleActivityId(execIds, childTargetId)
                            .changeState();

                    log.debug("[SYNC-STEP] 子流程迁移成功, processInstanceId={}, targetActivityId={}", child.getId(), childTargetId);
                    successCount++;
                } catch (Exception e) {
                    log.error("[SYNC-STEP] 子流程迁移失败, processInstanceId={}", child.getId(), e);
                    // 继续处理其他子流程
                }
            }

            log.info("[SYNC-STEP] 子流程迁移完成, parentBizKey={}, targetActivityId={}, 总数={}, 成功数={}",
                    parentBizKey, childTargetId, children.size(), successCount);
        } catch (Exception e) {
            log.error("[SYNC-STEP] 迁移子流程失败, parentBizKey={}, childTargetId={}", parentBizKey, childTargetId, e);
            throw new RuntimeException("迁移子流程失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将指定父流程的所有子流程回退到目标环节
     *
     * @param parentBizKey  父流程业务主键
     * @param childTargetId 子流程目标回退环节ID
     * @throws RuntimeException 回退失败时抛出
     */
    @Transactional
    public void rollbackChildrenTo(String parentBizKey, String childTargetId) {
        log.info("[SYNC-ROLLBACK] 开始回退子流程, parentBizKey={}, childTargetId={}", parentBizKey, childTargetId);

        // 参数校验
        if (parentBizKey == null || parentBizKey.trim().isEmpty()) {
            throw new IllegalArgumentException("父流程业务主键不能为空");
        }
        if (childTargetId == null || childTargetId.trim().isEmpty()) {
            log.warn("[SYNC-ROLLBACK] 回退目标环节ID为空, 跳过回退操作");
            return;
        }

        try {
            List<ProcessInstance> children = runtimeService.createProcessInstanceQuery()
                    .variableValueEquals(VAR_PARENT_BIZ_KEY, parentBizKey)
                    .active()
                    .list();

            if (CollectionUtils.isEmpty(children)) {
                log.info("[SYNC-ROLLBACK] 未找到活跃的子流程, parentBizKey={}", parentBizKey);
                return;
            }

            int successCount = 0;
            for (ProcessInstance child : children) {
                try {
                    List<String> execIds = runtimeService.createExecutionQuery()
                            .processInstanceId(child.getId())
                            .onlyChildExecutions()
                            .list()
                            .stream().map(Execution::getId)
                            .collect(Collectors.toList());

                    if (CollectionUtils.isEmpty(execIds)) {
                        log.warn("[SYNC-ROLLBACK] 子流程无执行实例, processInstanceId={}", child.getId());
                        continue;
                    }

                    runtimeService.createChangeActivityStateBuilder()
                            .moveExecutionsToSingleActivityId(execIds, childTargetId)
                            .changeState();

                    log.debug("[SYNC-ROLLBACK] 子流程回退成功, processInstanceId={}, targetActivityId={}", child.getId(), childTargetId);
                    successCount++;
                } catch (Exception e) {
                    log.error("[SYNC-ROLLBACK] 子流程回退失败, processInstanceId={}", child.getId(), e);
                    // 继续处理其他子流程
                }
            }

            log.info("[SYNC-ROLLBACK] 子流程回退完成, parentBizKey={}, targetActivityId={}, 总数={}, 成功数={}",
                    parentBizKey, childTargetId, children.size(), successCount);
        } catch (Exception e) {
            log.error("[SYNC-ROLLBACK] 回退子流程失败, parentBizKey={}, childTargetId={}", parentBizKey, childTargetId, e);
            throw new RuntimeException("回退子流程失败: " + e.getMessage(), e);
        }
    }
}
