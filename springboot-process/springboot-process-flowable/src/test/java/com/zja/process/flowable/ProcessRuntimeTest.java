package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程运行操作 - 完善版
 * 包含各种场景下的流程运行操作实现
 *
 * @Author: zhengja
 * @Date: 2025-08-13 18:05
 */
public class ProcessRuntimeTest extends ProcessFlowableApplicationTests {

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

    // 操作流程运行：推进流程、回退流程等操作
    @Test
    public void operateProcess() {
        // 1. 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 2. 查看当前流程状态
        printProcessStatus(processInstanceId);

        // 3. 推进流程 - 完成当前任务
        completeCurrentTask(processInstanceId);

        // 4. 再次查看流程状态
        printProcessStatus(processInstanceId);

        // 5. 演示跳转到指定节点（如果流程支持）
        // jumpToActivity(processInstanceId, "targetActivityId");

        // 6. 演示流程回退（如果需要）
        // rollbackToPreviousActivity(processInstanceId);
    }

    // 完成当前任务
    private void completeCurrentTask(String processInstanceId) {
        try {
            // 查询当前任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            if (tasks.isEmpty()) {
                System.out.println("当前没有待处理任务");
                return;
            }

            for (Task task : tasks) {
                System.out.println("完成任务: " + task.getName() + " (ID: " + task.getId() + ")");

                // 可以添加任务变量
                // taskService.setVariable(task.getId(), "approved", true);

                // 完成任务
                taskService.complete(task.getId());
            }

        } catch (Exception e) {
            System.err.println("完成任务失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 查看流程状态
    private void printProcessStatus(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                System.out.println("流程实例已结束或不存在");
                return;
            }

            System.out.println("\n========== 流程状态信息 ==========");
            System.out.println("流程实例ID: " + processInstance.getId());
            System.out.println("流程状态: " + (processInstance.isEnded() ? "已结束" : "进行中"));
            System.out.println("是否挂起: " + processInstance.isSuspended());

            // 获取当前活跃活动
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
            System.out.println("当前活跃活动: " + activeActivityIds);

            // 查询当前任务
            List<Task> currentTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            System.out.println("\n========== 当前待办任务 ==========");
            if (currentTasks.isEmpty()) {
                System.out.println("暂无待办任务");
            } else {
                for (Task task : currentTasks) {
                    System.out.println("任务ID: " + task.getId());
                    System.out.println("任务名称: " + task.getName());
                    System.out.println("任务描述: " + task.getDescription());
                    System.out.println("负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));
                    System.out.println("创建时间: " + task.getCreateTime());
                    System.out.println("---");
                }
            }

        } catch (Exception e) {
            System.err.println("获取流程状态失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 跳转到指定活动节点
    public void jumpToActivity(String processInstanceId, String targetActivityId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                System.out.println("流程实例不存在");
                return;
            }

            // 获取流程定义ID
            String processDefinitionId = processInstance.getProcessDefinitionId();

            // 获取BPMN模型
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            FlowElement targetElement = bpmnModel.getMainProcess().getFlowElement(targetActivityId);

            if (targetElement == null) {
                System.out.println("目标活动节点不存在: " + targetActivityId);
                return;
            }

            // 获取当前执行
            List<Execution> executions = runtimeService.createExecutionQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            // 跳转到指定节点（注意：这需要根据具体流程引擎版本调整）
            for (Execution execution : executions) {
                if (execution.getActivityId() != null) {
                    System.out.println("从节点 " + execution.getActivityId() + " 跳转到 " + targetActivityId);
                    // runtimeService.createChangeActivityStateBuilder()
                    //         .processInstanceId(processInstanceId)
                    //         .moveActivityIdTo(execution.getActivityId(), targetActivityId)
                    //         .changeState();
                }
            }

            System.out.println("已跳转到活动节点: " + targetActivityId);

        } catch (Exception e) {
            System.err.println("跳转节点失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 回退到上一个活动节点
    public void rollbackToPreviousActivity(String processInstanceId) {
        try {
            // 获取历史活动记录
            List<HistoricActivityInstance> historicActivities = processEngine.getHistoryService()
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceEndTime()
                    .desc()
                    .list();

            if (historicActivities.size() < 2) {
                System.out.println("没有足够的历史记录用于回退");
                return;
            }

            // 获取倒数第二个已完成的活动（即上一个活动）
            HistoricActivityInstance previousActivity = null;
            for (HistoricActivityInstance activity : historicActivities) {
                if (!"sequenceFlow".equals(activity.getActivityType()) &&
                        activity.getEndTime() != null) {
                    previousActivity = activity;
                    break;
                }
            }

            if (previousActivity == null) {
                System.out.println("未找到可回退的活动节点");
                return;
            }

            System.out.println("回退到活动: " + previousActivity.getActivityName() +
                    " (ID: " + previousActivity.getActivityId() + ")");

            // 执行回退操作（需要根据具体需求实现）
            // 这通常需要使用Flowable的ChangeActivityStateBuilder或者自定义逻辑

        } catch (Exception e) {
            System.err.println("回退操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 挂起流程实例
    @Test
    public void suspendProcessInstance() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();

        System.out.println("启动流程实例: " + processInstanceId);

        // 挂起流程实例
        runtimeService.suspendProcessInstanceById(processInstanceId);
        System.out.println("流程实例已挂起: " + processInstanceId);

        // 验证挂起状态
        ProcessInstance suspendedInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println("挂起状态: " + suspendedInstance.isSuspended());

        // 恢复流程实例
        runtimeService.activateProcessInstanceById(processInstanceId);
        System.out.println("流程实例已恢复: " + processInstanceId);

        // 验证恢复状态
        ProcessInstance activatedInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println("激活状态: " + activatedInstance.isSuspended());
    }

    // 设置流程变量
    @Test
    public void setProcessVariables() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();

        System.out.println("启动流程实例: " + processInstanceId);

        // 设置流程变量
        runtimeService.setVariable(processInstanceId, "days", 3);
        runtimeService.setVariable(processInstanceId, "approved", true);
        runtimeService.setVariable(processInstanceId, "approver", "admin");

        // 获取流程变量
        Object days = runtimeService.getVariable(processInstanceId, "days");
        Object approved = runtimeService.getVariable(processInstanceId, "approved");
        Object approver = runtimeService.getVariable(processInstanceId, "approver");

        System.out.println("流程变量:");
        System.out.println("days: " + days);
        System.out.println("approved: " + approved);
        System.out.println("approver: " + approver);

        // 完成任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            System.out.println("完成任务: " + task.getName());
            taskService.complete(task.getId());
        }
    }

    // 批量操作多个流程实例
    @Test
    public void batchOperateProcesses() {
        // 启动多个流程实例
        List<String> processInstanceIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProcessInstance instance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            processInstanceIds.add(instance.getId());
            System.out.println("启动流程实例 " + (i + 1) + ": " + instance.getId());
        }

        // 批量完成任务
        for (String processInstanceId : processInstanceIds) {
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (task != null) {
                System.out.println("完成流程 " + processInstanceId + " 的任务: " + task.getName());
                taskService.complete(task.getId());
            }
        }

        // 检查流程状态
        System.out.println("\n========== 批量操作后流程状态 ==========");
        for (String processInstanceId : processInstanceIds) {
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (instance == null) {
                System.out.println("流程 " + processInstanceId + ": 已结束");
            } else {
                System.out.println("流程 " + processInstanceId + ": " +
                        (instance.isEnded() ? "已结束" : "进行中"));
            }
        }
    }

    /**
     * 流程实例删除操作
     */
    @Test
    public void deleteProcessInstance() {
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 查看流程状态
        printProcessStatus(processInstanceId);

        // 删除流程实例（正常删除）
        runtimeService.deleteProcessInstance(processInstanceId, "测试删除");
        System.out.println("流程实例已删除: " + processInstanceId);

        // 验证删除
        ProcessInstance deletedInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println("删除后查询结果: " + (deletedInstance == null ? "流程实例不存在" : "仍存在"));
    }

    /**
     * 带原因的流程实例删除
     */
    @Test
    public void deleteProcessInstanceWithReason() {
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 模拟业务原因删除
        String deleteReason = "业务变更，流程作废";
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        System.out.println("流程实例因[" + deleteReason + "]已删除: " + processInstanceId);

        // 查询历史记录确认删除原因
        HistoricProcessInstance historicInstance = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicInstance != null) {
            System.out.println("历史记录中的删除原因: " + historicInstance.getDeleteReason());
        }
    }

    /**
     * 流程信号发送操作
     */
    @Test
    public void sendProcessSignal() {
        try {
            // 启动等待信号的流程实例（假设有这样的流程）
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            System.out.println("启动等待信号的流程实例: " + processInstance.getId());

            // 发送信号（需要流程中定义了相应的信号捕获事件）
            runtimeService.signalEventReceived("testSignal");
            System.out.println("信号[testSignal]已发送");

        } catch (Exception e) {
            System.out.println("发送信号操作演示（需要相应流程支持）: " + e.getMessage());
        }
    }

    /**
     * 流程消息发送操作
     */
    @Test
    public void sendProcessMessage() {
        try {
            // 启动等待消息的流程实例（假设有这样的流程）
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            System.out.println("启动等待消息的流程实例: " + processInstance.getId());

            // 发送消息（需要流程中定义了相应的消息捕获事件）
            runtimeService.messageEventReceived("testMessage", processInstance.getId());
            System.out.println("消息[testMessage]已发送到流程实例: " + processInstance.getId());

        } catch (Exception e) {
            System.out.println("发送消息操作演示（需要相应流程支持）: " + e.getMessage());
        }
    }

    /**
     * 流程变量批量操作
     */
    @Test
    public void batchProcessVariables() {
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 批量设置变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", "张三");
        variables.put("department", "技术部");
        variables.put("days", 5);
        variables.put("amount", 1000.00);
        variables.put("approved", false);

        runtimeService.setVariables(processInstanceId, variables);
        System.out.println("批量设置流程变量完成");

        // 批量获取变量
        Map<String, Object> retrievedVariables = runtimeService.getVariables(processInstanceId);
        System.out.println("\n批量获取的流程变量:");
        for (Map.Entry<String, Object> entry : retrievedVariables.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 更新部分变量
        Map<String, Object> updateVariables = new HashMap<>();
        updateVariables.put("approved", true);
        updateVariables.put("approvedBy", "李四");

        runtimeService.setVariables(processInstanceId, updateVariables);
        System.out.println("\n更新部分变量完成");

        // 验证更新
        Object approved = runtimeService.getVariable(processInstanceId, "approved");
        Object approvedBy = runtimeService.getVariable(processInstanceId, "approvedBy");
        System.out.println("更新后变量 - approved: " + approved + ", approvedBy: " + approvedBy);
    }

    /**
     * 流程任务委派操作
     */
    @Test
    public void delegateTask() {
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            String originalAssignee = task.getAssignee();
            System.out.println("原任务负责人: " + (originalAssignee != null ? originalAssignee : "未分配"));

            // 委派任务给其他人
            String delegateTo = "manager";
            taskService.delegateTask(task.getId(), delegateTo);
            System.out.println("任务已委派给: " + delegateTo);

            // 查询委派后的任务
            Task delegatedTask = taskService.createTaskQuery()
                    .taskId(task.getId())
                    .singleResult();

            System.out.println("委派后任务信息:");
            System.out.println("  任务ID: " + delegatedTask.getId());
            System.out.println("  任务名称: " + delegatedTask.getName());
            System.out.println("  当前处理人: " + delegatedTask.getAssignee());
            System.out.println("  原始负责人: " + delegatedTask.getOwner());

            // 完成委派任务
            taskService.resolveTask(task.getId());
            System.out.println("委派任务已解决");
        }
    }

    /**
     * 流程任务转办操作
     */
    @Test
    public void transferTask() {
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            String originalAssignee = task.getAssignee();
            System.out.println("原任务负责人: " + (originalAssignee != null ? originalAssignee : "未分配"));

            // 转办任务给其他人
            String transferTo = "newHandler";
            taskService.setAssignee(task.getId(), transferTo);
            System.out.println("任务已转办给: " + transferTo);

            // 查询转办后的任务
            Task transferredTask = taskService.createTaskQuery()
                    .taskId(task.getId())
                    .singleResult();

            System.out.println("转办后任务信息:");
            System.out.println("  任务ID: " + transferredTask.getId());
            System.out.println("  任务名称: " + transferredTask.getName());
            System.out.println("  当前处理人: " + transferredTask.getAssignee());
        }
    }

    /**
     * 流程任务设置候选人
     */
    @Test
    public void setTaskCandidates() {
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            // 设置候选用户
            List<String> candidateUsers = Arrays.asList("user1", "user2", "user3");
            for (String user : candidateUsers) {
                taskService.addCandidateUser(task.getId(), user);
            }
            System.out.println("设置候选用户: " + candidateUsers);

            // 设置候选组
            List<String> candidateGroups = Arrays.asList("group1", "group2");
            for (String group : candidateGroups) {
                taskService.addCandidateGroup(task.getId(), group);
            }
            System.out.println("设置候选组: " + candidateGroups);

            // 获取候选用户和组
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
            // 查询候选人信息
            List<String> users = identityLinks.stream()
                    .filter(link -> IdentityLinkType.CANDIDATE.equals(link.getType()) && link.getUserId() != null)
                    .map(IdentityLink::getUserId)
                    .collect(Collectors.toList());

            List<String> groups = identityLinks.stream()
                    .filter(link -> IdentityLinkType.CANDIDATE.equals(link.getType()) && link.getGroupId() != null)
                    .map(IdentityLink::getGroupId)
                    .collect(Collectors.toList());

            System.out.println("任务候选人信息:");
            System.out.println("  候选用户: " + users);
            System.out.println("  候选组: " + groups);
        }
    }

    /**
     * 流程实例状态查询
     */
    @Test
    public void queryProcessInstanceStatus() {
        // 启动多个流程实例用于测试
        List<String> processInstanceIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProcessInstance instance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            processInstanceIds.add(instance.getId());
            System.out.println("启动流程实例 " + (i + 1) + ": " + instance.getId());
        }

        // 挂起其中几个流程实例
        runtimeService.suspendProcessInstanceById(processInstanceIds.get(0));
        runtimeService.suspendProcessInstanceById(processInstanceIds.get(1));
        System.out.println("挂起流程实例: " + processInstanceIds.get(0) + ", " + processInstanceIds.get(1));

        // 完成其中几个流程实例的任务
        Task task1 = taskService.createTaskQuery().processInstanceId(processInstanceIds.get(2)).singleResult();
        Task task2 = taskService.createTaskQuery().processInstanceId(processInstanceIds.get(3)).singleResult();
        if (task1 != null) taskService.complete(task1.getId());
        if (task2 != null) taskService.complete(task2.getId());
        System.out.println("完成流程实例任务: " + processInstanceIds.get(2) + ", " + processInstanceIds.get(3));

        // 查询所有流程实例状态
        System.out.println("\n========== 流程实例状态统计 ==========");
        long totalCount = runtimeService.createProcessInstanceQuery().count();
        long activeCount = runtimeService.createProcessInstanceQuery().active().count();
        long suspendedCount = runtimeService.createProcessInstanceQuery().suspended().count();

        System.out.println("总流程实例数: " + totalCount);
        System.out.println("活跃流程实例数: " + activeCount);
        System.out.println("挂起流程实例数: " + suspendedCount);

        // 按状态分组查询
        System.out.println("\n活跃的流程实例:");
        List<ProcessInstance> activeInstances = runtimeService.createProcessInstanceQuery()
                .active()
                .list();
        for (ProcessInstance instance : activeInstances) {
            System.out.println("  - " + instance.getId());
        }

        System.out.println("\n挂起的流程实例:");
        List<ProcessInstance> suspendedInstances = runtimeService.createProcessInstanceQuery()
                .suspended()
                .list();
        for (ProcessInstance instance : suspendedInstances) {
            System.out.println("  - " + instance.getId());
        }
    }

    /**
     * 流程变量历史查询
     */
    @Test
    public void queryVariableHistory() {
        // 启动流程实例并设置变量
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();
        System.out.println("启动流程实例: " + processInstanceId);

        // 设置和更新变量
        runtimeService.setVariable(processInstanceId, "status", "pending");
        runtimeService.setVariable(processInstanceId, "status", "approved"); // 更新变量
        runtimeService.setVariable(processInstanceId, "approver", "admin");

        // 完成任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (task != null) {
            taskService.complete(task.getId());
        }

        // 查询变量历史（按变量名排序）
        List<HistoricVariableInstance> variableHistories = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByVariableName()
                .asc()
                .list();

        // 查询变量历史（如需按时间排序，可获取结果后手动排序）
//        List<HistoricVariableInstance> variableHistories = processEngine.getHistoryService()
//                .createHistoricVariableInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .list();
//
//        variableHistories.sort(Comparator.comparing(HistoricVariableInstance::getCreateTime));

        System.out.println("\n========== 流程变量历史记录 ==========");
        for (HistoricVariableInstance variable : variableHistories) {
            System.out.println("变量名: " + variable.getVariableName());
            System.out.println("变量值: " + variable.getValue());
            System.out.println("创建时间: " + variable.getCreateTime());
            System.out.println("最后更新时间: " + variable.getLastUpdatedTime());
            System.out.println("---");
        }
    }

    /**
     * 流程执行操作 - 包含异常处理
     */
    @Test
    public void processExecutionWithErrorHandling() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 安全地完成任务
            safeCompleteTask(processInstanceId);

            // 检查流程状态
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (instance == null) {
                System.out.println("流程实例已完成: " + processInstanceId);
            } else {
                System.out.println("流程实例状态: " + (instance.isEnded() ? "已结束" : "进行中"));
            }

        } catch (Exception e) {
            System.err.println("流程执行过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 安全地完成任务（包含异常处理）
     */
    private void safeCompleteTask(String processInstanceId) {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            if (tasks.isEmpty()) {
                System.out.println("当前没有待处理任务");
                return;
            }

            for (Task task : tasks) {
                try {
                    System.out.println("正在完成任务: " + task.getName());
                    taskService.complete(task.getId());
                    System.out.println("任务完成成功: " + task.getName());
                } catch (Exception e) {
                    System.err.println("完成任务失败 [" + task.getName() + "]: " + e.getMessage());
                    // 可以选择继续处理其他任务或抛出异常
                }
            }
        } catch (Exception e) {
            System.err.println("查询任务失败: " + e.getMessage());
        }
    }

    /**
     * 流程实例迁移操作（演示）
     */
    @Test
    public void migrateProcessInstance() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey("simpleProcess-V1")
                    .latestVersion()
                    .singleResult();

            if (processDefinition != null) {
                System.out.println("当前流程定义ID: " + processDefinition.getId());
                System.out.println("流程定义版本: " + processDefinition.getVersion());

                // 流程实例迁移通常需要相同流程定义的不同版本
                // 这里仅演示查询，实际迁移需要更复杂的配置
                System.out.println("流程实例迁移操作需要相同流程定义的不同版本支持");
            }

        } catch (Exception e) {
            System.out.println("流程实例迁移演示: " + e.getMessage());
        }
    }
}
