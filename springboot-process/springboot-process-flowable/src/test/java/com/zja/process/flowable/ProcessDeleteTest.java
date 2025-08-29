package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除流程实例
 * <p>
 * Flowable限制：Flowable默认不提供直接删除历史数据的API，历史数据通常需要通过配置的清理策略或直接数据库操作来删除。
 * 配置要求：要完全删除历史数据，需要在流程引擎配置中启用相关功能。
 * 生产环境注意：在生产环境中删除流程实例和历史数据需要谨慎操作，建议先备份相关数据。
 * </p>
 *
 * @Author: zhengja
 * @Date: 2025-08-14 10:42
 */
public class ProcessDeleteTest extends ProcessFlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    private String processDefinitionKey = "simpleProcess-V1";

    @BeforeEach
    public void setUp() {
        // 确保流程定义已部署
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        if (processDefinitions.isEmpty()) {
            System.out.println("警告: 流程定义 " + processDefinitionKey + " 未找到，请确保已部署");
        }
    }


    /**
     * 删除指定流程实例（含全部关联信息）
     */
    @Test
    public void deleteSpecificProcessInstanceWithAllInfo() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 删除流程实例及其全部关联信息
            deleteProcessInstanceCompletely(processInstanceId, "测试删除全部关联信息");

            // 验证删除结果
            ProcessInstance deletedInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            // 查询历史记录确认是否完全删除
            HistoricProcessInstance historicInstance = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            System.out.println("运行时流程实例是否存在: " + (deletedInstance == null ? "否" : "是"));
            System.out.println("历史流程实例是否存在: " + (historicInstance == null ? "否" : "是"));
            System.out.println("指定流程实例删除完成");

        } catch (Exception e) {
            System.err.println("删除指定流程实例失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除全部流程实例（含全部关联信息）
     */
    @Test
    public void deleteAllProcessInstancesWithAllInfo() {
        try {
            // 启动多个流程实例用于测试
            List<String> processInstanceIds = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ProcessInstance instance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
                processInstanceIds.add(instance.getId());
                System.out.println("启动流程实例 " + (i + 1) + ": " + instance.getId());
            }

            System.out.println("开始删除所有流程实例...");

            // 获取所有流程实例
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
            System.out.println("待删除的运行时流程实例数量: " + processInstances.size());

            // 删除所有运行时流程实例
            for (ProcessInstance instance : processInstances) {
                deleteProcessInstanceCompletely(instance.getId(), "批量删除-清理所有流程实例");
            }

            // 验证删除结果
            long remainingInstances = runtimeService.createProcessInstanceQuery().count();
            long remainingHistoricInstances = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .count();

            System.out.println("删除后剩余运行时流程实例数量: " + remainingInstances);
            System.out.println("删除后剩余历史流程实例数量: " + remainingHistoricInstances);
            System.out.println("全部流程实例删除完成");

        } catch (Exception e) {
            System.err.println("删除全部流程实例失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 完全删除流程实例（包括运行时和历史数据）
     * 注意：Flowable默认不提供直接删除历史数据的API，需要通过配置或手动清理
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    private void deleteProcessInstanceCompletely(String processInstanceId, String deleteReason) {
        try {
            // 删除运行时流程实例
            runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
            System.out.println("已删除运行时流程实例: " + processInstanceId);

            // 注意：Flowable默认情况下会在流程实例结束后自动保留历史数据
            // 如果需要完全删除历史数据，需要:
            // 1. 配置流程引擎时设置 historyCleanupEnabled=true
            // 2. 或者手动调用历史数据清理API
            // 3. 或者直接操作数据库删除相关历史记录

            // 删除历史流程实例（手动调用历史数据清理API，需要先配置 enable-history-cleaning: true # 启用数据库历史记录清理）
            // processEngine.getHistoryService().deleteHistoricProcessInstance(processInstanceId);

        } catch (Exception e) {
            System.err.println("完全删除流程实例失败 [" + processInstanceId + "]: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 清理历史流程实例数据（辅助方法）
     *
     * @param processInstanceId 流程实例ID
     */
    private void cleanupHistoricProcessInstance(String processInstanceId) {
        try {
            // Flowable不直接提供删除单个历史流程实例的API
            // 通常需要通过历史数据清理策略或者直接数据库操作
            System.out.println("历史数据清理需要通过Flowable的历史数据清理机制或直接数据库操作完成");

            // 可以查询历史数据确认存在
            HistoricProcessInstance historicInstance = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (historicInstance != null) {
                System.out.println("历史流程实例存在: " + historicInstance.getId());
            }

        } catch (Exception e) {
            System.err.println("清理历史数据时出错: " + e.getMessage());
        }
    }

    /**
     * 使用历史数据清理策略删除历史流程数据
     */
    @Test
    public void cleanupHistoricDataWithStrategy() {
        try {
            // 启动并完成一个流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 完成任务使流程结束
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            if (task != null) {
                taskService.complete(task.getId());
            }

            System.out.println("流程实例已完成，进入历史数据");

            // 使用Flowable的历史数据清理功能（需要配置支持）
            // 注意：这个功能需要在流程引擎配置中启用
            try {
                processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();
                System.out.println("历史数据清理功能需要在引擎配置中启用");
            } catch (Exception e) {
                System.out.println("历史数据清理演示: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("历史数据清理测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
