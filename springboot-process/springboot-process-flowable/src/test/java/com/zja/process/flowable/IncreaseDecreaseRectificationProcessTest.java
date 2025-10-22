package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2025-10-22 14:18
 */
@Slf4j
public class IncreaseDecreaseRectificationProcessTest extends ProcessFlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    private static final String PROCESS_DEFINITION_KEY = "increaseDecreaseRectificationV1";

    /*@BeforeEach
    public void setUp() {
        // 部署流程定义
        repositoryService.createDeployment()
                .addClasspathResource("processes/增减挂整改监管流程.bpmn20.xml")
                .deploy();
    }*/

    @BeforeEach
    public void setUp() {
        try {
            // 检查流程定义是否已部署
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(PROCESS_DEFINITION_KEY)
                    .list();

            if (processDefinitions.isEmpty()) {
                log.warn("警告: 流程定义 {} 未找到，请确保已部署", PROCESS_DEFINITION_KEY);

                // 列出所有已部署的流程定义
                List<ProcessDefinition> allDefinitions = repositoryService.createProcessDefinitionQuery().list();
                log.info("当前已部署的流程定义数量: {}", allDefinitions.size());
                for (ProcessDefinition pd : allDefinitions) {
                    log.info("  - {} (ID: {}, 版本: {})", pd.getKey(), pd.getId(), pd.getVersion());
                }
            } else {
                log.info("找到流程定义: {} (版本: {})", PROCESS_DEFINITION_KEY, processDefinitions.get(0).getVersion());
            }
        } catch (Exception e) {
            log.error("setUp方法执行失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 测试完整流程 - 正常通过流程
     */
    @Test
    public void testCompleteProcessFlow() {
        log.info("开始执行完整流程测试 - 正常通过流程");

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
        log.info("流程实例已启动, 实例ID: {}", processInstance.getId());

        // 验证流程已启动
        assertNotNull(processInstance);
        assertFalse(processInstance.isEnded());
        log.info("流程实例验证通过");

        // 第一步：上报任务
        Task reportTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("上报")
                .singleResult();

        assertNotNull(reportTask);
        log.info("获取到上报任务, 任务ID: {}, 任务名称: {}", reportTask.getId(), reportTask.getName());

        // 完成上报任务
        Map<String, Object> reportVariables = new HashMap<>();
        reportVariables.put("reportData", "测试上报数据");
        taskService.complete(reportTask.getId(), reportVariables);
        log.info("上报任务已完成, 任务ID: {}", reportTask.getId());

        // 第二步：审查任务
        Task reviewTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("审查")
                .singleResult();

        assertNotNull(reviewTask);
        log.info("获取到审查任务, 任务ID: {}, 任务名称: {}", reviewTask.getId(), reviewTask.getName());

        // 完成审查任务，选择通过
        Map<String, Object> reviewVariables = new HashMap<>();
        reviewVariables.put("operation", "pass");
        taskService.complete(reviewTask.getId(), reviewVariables);
        log.info("审查任务已完成(通过), 任务ID: {}", reviewTask.getId());

        // 第三步：经办人审查任务
        Task handlerReviewTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("经办人审查")
                .singleResult();

        assertNotNull(handlerReviewTask);
        log.info("获取到经办人审查任务, 任务ID: {}, 任务名称: {}", handlerReviewTask.getId(), handlerReviewTask.getName());

        // 完成经办人审查任务，选择结束流程
        Map<String, Object> handlerVariables = new HashMap<>();
        handlerVariables.put("operation", "end");
        taskService.complete(handlerReviewTask.getId(), handlerVariables);
        log.info("经办人审查任务已完成(结束), 任务ID: {}", handlerReviewTask.getId());

        // 验证流程已结束
        ProcessInstance finishedProcessInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        assertNull(finishedProcessInstance); // 流程实例已结束，查询应返回null
        log.info("流程实例已正确结束, 实例ID: {}", processInstance.getId());
    }

    /**
     * 测试流程 - 审查阶段退回
     */
    @Test
    public void testReviewRejectFlow() {
        log.info("开始执行审查阶段退回测试");

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
        log.info("流程实例已启动, 实例ID: {}", processInstance.getId());

        // 完成上报任务
        Task reportTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("上报")
                .singleResult();

        Map<String, Object> reportVariables = new HashMap<>();
        reportVariables.put("reportData", "测试上报数据");
        taskService.complete(reportTask.getId(), reportVariables);
        log.info("上报任务已完成, 任务ID: {}", reportTask.getId());

        // 审查任务 - 选择退回
        Task reviewTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("审查")
                .singleResult();

        log.info("获取到审查任务, 任务ID: {}, 任务名称: {}", reviewTask.getId(), reviewTask.getName());

        Map<String, Object> reviewVariables = new HashMap<>();
        reviewVariables.put("operation", "reject");
        taskService.complete(reviewTask.getId(), reviewVariables);
        log.info("审查任务已完成(退回), 任务ID: {}", reviewTask.getId());

        // 验证流程退回至上报任务
        List<Task> reportTasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("上报")
                .list();

        assertEquals(1, reportTasks.size());
        assertEquals("上报", reportTasks.get(0).getName());
        log.info("流程已正确退回至上报任务, 任务ID: {}", reportTasks.get(0).getId());
    }

    /**
     * 测试流程 - 经办人审查阶段退回
     */
    @Test
    public void testHandlerReviewRejectFlow() {
        log.info("开始执行经办人审查阶段退回测试");

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
        log.info("流程实例已启动, 实例ID: {}", processInstance.getId());

        // 完成上报任务
        Task reportTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("上报")
                .singleResult();

        Map<String, Object> reportVariables = new HashMap<>();
        reportVariables.put("reportData", "测试上报数据");
        taskService.complete(reportTask.getId(), reportVariables);
        log.info("上报任务已完成, 任务ID: {}", reportTask.getId());

        // 完成审查任务，选择通过
        Task reviewTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("审查")
                .singleResult();

        Map<String, Object> reviewVariables = new HashMap<>();
        reviewVariables.put("operation", "pass");
        taskService.complete(reviewTask.getId(), reviewVariables);
        log.info("审查任务已完成(通过), 任务ID: {}", reviewTask.getId());

        // 经办人审查任务 - 选择退回
        Task handlerReviewTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("经办人审查")
                .singleResult();

        log.info("获取到经办人审查任务, 任务ID: {}, 任务名称: {}", handlerReviewTask.getId(), handlerReviewTask.getName());

        Map<String, Object> handlerVariables = new HashMap<>();
        handlerVariables.put("operation", "reject");
        taskService.complete(handlerReviewTask.getId(), handlerVariables);
        log.info("经办人审查任务已完成(退回), 任务ID: {}", handlerReviewTask.getId());

        // 验证流程退回至上报任务
        List<Task> reportTasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("上报")
                .list();

        assertEquals(1, reportTasks.size());
        assertEquals("上报", reportTasks.get(0).getName());
        log.info("流程已正确退回至上报任务, 任务ID: {}", reportTasks.get(0).getId());
    }

    /**
     * 测试流程变量传递
     */
    @Test
    public void testProcessVariables() {
        log.info("开始执行流程变量传递测试");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
        log.info("流程实例已启动, 实例ID: {}", processInstance.getId());

        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("projectName", "增减挂项目");
        variables.put("applicant", "张三");
        runtimeService.setVariables(processInstance.getId(), variables);
        log.info("流程变量已设置: projectName=增减挂项目, applicant=张三");

        // 验证变量是否正确设置
        Object projectName = runtimeService.getVariable(processInstance.getId(), "projectName");
        Object applicant = runtimeService.getVariable(processInstance.getId(), "applicant");

        assertEquals("增减挂项目", projectName);
        assertEquals("张三", applicant);
        log.info("流程变量验证通过: projectName={}, applicant={}", projectName, applicant);
    }
}
