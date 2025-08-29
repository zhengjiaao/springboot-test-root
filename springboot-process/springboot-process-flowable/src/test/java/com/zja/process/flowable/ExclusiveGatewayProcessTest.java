package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Flowable排他网关流程测试类
 * 测试"网关-排他网关-请假单V1"流程，包含排他网关根据天数判断审批路径：
 * 1. 创建请假单
 * 2. 排他网关判断天数：
 * - 天数 < 3: 部门经理审批 -> 人事审批
 * - 天数 > 3: 总经理审批 -> 人事审批
 * 3. 人事审批 -> 结束
 *
 * @Author: zhengja
 * @Date: 2025-08-15 15:09
 */
public class ExclusiveGatewayProcessTest extends ProcessFlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormService formService;

    @Autowired
    private RepositoryService repositoryService;

    // 排他网关流程定义的Key
    private String processDefinitionKey = "exclusive-gateway-V1";

    @BeforeEach
    public void setUp() {
        try {
            // 检查流程定义是否已部署
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .list();

            if (processDefinitions.isEmpty()) {
                System.out.println("警告: 流程定义 " + processDefinitionKey + " 未找到，请确保已部署");

                // 列出所有已部署的流程定义
                List<ProcessDefinition> allDefinitions = repositoryService.createProcessDefinitionQuery().list();
                System.out.println("当前已部署的流程定义:");
                for (ProcessDefinition pd : allDefinitions) {
                    System.out.println("  - " + pd.getKey() + " (ID: " + pd.getId() + ", 版本: " + pd.getVersion() + ")");
                }
            } else {
                System.out.println("找到流程定义: " + processDefinitionKey + " (版本: " + processDefinitions.get(0).getVersion() + ")");
            }
        } catch (Exception e) {
            System.err.println("setUp方法执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景1: 获取流程定义信息
     * 验证排他网关流程定义是否存在以及基本信息
     */
    @Test
    public void getProcessDefinitionInfo() {
        try {
            System.out.println("========== 获取排他网关流程定义信息 ==========");

            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                System.out.println("未找到流程定义: " + processDefinitionKey);
                return;
            }

            System.out.println("流程定义ID: " + processDefinition.getId());
            System.out.println("流程定义名称: " + processDefinition.getName());
            System.out.println("流程定义Key: " + processDefinition.getKey());
            System.out.println("流程定义版本: " + processDefinition.getVersion());
            System.out.println("资源名称: " + processDefinition.getResourceName());

        } catch (Exception e) {
            System.err.println("获取流程定义信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景2: 列出所有已部署的流程定义
     * 帮助调试流程部署情况
     */
    @Test
    public void listAllProcessDefinitions() {
        try {
            System.out.println("========== 所有已部署的流程定义 ==========");

            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();

            if (processDefinitions.isEmpty()) {
                System.out.println("没有找到任何流程定义");
            } else {
                for (ProcessDefinition pd : processDefinitions) {
                    System.out.println("流程定义ID: " + pd.getId());
                    System.out.println("流程定义Key: " + pd.getKey());
                    System.out.println("流程定义名称: " + pd.getName());
                    System.out.println("版本: " + pd.getVersion());
                    System.out.println("资源名称: " + pd.getResourceName());
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            System.err.println("列出流程定义失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景3: 测试少于3天的请假流程（部门经理审批路径）
     * 测试路径: 创建请假单 -> 排他网关(days<3) -> 部门经理审批 -> 人事审批 -> 结束
     */
    @Test
    public void testLeaveProcessLessThan3Days() {
        try {
            System.out.println("========== 测试少于3天请假流程 ==========");

            // 启动流程实例，设置请假天数为2天
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("days", 2); // 小于3天，走部门经理审批路径

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 处理"创建请假单"任务
            processTaskByName(processInstance.getId(), "创建请假单", new HashMap<String, String>());

            // 验证当前任务应该是"部门经理审批"
            Task currentTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (currentTask != null && "部门经理审批".equals(currentTask.getName())) {
                System.out.println("✓ 正确路由到部门经理审批任务");
                processTaskByName(processInstance.getId(), "部门经理审批", new HashMap<String, String>());
            } else {
                System.out.println("✗ 未正确路由到部门经理审批任务");
                if (currentTask != null) {
                    System.out.println("  当前任务: " + currentTask.getName());
                }
                cleanupProcessInstance(processInstance.getId());
                return;
            }

            // 验证当前任务应该是"人事审批"
            currentTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (currentTask != null && "人事审批".equals(currentTask.getName())) {
                System.out.println("✓ 正确路由到人事审批任务");
                processTaskByName(processInstance.getId(), "人事审批", new HashMap<String, String>());
            } else {
                System.out.println("✗ 未正确路由到人事审批任务");
                if (currentTask != null) {
                    System.out.println("  当前任务: " + currentTask.getName());
                }
                cleanupProcessInstance(processInstance.getId());
                return;
            }

            // 验证流程结束
            verifyProcessCompletion(processInstance.getId());

        } catch (Exception e) {
            System.err.println("测试少于3天请假流程失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景4: 测试大于3天的请假流程（总经理审批路径）
     * 测试路径: 创建请假单 -> 排他网关(days>3) -> 总经理审批 -> 人事审批 -> 结束
     */
    @Test
    public void testLeaveProcessMoreThan3Days() {
        try {
            System.out.println("========== 测试大于3天请假流程 ==========");

            // 启动流程实例，设置请假天数为5天
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("days", 5); // 大于3天，走总经理审批路径

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 处理"创建请假单"任务
            processTaskByName(processInstance.getId(), "创建请假单", new HashMap<String, String>());

            // 验证当前任务应该是"总经理审批"
            Task currentTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (currentTask != null && "总经理审批".equals(currentTask.getName())) {
                System.out.println("✓ 正确路由到总经理审批任务");
                processTaskByName(processInstance.getId(), "总经理审批", new HashMap<String, String>());
            } else {
                System.out.println("✗ 未正确路由到总经理审批任务");
                if (currentTask != null) {
                    System.out.println("  当前任务: " + currentTask.getName());
                }
                cleanupProcessInstance(processInstance.getId());
                return;
            }

            // 验证当前任务应该是"人事审批"
            currentTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (currentTask != null && "人事审批".equals(currentTask.getName())) {
                System.out.println("✓ 正确路由到人事审批任务");
                processTaskByName(processInstance.getId(), "人事审批", new HashMap<String, String>());
            } else {
                System.out.println("✗ 未正确路由到人事审批任务");
                if (currentTask != null) {
                    System.out.println("  当前任务: " + currentTask.getName());
                }
                cleanupProcessInstance(processInstance.getId());
                return;
            }

            // 验证流程结束
            verifyProcessCompletion(processInstance.getId());

        } catch (Exception e) {
            System.err.println("测试大于3天请假流程失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景5: 测试等于3天的边界情况
     * 根据流程定义，等于3天应该不满足任何条件，需要验证流程行为
     */
    @Test
    public void testLeaveProcessEqual3Days() {
        try {
            System.out.println("========== 测试等于3天请假流程 ==========");

            // 启动流程实例，设置请假天数为3天
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("days", 3); // 等于3天，不满足任何条件

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 处理"创建请假单"任务
            processTaskByName(processInstance.getId(), "创建请假单", new HashMap<String, String>());

            // 等待一段时间让流程继续执行
            Thread.sleep(1000);

            // 检查流程状态
            Task currentTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (currentTask != null) {
                System.out.println("当前任务: " + currentTask.getName());
                System.out.println("流程仍在运行中，可能需要人工干预或默认路径");
            } else {
                System.out.println("流程已结束");
            }

            // 清理测试数据
            cleanupProcessInstance(processInstance.getId());

        } catch (Exception e) {
            System.err.println("测试等于3天请假流程失败: " + e.getMessage());
            // 处理任务 创建请假单 失败: No outgoing sequence flow of the exclusive gateway 'sid-A2604B0C-245C-4AB4-A9EA-35952ED45C23' could be selected for continuing the process
            e.printStackTrace();
        }
    }

    /**
     * 场景6: 验证流程中所有任务的表单属性
     */
    @Test
    public void validateTaskFormProperties() {
        try {
            System.out.println("========== 验证排他网关流程表单属性 ==========");

            // 启动流程实例
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("days", 2);

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 获取并显示所有任务的表单属性
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .list();

            for (Task task : tasks) {
                System.out.println("\n任务: " + task.getName());

                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                if (taskFormData != null) {
                    System.out.println("  表单属性数量: " + taskFormData.getFormProperties().size());

                    List<FormProperty> properties = taskFormData.getFormProperties();
                    for (FormProperty prop : properties) {
                        System.out.println("    属性ID: " + prop.getId());
                        System.out.println("    属性名称: " + prop.getName());
                        System.out.println("    属性类型: " + prop.getType().getName());
                        System.out.println("    是否必填: " + prop.isRequired());
                        System.out.println("    默认值: " + prop.getValue());
                    }
                } else {
                    System.out.println("  该任务没有表单定义");
                }

                // 完成任务
                taskService.complete(task.getId());
            }

            // 清理测试数据
            cleanupProcessInstance(processInstance.getId());

        } catch (Exception e) {
            System.err.println("验证表单属性失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景7: 完整流程测试（包含变量验证）
     */
    @Test
    public void completeProcessWithVariableValidation() {
        try {
            System.out.println("========== 完整流程测试（变量验证） ==========");

            // 启动流程实例
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("days", 1);
            variables.put("applicant", "张三");
            variables.put("leaveReason", "事假");

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
            System.out.println("启动流程实例: " + processInstance.getId());
            System.out.println("初始变量: days=" + variables.get("days") + ", applicant=" + variables.get("applicant"));

            // 处理所有任务
            boolean hasActiveTasks = true;
            int taskCounter = 0;

            while (hasActiveTasks) {
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstance.getId())
                        .list();

                if (tasks.isEmpty()) {
                    hasActiveTasks = false;
                    System.out.println("所有任务已完成");
                    break;
                }

                for (Task task : tasks) {
                    taskCounter++;
                    System.out.println("\n处理第" + taskCounter + "个任务: " + task.getName());

                    // 添加任务特定的变量
                    Map<String, Object> taskVariables = new HashMap<String, Object>();
                    switch (task.getName()) {
                        case "创建请假单":
                            taskVariables.put("createdDate", "2025-10-01");
                            break;
                        case "部门经理审批":
                            taskVariables.put("managerApproval", "approved");
                            taskVariables.put("managerComment", "同意");
                            break;
                        case "总经理审批":
                            taskVariables.put("gmApproval", "approved");
                            taskVariables.put("gmComment", "同意");
                            break;
                        case "人事审批":
                            taskVariables.put("hrApproval", "processed");
                            taskVariables.put("hrComment", "已处理");
                            break;
                    }

                    // 完成任务并设置变量
                    taskService.complete(task.getId(), taskVariables);
                    System.out.println("  任务完成，设置变量: " + taskVariables.size() + " 个");

                    ProcessInstance processInstanceV2 = runtimeService.createProcessInstanceQuery()
                            .processInstanceId(processInstance.getId())
                            .singleResult();
                    if (processInstanceV2 != null) {
                        // 显示流程变量
                        Map<String, Object> finalVariables = runtimeService.getVariables(processInstance.getId());
                        System.out.println("\n流程变量B:");
                        for (Map.Entry<String, Object> entry : finalVariables.entrySet()) {
                            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
                        }
                    }
                }
            }

            // 验证流程结束和最终变量
            verifyProcessCompletion(processInstance.getId());

        } catch (Exception e) {
            System.err.println("完整流程测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据任务名称处理任务
     */
    private void processTaskByName(String processInstanceId, String taskName, Map<String, String> variables) {
        try {
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskName(taskName)
                    .singleResult();

            if (task != null) {
                System.out.println("处理任务: " + task.getName() + " (ID: " + task.getId() + ")");
                formService.submitTaskFormData(task.getId(), variables);
                System.out.println("  ✓ 任务处理完成");
            } else {
                System.out.println("  ✗ 未找到任务: " + taskName);
            }
        } catch (Exception e) {
            System.err.println("处理任务 " + taskName + " 失败: " + e.getMessage());
        }
    }

    /**
     * 验证流程是否完成
     */
    private void verifyProcessCompletion(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                System.out.println("✓ 流程已成功完成");
            } else {
                System.out.println("✗ 流程仍在运行中");
                cleanupProcessInstance(processInstanceId);
            }
        } catch (Exception e) {
            System.err.println("验证流程完成状态失败: " + e.getMessage());
        }
    }

    /**
     * 清理流程实例
     */
    private void cleanupProcessInstance(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance != null) {
                runtimeService.deleteProcessInstance(processInstanceId, "测试清理");
                System.out.println("已清理流程实例: " + processInstanceId);
            }
        } catch (Exception e) {
            System.err.println("清理流程实例失败: " + e.getMessage());
        }
    }

    /**
     * 检查变量类型
     */
    @Test
    public void debugVariableTypes() {
        try {
            System.out.println("========== 调试变量类型 ==========");

            // 测试不同的变量设置方式
            Map<String, Object> variables1 = new HashMap<String, Object>();
            variables1.put("days", 5); // Integer
            System.out.println("变量类型测试1 - days类型: " + variables1.get("days").getClass().getName());

            Map<String, Object> variables2 = new HashMap<String, Object>();
            variables2.put("days", Integer.valueOf(5)); // 明确的Integer
            System.out.println("变量类型测试2 - days类型: " + variables2.get("days").getClass().getName());

            Map<String, Object> variables3 = new HashMap<String, Object>();
            variables3.put("days", "5"); // String
            System.out.println("变量类型测试3 - days类型: " + variables3.get("days").getClass().getName());

        } catch (Exception e) {
            System.err.println("调试变量类型失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
