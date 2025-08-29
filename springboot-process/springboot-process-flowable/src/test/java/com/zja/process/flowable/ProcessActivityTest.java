package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程环节 - 完善版
 * 包含各种常见的流程环节信息获取方法
 *
 * @Author: zhengja
 * @Date: 2025-08-13 17:58
 */
public class ProcessActivityTest extends ProcessFlowableApplicationTests {

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
    public void getDetailedProcessActivityInfo() {
        // 启动一个新的流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();

        System.out.println("启动流程实例: " + processInstanceId);

        // 获取详细的流程活动信息
        getDetailedActivityAnalysis(processInstanceId);
    }

    private void getDetailedActivityAnalysis(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                System.out.println("流程实例不存在");
                return;
            }

            String processDefinitionId = processInstance.getProcessDefinitionId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

            System.out.println("\n========== 详细流程活动分析 ==========");
            System.out.println("流程实例ID: " + processInstanceId);
            System.out.println("流程定义ID: " + processDefinitionId);
            System.out.println("流程状态: " + (processInstance.isEnded() ? "已完成" : "进行中"));

            // 获取所有活动节点
            List<FlowElement> allActivities = new ArrayList<>();
            collectAllActivities(bpmnModel.getMainProcess(), allActivities);

            // 获取历史活动
            List<HistoricActivityInstance> historicActivities = processEngine.getHistoryService()
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            // 获取当前活跃活动
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

            // 创建活动执行状态映射
            System.out.println("\n----- 活动执行状态详情 -----");
            System.out.printf("%-30s %-20s %-15s %-15s%n", "活动ID", "活动名称", "活动类型", "执行状态");
            System.out.println("------------------------------------------------------------------------");

            for (FlowElement element : allActivities) {
                if (element instanceof Activity) {
                    Activity activity = (Activity) element;
                    String status = getActivityStatus(activity.getId(), historicActivities, activeActivityIds);

                    System.out.printf("%-30s %-20s %-15s %-15s%n",
                            activity.getId(),
                            activity.getName() != null ? activity.getName() : "N/A",
                            activity.getClass().getSimpleName(),
                            status);
                }
            }

            // 统计信息
            long executedCount = historicActivities.stream()
                    .filter(activity -> !(activity.getActivityType().equals("sequenceFlow")))
                    .count();

            System.out.println("\n----- 统计信息 -----");
            System.out.println("总活动数: " + allActivities.size());
            System.out.println("已执行活动数: " + executedCount);
            System.out.println("当前活跃活动数: " + activeActivityIds.size());
            System.out.println("未执行活动数: " + (allActivities.size() - executedCount - activeActivityIds.size()));

        } catch (Exception e) {
            System.err.println("分析流程活动信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 获取活动执行状态
    private String getActivityStatus(String activityId, List<HistoricActivityInstance> historicActivities,
                                     List<String> activeActivityIds) {
        // 检查是否为当前活跃活动
        if (activeActivityIds.contains(activityId)) {
            return "进行中";
        }

        // 检查是否已执行
        for (HistoricActivityInstance historicActivity : historicActivities) {
            if (activityId.equals(historicActivity.getActivityId())) {
                return "已完成";
            }
        }

        // 未执行
        return "未执行";
    }

    // 递归获取所有活动节点
    private void collectAllActivities(org.flowable.bpmn.model.FlowElementsContainer flowElementsContainer,
                                      List<FlowElement> allActivities) {
        for (FlowElement element : flowElementsContainer.getFlowElements()) {
            allActivities.add(element);
            if (element instanceof org.flowable.bpmn.model.FlowElementsContainer) {
                // 递归处理子流程
                collectAllActivities((org.flowable.bpmn.model.FlowElementsContainer) element, allActivities);
            }
        }
    }

    /**
     * 获取流程定义中的所有活动环节
     */
    @Test
    public void getAllProcessActivities() {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                System.out.println("未找到流程定义: " + processDefinitionKey);
                return;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

            System.out.println("\n========== 流程定义中的所有活动环节 ==========");
            System.out.println("流程定义ID: " + processDefinition.getId());
            System.out.println("流程定义名称: " + processDefinition.getName());

            List<Activity> activities = getAllActivitiesFromModel(bpmnModel);

            System.out.println("\n----- 活动环节列表 -----");
            System.out.printf("%-30s %-25s %-20s %-15s%n", "活动ID", "活动名称", "活动类型", "文档说明");
            System.out.println("--------------------------------------------------------------------------------------------");

            for (Activity activity : activities) {
                System.out.printf("%-30s %-25s %-20s %-15s%n",
                        activity.getId(),
                        activity.getName() != null ? activity.getName() : "N/A",
                        activity.getClass().getSimpleName(),
                        activity.getDocumentation() != null ? activity.getDocumentation() : "N/A");
            }

            System.out.println("\n总活动数: " + activities.size());

        } catch (Exception e) {
            System.err.println("获取流程活动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从BPMN模型中获取所有活动
     */
    private List<Activity> getAllActivitiesFromModel(BpmnModel bpmnModel) {
        List<Activity> activities = new ArrayList<>();

        if (bpmnModel != null) {
            for (Process process : bpmnModel.getProcesses()) {
                collectActivitiesFromProcess(process, activities);
            }
        }

        return activities;
    }

    /**
     * 从流程中递归收集活动
     */
    private void collectActivitiesFromProcess(FlowElementsContainer container, List<Activity> activities) {
        for (FlowElement element : container.getFlowElements()) {
            if (element instanceof Activity) {
                activities.add((Activity) element);
            }

            // 递归处理子流程
            if (element instanceof FlowElementsContainer) {
                collectActivitiesFromProcess((FlowElementsContainer) element, activities);
            }
        }
    }

    /**
     * 获取当前流程实例的活跃环节
     */
    @Test
    public void getActiveActivities() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            // 获取当前活跃活动
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstance.getId());

            System.out.println("\n========== 当前活跃环节 ==========");
            System.out.println("流程实例ID: " + processInstance.getId());

            if (activeActivityIds.isEmpty()) {
                System.out.println("当前无活跃环节");
            } else {
                System.out.println("活跃环节ID列表:");
                for (String activityId : activeActivityIds) {
                    System.out.println("  - " + activityId);
                }

                // 获取活跃环节详细信息
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
                System.out.println("\n----- 活跃环节详细信息 -----");
                for (String activityId : activeActivityIds) {
                    FlowElement element = bpmnModel.getMainProcess().getFlowElement(activityId);
                    if (element instanceof Activity) {
                        Activity activity = (Activity) element;
                        System.out.println("环节ID: " + activity.getId());
                        System.out.println("环节名称: " + (activity.getName() != null ? activity.getName() : "N/A"));
                        System.out.println("环节类型: " + activity.getClass().getSimpleName());
                        System.out.println("---");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("获取活跃环节失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取流程实例的历史执行环节
     */
    @Test
    public void getHistoricActivities() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            // 获取历史活动实例
            List<HistoricActivityInstance> historicActivities = processEngine.getHistoryService()
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list();

            System.out.println("\n========== 历史执行环节 ==========");
            System.out.println("流程实例ID: " + processInstance.getId());

            if (historicActivities.isEmpty()) {
                System.out.println("暂无历史执行环节");
            } else {
                System.out.printf("%-5s %-30s %-25s %-20s %-20s %-20s %-15s%n",
                        "序号", "环节ID", "环节名称", "环节类型", "开始时间", "结束时间", "持续时间");
                System.out.println("------------------------------------------------------------------------------------------------------------------------");

                int index = 1;
                for (HistoricActivityInstance activity : historicActivities) {
                    if (!"sequenceFlow".equals(activity.getActivityType())) { // 过滤掉连线
                        String duration = activity.getDurationInMillis() != null ?
                                formatDuration(activity.getDurationInMillis()) : "N/A";

                        System.out.printf("%-5d %-30s %-25s %-20s %-20s %-20s %-15s%n",
                                index++,
                                activity.getActivityId(),
                                activity.getActivityName() != null ? activity.getActivityName() : "N/A",
                                activity.getActivityType(),
                                formatDate(activity.getStartTime()),
                                activity.getEndTime() != null ? formatDate(activity.getEndTime()) : "N/A",
                                duration);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("获取历史执行环节失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取流程实例的待办任务环节
     */
    @Test
    public void getPendingTaskActivities() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            // 获取当前待办任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .list();

            System.out.println("\n========== 待办任务环节 ==========");
            System.out.println("流程实例ID: " + processInstance.getId());

            if (tasks.isEmpty()) {
                System.out.println("暂无待办任务");
            } else {
                System.out.printf("%-36s %-25s %-20s %-20s %-15s%n",
                        "任务ID", "任务名称", "任务Key", "负责人", "创建时间");
                System.out.println("------------------------------------------------------------------------------------------------------------------------");

                for (Task task : tasks) {
                    System.out.printf("%-36s %-25s %-20s %-20s %-15s%n",
                            task.getId(),
                            task.getName(),
                            task.getTaskDefinitionKey(),
                            task.getAssignee() != null ? task.getAssignee() : "未分配",
                            formatDate(task.getCreateTime()));
                }
            }

        } catch (Exception e) {
            System.err.println("获取待办任务环节失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取流程环节的详细属性信息
     */
    @Test
    public void getActivityProperties() {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                System.out.println("未找到流程定义: " + processDefinitionKey);
                return;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            List<Activity> activities = getAllActivitiesFromModel(bpmnModel);

            System.out.println("\n========== 流程环节详细属性信息 ==========");
            System.out.println("流程定义ID: " + processDefinition.getId());
            System.out.println("流程定义名称: " + processDefinition.getName());

            for (Activity activity : activities) {
                System.out.println("\n----- 环节: " + (activity.getName() != null ? activity.getName() : activity.getId()) + " -----");
                System.out.println("环节ID: " + activity.getId());
                System.out.println("环节类型: " + activity.getClass().getSimpleName());
                System.out.println("环节名称: " + (activity.getName() != null ? activity.getName() : "N/A"));
                System.out.println("文档说明: " + (activity.getDocumentation() != null ? activity.getDocumentation() : "N/A"));
                System.out.println("默认流程线: " + (activity.getDefaultFlow() != null ? activity.getDefaultFlow() : "N/A"));
                System.out.println("异步执行: " + activity.isAsynchronous());
                System.out.println("排除异步执行: " + activity.isExclusive());

                // 特定类型活动的属性
                if (activity instanceof UserTask) {
                    UserTask userTask = (UserTask) activity;
                    System.out.println("用户任务 - 负责人: " + (userTask.getAssignee() != null ? userTask.getAssignee() : "N/A"));
                    System.out.println("用户任务 - 候选用户: " + userTask.getCandidateUsers());
                    System.out.println("用户任务 - 候选组: " + userTask.getCandidateGroups());
                } else if (activity instanceof ServiceTask) {
                    ServiceTask serviceTask = (ServiceTask) activity;
                    System.out.println("服务任务 - 类名: " + (serviceTask.getImplementation() != null ? serviceTask.getImplementation() : "N/A"));
                }
            }

        } catch (Exception e) {
            System.err.println("获取环节属性信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取流程环节的执行顺序
     */
    @Test
    public void getActivityExecutionOrder() {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            // 获取历史活动实例（按执行顺序排序）
            List<HistoricActivityInstance> historicActivities = processEngine.getHistoryService()
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list();

            System.out.println("\n========== 流程环节执行顺序 ==========");
            System.out.println("流程实例ID: " + processInstance.getId());

            if (historicActivities.isEmpty()) {
                System.out.println("暂无执行记录");
            } else {
                System.out.println("\n执行顺序详情:");
                int order = 1;
                for (HistoricActivityInstance activity : historicActivities) {
                    if (!"sequenceFlow".equals(activity.getActivityType())) { // 过滤掉连线
                        System.out.println(order + ". [" + activity.getActivityType() + "] " +
                                (activity.getActivityName() != null ? activity.getActivityName() : activity.getActivityId()));
                        System.out.println("   开始时间: " + formatDate(activity.getStartTime()));
                        System.out.println("   结束时间: " + (activity.getEndTime() != null ? formatDate(activity.getEndTime()) : "N/A"));
                        if (activity.getDurationInMillis() != null) {
                            System.out.println("   执行时长: " + formatDuration(activity.getDurationInMillis()));
                        }
                        System.out.println();
                        order++;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("获取执行顺序失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取流程环节的流转关系
     */
    @Test
    public void getActivityTransitions() {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                System.out.println("未找到流程定义: " + processDefinitionKey);
                return;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

            System.out.println("\n========== 流程环节流转关系 ==========");
            System.out.println("流程定义ID: " + processDefinition.getId());
            System.out.println("流程定义名称: " + processDefinition.getName());

            // 获取所有流程线
            List<SequenceFlow> sequenceFlows = new ArrayList<>();
            collectSequenceFlows(bpmnModel.getMainProcess(), sequenceFlows);

            System.out.println("\n----- 流转关系 -----");
            for (SequenceFlow flow : sequenceFlows) {
                System.out.println("从 [" + (flow.getSourceFlowElement() != null ?
                        flow.getSourceFlowElement().getName() != null ? flow.getSourceFlowElement().getName() :
                        flow.getSourceFlowElement().getId() : "N/A") +
                        "] 到 [" + (flow.getTargetFlowElement() != null ?
                        flow.getTargetFlowElement().getName() != null ? flow.getTargetFlowElement().getName() :
                        flow.getTargetFlowElement().getId() : "N/A") + "]");
                System.out.println("  流程线ID: " + flow.getId());
                System.out.println("  条件表达式: " + (flow.getConditionExpression() != null ? flow.getConditionExpression() : "N/A"));
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("获取流转关系失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 递归收集流程线
     */
    private void collectSequenceFlows(FlowElementsContainer container, List<SequenceFlow> sequenceFlows) {
        for (FlowElement element : container.getFlowElements()) {
            if (element instanceof SequenceFlow) {
                sequenceFlows.add((SequenceFlow) element);
            }

            // 递归处理子流程
            if (element instanceof FlowElementsContainer) {
                collectSequenceFlows((FlowElementsContainer) element, sequenceFlows);
            }
        }
    }

    /**
     * 获取特定类型的流程环节
     */
    @Test
    public void getSpecificTypeActivities() {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .singleResult();

            if (processDefinition == null) {
                System.out.println("未找到流程定义: " + processDefinitionKey);
                return;
            }

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            List<Activity> activities = getAllActivitiesFromModel(bpmnModel);

            // 按类型分组
            Map<String, List<Activity>> activitiesByType = activities.stream()
                    .collect(Collectors.groupingBy(activity -> activity.getClass().getSimpleName()));

            System.out.println("\n========== 按类型分组的流程环节 ==========");
            System.out.println("流程定义ID: " + processDefinition.getId());

            for (Map.Entry<String, List<Activity>> entry : activitiesByType.entrySet()) {
                System.out.println("\n----- " + entry.getKey() + " (" + entry.getValue().size() + "个) -----");
                for (Activity activity : entry.getValue()) {
                    System.out.println("  - " + (activity.getName() != null ? activity.getName() : activity.getId()) +
                            " (ID: " + activity.getId() + ")");
                }
            }

        } catch (Exception e) {
            System.err.println("获取特定类型环节失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 格式化日期显示
     */
    private String formatDate(Date date) {
        if (date == null) return "N/A";
        return String.format("%tF %tT", date, date);
    }

    /**
     * 格式化持续时间显示
     */
    private String formatDuration(Long durationInMillis) {
        if (durationInMillis == null) return "N/A";

        long seconds = durationInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 24;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("天 ");
        if (hours > 0) sb.append(hours).append("小时 ");
        if (minutes > 0) sb.append(minutes).append("分钟 ");
        sb.append(seconds).append("秒");

        return sb.toString();
    }
}
