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
 * Flowable表单流程测试类(内联表单)
 * 测试"简单示例-表单-V1"流程，包含三个用户任务环节：
 * 1. 环节A(请假申请) - form-A
 * 2. 环节B(部门审批) - form-B
 * 3. 环节C(HR归档) - form-C
 *
 * @Author: zhengja
 * @Date: 2025-08-15 15:09
 */
public class ProcessFormV2Test extends ProcessFlowableApplicationTests {

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

    // 流程定义的Key
    private String processDefinitionKey = "simpleProcess-form-V2";

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
     * 场景1: 获取流程定义信息
     * 验证流程定义是否存在以及基本信息
     */
    @Test
    public void getProcessDefinitionInfo() {
        try {
            System.out.println("========== 获取流程定义信息 ==========");

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
     * 场景2: 获取启动表单数据
     * 验证启动表单是否存在以及表单属性
     */
    @Test
    public void getStartFormModel() {
        try {
            System.out.println("========== 获取启动表单数据 ==========");

            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                System.out.println("未找到流程定义: " + processDefinitionKey);
                return;
            }

            // 获取启动表单数据
            StartFormData startFormData = formService.getStartFormData(processDefinition.getId());

            if (startFormData != null) {
                System.out.println("表单Key: " + startFormData.getFormKey());
                System.out.println("表单属性数量: " + startFormData.getFormProperties().size());
                if (startFormData.getFormKey() == null) {
                    System.out.println("该流程没有配置启动表单");
                    System.out.println("提示: 请检查BPMN文件中的开始事件是否配置了formKey或内部表单字段");
                    // <startEvent id="startEvent1" name="开始" flowable:formKey="start-form" flowable:formFieldValidation="true">
                }

                if (startFormData.getFormProperties().isEmpty()) {
                    System.out.println("警告: 启动表单没有定义任何属性");
                } else {
                    List<FormProperty> formProperties = startFormData.getFormProperties();
                    for (FormProperty property : formProperties) {
                        System.out.println("  属性ID: " + property.getId());
                        System.out.println("  属性名称: " + property.getName());
                        System.out.println("  属性类型: " + property.getType().getName());
                        System.out.println("  是否必填: " + property.isRequired());
                        System.out.println("  默认值: " + property.getValue());
                        System.out.println("  ---");
                    }
                }
            } else {
                System.out.println("该流程没有配置启动表单");
                System.out.println("提示: 请检查BPMN文件中的开始事件是否配置了formKey或内部表单字段");
            }

        } catch (Exception e) {
            System.err.println("获取启动表单数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景3: 获取所有任务表单数据
     * 验证各环节表单定义是否正确加载
     */
    @Test
    public void getTaskFormModels() {
        try {
            System.out.println("========== 获取所有任务表单数据 ==========");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 查询所有任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .list();

            System.out.println("找到 " + tasks.size() + " 个任务");

            for (Task task : tasks) {
                System.out.println("\n任务信息:");
                System.out.println("  任务ID: " + task.getId());
                System.out.println("  任务名称: " + task.getName());
                System.out.println("  任务定义Key: " + task.getTaskDefinitionKey());

                // 获取任务表单数据
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());

                if (taskFormData != null) {
                    System.out.println("  表单Key: " + taskFormData.getFormKey());
                    System.out.println("  表单属性数量: " + taskFormData.getFormProperties().size());

                    List<FormProperty> formProperties = taskFormData.getFormProperties();
                    for (FormProperty property : formProperties) {
                        System.out.println("    属性ID: " + property.getId());
                        System.out.println("    属性名称: " + property.getName());
                        System.out.println("    属性类型: " + property.getType().getName());
                        System.out.println("    是否必填: " + property.isRequired());
                        System.out.println("    默认值: " + property.getValue());
                        System.out.println("    ---");
                    }
                } else {
                    System.out.println("  该任务没有表单");
                }
            }

            // 清理测试数据（仅在流程仍在运行时才删除）
            if (runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count() > 0) {
                runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");
            }

        } catch (Exception e) {
            System.err.println("获取任务表单数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void checkFormFiles() {
        try {
            System.out.println("========== 检查表单文件 ==========");

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            // 检查表单文件是否存在
            String[] formFiles = {"forms/form-A.form", "forms/form-B.form", "forms/form-C.form"};

            for (String formFile : formFiles) {
                java.io.InputStream inputStream = classLoader.getResourceAsStream(formFile);
                if (inputStream != null) {
                    System.out.println("✓ 找到表单文件: " + formFile);
                    // 尝试解析JSON
                    try {
                        java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
                        String content = scanner.hasNext() ? scanner.next() : "";
                        System.out.println("  文件内容预览: " + content.substring(0, Math.min(100, content.length())) + "...");
                        inputStream.close();
                    } catch (Exception e) {
                        System.out.println("  文件解析错误: " + e.getMessage());
                    }
                } else {
                    System.out.println("✗ 未找到表单文件: " + formFile);
                }
            }

        } catch (Exception e) {
            System.err.println("检查表单文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景4: 验证各环节表单属性
     * 分别验证环节A、B、C的表单属性是否符合预期
     */
    @Test
    public void validateTaskFormProperties() {
        try {
            System.out.println("========== 验证各环节表单属性 ==========");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 处理所有任务并验证表单属性
            boolean hasActiveTasks = true;
            int taskIndex = 0;

            while (hasActiveTasks) {
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstance.getId())
                        .list();

                if (tasks.isEmpty()) {
                    hasActiveTasks = false;
                    break;
                }

                for (Task task : tasks) {
                    taskIndex++;
                    System.out.println("\n验证第" + taskIndex + "个任务: " + task.getName());

                    // 获取任务表单数据
                    TaskFormData taskFormData = formService.getTaskFormData(task.getId());

                    if (taskFormData != null) {
                        validateFormPropertiesByTaskName(task.getName(), taskFormData);
                        formService.submitTaskFormData(task.getId(), generateTestDataForTask(task.getName()));
                    }
                }
            }

            // 只有当流程实例仍然存在时才尝试删除
            // ProcessInstance instance = runtimeService.createProcessInstanceQuery()
            //         .processInstanceId(processInstance.getId())
            //         .singleResult();
            //
            // if (instance != null) {
            //     runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");
            // }

            // 检查流程实例最终状态
            String finalStatus = checkProcessInstanceFinalStatus(processInstance.getId());
            System.out.println("流程实例最终状态: " + finalStatus);

            switch (finalStatus) {
                case "RUNNING":
                    System.out.println("流程仍在运行中，需要清理");
                    runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");
                    break;
                case "COMPLETED":
                    System.out.println("流程已正常完成，无需清理");
                    break;
                case "NOT_FOUND":
                    System.out.println("流程实例不存在");
                    break;
                default:
                    System.out.println("流程实例处于未知状态: " + finalStatus);
                    break;
            }

        } catch (Exception e) {
            System.err.println("验证表单属性失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据任务名称验证表单属性
     */
    private void validateFormPropertiesByTaskName(String taskName, TaskFormData taskFormData) {
        System.out.println("  任务名称: " + taskName);
        System.out.println("  表单Key: " + (taskFormData.getFormKey() != null ? taskFormData.getFormKey() : "null"));
        System.out.println("  表单属性数量: " + taskFormData.getFormProperties().size());

        // 添加表单属性详细信息
        if (!taskFormData.getFormProperties().isEmpty()) {
            for (FormProperty prop : taskFormData.getFormProperties()) {
                System.out.println("    属性: " + prop.getId() + " (" + prop.getName() + ")");
            }
        } else {
            System.out.println("    警告: 未找到表单属性");
        }

        System.out.println("  验证表单属性:");
        switch (taskName) {
            case "环节A":
                validateFormAProperties(taskFormData);
                break;
            case "环节B":
                validateFormBProperties(taskFormData);
                break;
            case "环节C":
                validateFormCProperties(taskFormData);
                break;
            default:
                System.out.println("  未知任务类型: " + taskName);
        }
    }

    /**
     * 验证环节A(请假申请)表单属性
     */
    private void validateFormAProperties(TaskFormData taskFormData) {
        System.out.println("  验证环节A表单属性:");
        List<FormProperty> properties = taskFormData.getFormProperties();

        // 验证属性数量
        if (properties.size() != 6) {
            System.out.println("  警告: 环节A表单属性数量不正确，期望6个，实际" + properties.size() + "个");
        }

        // 验证关键属性
        for (FormProperty prop : properties) {
            switch (prop.getId()) {
                case "applicant":
                    if (!"申请人".equals(prop.getName()) || !"string".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: applicant属性配置不正确");
                    }
                    break;
                case "leaveType":
                    if (!"请假类型".equals(prop.getName()) || !"dropdown".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: leaveType属性配置不正确");
                    }
                    break;
                case "startDate":
                    if (!"开始日期".equals(prop.getName()) || !"date".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: startDate属性配置不正确");
                    }
                    break;
                case "endDate":
                    if (!"结束日期".equals(prop.getName()) || !"date".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: endDate属性配置不正确");
                    }
                    break;
                case "days":
                    if (!"请假天数".equals(prop.getName()) || !"number".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: days属性配置不正确");
                    }
                    break;
                case "reason":
                    if (!"请假事由".equals(prop.getName()) || !"textarea".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: reason属性配置不正确");
                    }
                    break;
            }
        }
        System.out.println("  环节A表单属性验证完成");
    }

    /**
     * 验证环节B(部门审批)表单属性
     */
    private void validateFormBProperties(TaskFormData taskFormData) {
        System.out.println("  验证环节B表单属性:");
        List<FormProperty> properties = taskFormData.getFormProperties();

        // 验证属性数量
        if (properties.size() != 3) {
            System.out.println("  警告: 环节B表单属性数量不正确，期望3个，实际" + properties.size() + "个");
        }

        // 验证关键属性
        for (FormProperty prop : properties) {
            switch (prop.getId()) {
                case "approvalResult":
                    if (!"审批结果".equals(prop.getName()) || !"radio".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: approvalResult属性配置不正确");
                    }
                    break;
                case "comment":
                    if (!"审批意见".equals(prop.getName()) || !"textarea".equals(prop.getType().getName()) || prop.isRequired()) {
                        System.out.println("  警告: comment属性配置不正确");
                    }
                    break;
                case "attachment":
                    if (!"补充材料".equals(prop.getName()) || !"file".equals(prop.getType().getName()) || prop.isRequired()) {
                        System.out.println("  警告: attachment属性配置不正确");
                    }
                    break;
            }
        }
        System.out.println("  环节B表单属性验证完成");
    }

    /**
     * 验证环节C(HR归档)表单属性
     */
    private void validateFormCProperties(TaskFormData taskFormData) {
        System.out.println("  验证环节C表单属性:");
        List<FormProperty> properties = taskFormData.getFormProperties();

        // 验证属性数量
        if (properties.size() != 2) {
            System.out.println("  警告: 环节C表单属性数量不正确，期望2个，实际" + properties.size() + "个");
        }

        // 验证关键属性
        for (FormProperty prop : properties) {
            switch (prop.getId()) {
                case "archiveStatus":
                    if (!"归档状态".equals(prop.getName()) || !"dropdown".equals(prop.getType().getName()) || !prop.isRequired()) {
                        System.out.println("  警告: archiveStatus属性配置不正确");
                    }
                    break;
                case "hrComment":
                    if (!"HR备注".equals(prop.getName()) || !"textarea".equals(prop.getType().getName()) || prop.isRequired()) {
                        System.out.println("  警告: hrComment属性配置不正确");
                    }
                    break;
            }
        }
        System.out.println("  环节C表单属性验证完成");
    }

    /**
     * 检查流程实例的最终状态
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例状态描述
     */
    private String checkProcessInstanceFinalStatus(String processInstanceId) {
        try {
            // 1. 首先检查运行时实例是否存在
            ProcessInstance runtimeInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (runtimeInstance != null) {
                return "RUNNING"; // 流程仍在运行中
            }

            // 2. 如果运行时实例不存在，检查历史实例
            HistoryService historyService = processEngine.getHistoryService();
            HistoricProcessInstance historicInstance =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId)
                            .singleResult();

            if (historicInstance == null) {
                return "NOT_FOUND"; // 流程实例不存在（从未存在或已被彻底删除）
            }

            // 3. 检查历史实例的结束时间来判断状态
            if (historicInstance.getEndTime() != null) {
                return "COMPLETED"; // 流程已正常完成
            } else {
                // 理论上不应该出现这种情况，因为如果在运行时表中找不到但在历史表中找到，
                // 且没有结束时间，那可能是数据不一致
                return "UNKNOWN";
            }

        } catch (Exception e) {
            System.err.println("检查流程实例状态时出错: " + e.getMessage());
            return "ERROR";
        }
    }

    /**
     * 场景5: 完整表单流程测试
     * 模拟完整的请假申请流程：环节A -> 环节B -> 环节C
     */
    @Test
    public void completeFormProcessDemo() {
        try {
            System.out.println("========== 完整表单流程测试 ==========");

            // 1. 提交环节A(请假申请)表单
            System.out.println("\n1. 提交环节A(请假申请)表单:");
            Map<String, String> formAProperties = new HashMap<>();
            formAProperties.put("applicant", "张三");
            formAProperties.put("leaveType", "年假");
            formAProperties.put("startDate", "2025-09-01");
            formAProperties.put("endDate", "2025-09-03");
            formAProperties.put("days", "3");
            formAProperties.put("reason", "家庭事务处理");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动流程实例: " + processInstance.getId());

            // ProcessInstance processInstance = formService.submitStartFormData(
            //         processDefinitionKey, formAProperties);
            // System.out.println("   流程实例ID: " + processInstance.getId());

            // 1. 处理环节A(申请)任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .list();
            Task task = tasks.get(0);
            formService.submitTaskFormData(task.getId(), formAProperties);

            // 2. 处理环节B(部门审批)任务
            System.out.println("\n2. 处理环节B(部门审批)任务:");
            processTaskByName(processInstance.getId(), "环节B", "approvalResult", "同意", "comment", "情况属实，同意请假");

            // 3. 处理环节C(HR归档)任务
            System.out.println("\n3. 处理环节C(HR归档)任务:");
            processTaskByName(processInstance.getId(), "环节C", "archiveStatus", "已归档", "hrComment", "请假流程已完成归档");

            // 4. 验证流程结束
            System.out.println("\n4. 验证流程状态:");
            ProcessInstance finalInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (finalInstance == null) {
                System.out.println("   ✓ 流程已成功结束");
            } else {
                System.out.println("   流程仍在运行中");
                // 清理测试数据
                runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");
            }

            System.out.println("\n完整表单流程测试完成！");

        } catch (Exception e) {
            System.err.println("完整表单流程测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景6: 表单数据验证测试
     * 测试表单数据提交后的验证
     */
    @Test
    public void formDataValidationTest() {
        try {
            System.out.println("========== 表单数据验证测试 ==========");

            // 1. 提交表单数据
            Map<String, String> formData = new HashMap<>();
            formData.put("applicant", "李四");
            formData.put("leaveType", "病假");
            formData.put("startDate", "2025-10-10");
            formData.put("endDate", "2025-10-12");
            formData.put("days", "3");
            formData.put("reason", "身体不适需要休息");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动流程实例: " + processInstance.getId());

            // ProcessInstance processInstance = formService.submitStartFormData(
            //         processDefinitionKey, formAProperties);
            // System.out.println("   流程实例ID: " + processInstance.getId());

            // 1. 处理环节A任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .list();
            Task task = tasks.get(0);
            formService.submitTaskFormData(task.getId(), formData);

            // 2. 验证流程变量
            Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
            System.out.println("\n流程变量验证:");

            for (Map.Entry<String, String> entry : formData.entrySet()) {
                Object value = variables.get(entry.getKey());
                if (value != null && value.toString().equals(entry.getValue())) {
                    System.out.println("  ✓ " + entry.getKey() + " = " + value);
                } else {
                    System.out.println("  ✗ " + entry.getKey() + " 验证失败");
                }
            }

            // 3. 清理测试数据（如果流程仍在运行）
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (instance != null) {
                runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");
            }
            System.out.println("\n表单数据验证测试完成！");

        } catch (Exception e) {
            System.err.println("表单数据验证测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景7: 动态表单处理测试
     * 演示如何动态处理不同任务的表单数据
     */
    @Test
    public void dynamicFormHandling() {
        try {
            System.out.println("========== 动态表单处理测试 ==========");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 动态处理所有任务
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

                    // 根据任务名称动态生成表单数据
                    Map<String, String> taskFormProperties = generateTestDataForTask(task.getName());

                    System.out.println("  提交表单数据:");
                    for (Map.Entry<String, String> entry : taskFormProperties.entrySet()) {
                        System.out.println("    " + entry.getKey() + " = " + entry.getValue());
                    }

                    formService.submitTaskFormData(task.getId(), taskFormProperties);
                    System.out.println("  任务表单提交完成");
                }
            }

            // 验证流程结束
            ProcessInstance finalInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (finalInstance == null) {
                System.out.println("✓ 流程已成功结束");
            } else {
                System.out.println("流程仍在运行中");
                // 清理测试数据
                runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");
            }

        } catch (Exception e) {
            System.err.println("动态表单处理测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 场景8: 多实例流程测试
     * 测试多个流程实例并行处理
     */
    @Test
    public void multiInstanceProcessTest() {
        try {
            System.out.println("========== 多实例流程测试 ==========");

            // 创建多个流程实例
            int instanceCount = 3;
            ProcessInstance[] instances = new ProcessInstance[instanceCount];

            for (int i = 0; i < instanceCount; i++) {
                Map<String, String> formData = new HashMap<>();
                formData.put("applicant", "用户" + (i + 1));
                formData.put("leaveType", "事假");
                formData.put("startDate", "2025-11-0" + (i + 1));
                formData.put("endDate", "2025-11-0" + (i + 2));
                formData.put("days", "1");
                formData.put("reason", "多实例测试" + (i + 1));

                // 启动流程实例
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
                System.out.println("启动流程实例: " + processInstance.getId());

                // instances[i] = formService.submitStartFormData(processDefinitionKey, formData);

                // 处理环节A
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstance.getId())
                        .list();
                Task task = tasks.get(0);
                formService.submitTaskFormData(task.getId(), formData);

                instances[i] = processInstance;
                System.out.println("创建流程实例 " + (i + 1) + ": " + instances[i].getId());
            }

            // 处理所有实例的任务
            for (int i = 0; i < instanceCount; i++) {
                System.out.println("\n处理实例 " + (i + 1) + " 的任务:");

                // 处理环节B
                processTaskByName(instances[i].getId(), "环节B", "approvalResult", "同意", "comment", "多实例审批通过");

                // 处理环节C
                processTaskByName(instances[i].getId(), "环节C", "archiveStatus", "已归档", "hrComment", "多实例归档完成");
            }

            // 验证所有流程结束
            System.out.println("\n验证流程状态:");
            boolean allCompleted = true;
            for (int i = 0; i < instanceCount; i++) {
                ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(instances[i].getId())
                        .singleResult();

                if (instance == null) {
                    System.out.println("  实例 " + (i + 1) + ": ✓ 已完成");
                } else {
                    System.out.println("  实例 " + (i + 1) + ": ✗ 仍在运行");
                    allCompleted = false;
                    // 清理未完成的实例
                    runtimeService.deleteProcessInstance(instances[i].getId(), "测试清理");
                }
            }

            if (allCompleted) {
                System.out.println("\n✓ 所有流程实例均已成功完成");
            }

            System.out.println("多实例流程测试完成！");

        } catch (Exception e) {
            System.err.println("多实例流程测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据任务名称生成测试数据
     */
    private Map<String, String> generateTestDataForTask(String taskName) {
        Map<String, String> formProperties = new HashMap<>();

        switch (taskName) {
            case "环节A":
                formProperties.put("applicant", "测试用户");
                formProperties.put("leaveType", "事假");
                formProperties.put("startDate", "2025-10-01");
                formProperties.put("endDate", "2025-10-02");
                formProperties.put("days", "2");
                formProperties.put("reason", "动态测试请假");
                break;

            case "环节B":
                formProperties.put("approvalResult", "同意");
                formProperties.put("comment", "动态审批通过");
                break;

            case "环节C":
                formProperties.put("archiveStatus", "已归档");
                formProperties.put("hrComment", "动态归档完成");
                break;

            default:
                formProperties.put("result", "completed");
                formProperties.put("remark", "默认处理");
                break;
        }

        return formProperties;
    }

    /**
     * 根据任务名称处理任务
     */
    private void processTaskByName(String processInstanceId, String taskName, String prop1, String value1, String prop2, String value2) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskName(taskName)
                .singleResult();

        if (task != null) {
            System.out.println("   处理任务: " + task.getName());

            Map<String, String> taskProperties = new HashMap<>();
            taskProperties.put(prop1, value1);
            if (prop2 != null && value2 != null) {
                taskProperties.put(prop2, value2);
            }

            formService.submitTaskFormData(task.getId(), taskProperties);
            System.out.println("   任务处理完成");
        } else {
            System.out.println("   未找到任务: " + taskName);
        }
    }

    /**
     * 使用程序化表单定义测试
     */
    @Test
    public void testWithProgrammaticFormDefinition() {
        try {
            System.out.println("========== 使用程序化表单定义测试 ==========");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey); // 采用：修改BPMN文件使用内联表单定义，直接在用户任务中定义表单字段
            System.out.println("启动流程实例: " + processInstance.getId());

            // 查询任务
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (task != null) {
                System.out.println("任务名称: " + task.getName());

                // 手动创建表单数据进行测试
                Map<String, String> formData = new HashMap<>();
                switch (task.getName()) {
                    case "环节A":
                        formData.put("applicant", "测试用户");
                        formData.put("leaveType", "annual");
                        formData.put("startDate", "2025-10-01");
                        formData.put("endDate", "2025-10-03");
                        formData.put("days", "3");
                        formData.put("reason", "测试请假");
                        break;
                    case "环节B":
                        formData.put("approvalResult", "approved");
                        formData.put("comment", "测试审批通过");
                        break;
                    case "环节C":
                        formData.put("archiveStatus", "archived");
                        formData.put("hrComment", "测试归档完成");
                        break;
                }

                // 提交表单数据
                formService.submitTaskFormData(task.getId(), formData);
                System.out.println("表单数据提交成功");
            }

            // 清理测试数据
            runtimeService.deleteProcessInstance(processInstance.getId(), "测试完成");

        } catch (Exception e) {
            System.err.println("程序化表单定义测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 完整测试V2流程（使用内联表单定义）
     */
    @Test
    public void completeFormProcessDemoV2() {
        try {
            System.out.println("========== 完整V2流程表单测试 ==========");

            // 1. 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("1. 启动流程实例: " + processInstance.getId());

            // 2. 处理环节A任务
            processTaskWithFormDataV2(processInstance.getId(), "环节A",
                    new HashMap<String, String>() {{
                        put("applicant", "张三");
                        put("leaveType", "annual");
                        put("startDate", "2025-10-01");
                        put("endDate", "2025-10-03");
                        put("days", "3");
                        put("reason", "年假申请");
                    }});

            // 3. 处理环节B任务
            processTaskWithFormDataV2(processInstance.getId(), "环节B",
                    new HashMap<String, String>() {{
                        put("approvalResult", "approved");
                        put("comment", "审批通过");
                        put("attachment", "file123.pdf");
                    }});

            // 4. 处理环节C任务
            processTaskWithFormDataV2(processInstance.getId(), "环节C",
                    new HashMap<String, String>() {{
                        put("archiveStatus", "archived");
                        put("hrComment", "已归档处理");
                    }});

            // 5. 验证流程完成
            ProcessInstance finalInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            if (finalInstance == null) {
                System.out.println("✓ 流程已成功完成");
            } else {
                System.out.println("✗ 流程仍在运行中");
                // 清理测试数据
                runtimeService.deleteProcessInstance(processInstance.getId(), "测试清理");
            }

            System.out.println("完整V2流程表单测试完成！");

        } catch (Exception e) {
            System.err.println("完整V2流程表单测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 处理特定任务的表单数据 (V2版本)
     */
    private void processTaskWithFormDataV2(String processInstanceId, String taskName, Map<String, String> formData) {
        try {
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskName(taskName)
                    .singleResult();

            if (task != null) {
                System.out.println("2. 处理任务: " + task.getName() + " (ID: " + task.getId() + ")");

                // 显示将要提交的表单数据
                System.out.println("   表单数据:");
                formData.forEach((key, value) -> System.out.println("     " + key + " = " + value));

                // 提交表单数据
                formService.submitTaskFormData(task.getId(), formData);
                System.out.println("   ✓ 表单提交成功");
            } else {
                System.out.println("   ✗ 未找到任务: " + taskName);
            }
        } catch (Exception e) {
            System.err.println("   处理任务 " + taskName + " 失败: " + e.getMessage());
        }
    }

    /**
     * 验证V2流程表单属性
     */
    @Test
    public void validateTaskFormPropertiesV2() {
        try {
            System.out.println("========== 验证V2流程表单属性 ==========");

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动流程实例: " + processInstance.getId());

            // 获取并验证所有任务的表单属性
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .list();

            for (Task task : tasks) {
                System.out.println("\n任务: " + task.getName());

                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                if (taskFormData != null) {
                    System.out.println("  表单Key: " + taskFormData.getFormKey());
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
            }

            // 清理测试数据
            runtimeService.deleteProcessInstance(processInstance.getId(), "测试清理");

        } catch (Exception e) {
            System.err.println("验证V2流程表单属性失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
