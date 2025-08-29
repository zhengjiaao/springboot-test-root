package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-08-13 13:49
 */
public class RuntimeServiceTest extends ProcessFlowableApplicationTests {

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

    @Test
    public void startSimpleProcessV1() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");

        System.out.println("========== 流程实例基本信息 ==========");
        System.out.println("流程实例ID: " + processInstance.getId()); // 流程实例ID: a96c9919-7818-11f0-8a53-8245ddc034b5
        System.out.println("流程实例名称: " + processInstance.getName()); // 流程实例名称: null
        System.out.println("流程实例描述: " + processInstance.getDescription()); // 流程实例描述: null

        System.out.println("\n========== 流程定义信息 ==========");
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId()); // 流程定义ID: simpleProcess-V1:1:fe24f112-7817-11f0-b62d-8245ddc034b5
        System.out.println("流程定义名称: " + processInstance.getProcessDefinitionName()); // 流程定义名称: 简单示例
        System.out.println("流程定义Key: " + processInstance.getProcessDefinitionKey()); // 流程定义Key: simpleProcess-V1
        System.out.println("流程定义版本: " + processInstance.getProcessDefinitionVersion()); // 流程定义版本: 1
        System.out.println("部署ID: " + processInstance.getDeploymentId()); // 部署ID: fdaf99fc-7817-11f0-b62d-8245ddc034b5

        System.out.println("\n========== 流程状态信息 ==========");
        System.out.println("是否挂起: " + processInstance.isSuspended()); // 是否挂起: false
        System.out.println("是否结束: " + processInstance.isEnded()); // 是否结束: false
        System.out.println("当前活动ID: " + processInstance.getActivityId()); // 当前活动ID: null

        System.out.println("\n========== 时间信息 ==========");
        System.out.println("启动时间: " + processInstance.getStartTime()); // 启动时间: Wed Aug 13 13:59:10 CST 2025

        System.out.println("\n========== 用户相关信息 ==========");
        System.out.println("启动用户ID: " + processInstance.getStartUserId()); // 启动用户ID: null

        System.out.println("\n========== 业务相关信息 ==========");
        System.out.println("业务Key: " + processInstance.getBusinessKey()); // 业务Key: null
        System.out.println("业务状态: " + processInstance.getBusinessStatus()); // 业务状态: null

        System.out.println("\n========== 关联信息 ==========");
        System.out.println("父流程实例ID: " + processInstance.getParentId()); // 父流程实例ID: null
        System.out.println("根流程实例ID: " + processInstance.getRootProcessInstanceId()); // 根流程实例ID: a96c9919-7818-11f0-8a53-8245ddc034b5
        System.out.println("超级执行ID: " + processInstance.getSuperExecutionId()); // 超级执行ID: null

        System.out.println("\n========== 租户信息 ==========");
        System.out.println("租户ID: " + processInstance.getTenantId());

        System.out.println("\n========== 回调信息 ==========");
        System.out.println("回调ID: " + processInstance.getCallbackId());
        System.out.println("回调类型: " + processInstance.getCallbackType());

        System.out.println("\n========== 引用信息 ==========");
        System.out.println("引用ID: " + processInstance.getReferenceId());
        System.out.println("引用类型: " + processInstance.getReferenceType());

        System.out.println("\n========== 本地化信息 ==========");
        System.out.println("本地化名称: " + processInstance.getLocalizedName());
        System.out.println("本地化描述: " + processInstance.getLocalizedDescription());

        System.out.println("\n========== 其他信息 ==========");
        System.out.println("传播阶段实例ID: " + processInstance.getPropagatedStageInstanceId());

        // 解决您提到的days变量问题
        System.out.println("\n========== 流程变量信息 ==========");
        System.out.println("流程变量: " + processInstance.getProcessVariables()); // 流程变量: {}

        // 如果需要设置days变量，可以这样操作
        // runtimeService.setVariable(processInstance.getId(), "days", 2);
    }

}
