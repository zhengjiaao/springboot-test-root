package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 流程日志 - 完善版
 * 包含各种常见的流程日志查询方式
 *
 * @Author: zhengja
 * @Date: 2025-08-13 16:42
 */
public class ProcessInstanceLogTest extends ProcessFlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 流程定义的Key
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

    // 流程日志查询测试方法
    @Test
    public void getCompleteProcessLog() {
        // 启动一个新的流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
        String processInstanceId = processInstance.getId();

        System.out.println("启动流程实例: " + processInstanceId);

        // 获取完整的流程执行日志
        printCompleteProcessLog(processInstanceId);
    }

    // 打印完整的流程执行日志
    public void printCompleteProcessLog(String processInstanceId) {
        System.out.println("\n========== 完整流程执行日志 ==========");
        System.out.println("流程实例ID: " + processInstanceId);

        try {
            // 1. 获取流程实例历史
            HistoricProcessInstance historicProcessInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (historicProcessInstance != null) {
                System.out.println("\n----- 流程实例信息 -----");
                System.out.println("启动时间: " + formatDate(historicProcessInstance.getStartTime()));
                System.out.println("结束时间: " + (historicProcessInstance.getEndTime() != null ?
                        formatDate(historicProcessInstance.getEndTime()) : "进行中"));
                System.out.println("持续时间: " + (historicProcessInstance.getDurationInMillis() != null ?
                        formatDuration(historicProcessInstance.getDurationInMillis()) : "N/A"));
                System.out.println("状态: " + (historicProcessInstance.getEndTime() != null ? "已完成" : "进行中"));
                System.out.println("删除原因: " + (historicProcessInstance.getDeleteReason() != null ?
                        historicProcessInstance.getDeleteReason() : "N/A"));
            }

            // 2. 获取活动历史（按时间顺序）
            List<HistoricActivityInstance> activities = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list();

            System.out.println("\n----- 活动执行顺序 -----");
            for (int i = 0; i < activities.size(); i++) {
                HistoricActivityInstance activity = activities.get(i);
                System.out.println((i + 1) + ". [" + activity.getActivityType() + "] " +
                        (activity.getActivityName() != null ? activity.getActivityName() : "N/A") +
                        " (" + activity.getActivityId() + ")");
                System.out.println("   开始时间: " + formatDate(activity.getStartTime()));
                System.out.println("   结束时间: " + (activity.getEndTime() != null ?
                        formatDate(activity.getEndTime()) : "N/A"));
                if (activity.getDurationInMillis() != null) {
                    System.out.println("   持续时间: " + formatDuration(activity.getDurationInMillis()));
                }
                System.out.println();
            }

            // 3. 获取任务历史
            List<HistoricTaskInstance> tasks = historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricTaskInstanceStartTime()
                    .asc()
                    .list();

            if (!tasks.isEmpty()) {
                System.out.println("----- 任务执行记录 -----");
                for (int i = 0; i < tasks.size(); i++) {
                    HistoricTaskInstance task = tasks.get(i);
                    System.out.println((i + 1) + ". 任务: " + task.getName());
                    System.out.println("   任务ID: " + task.getId());
                    System.out.println("   负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));
                    System.out.println("   开始时间: " + formatDate(task.getStartTime()));
                    System.out.println("   结束时间: " + (task.getEndTime() != null ?
                            formatDate(task.getEndTime()) : "未完成"));
                    if (task.getDurationInMillis() != null) {
                        System.out.println("   处理时间: " + formatDuration(task.getDurationInMillis()));
                    }
                    System.out.println("   删除原因: " + (task.getDeleteReason() != null ?
                            task.getDeleteReason() : "N/A"));
                    System.out.println();
                }
            }

            // 4. 获取变量历史
            List<HistoricVariableInstance> variables = historyService
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            if (!variables.isEmpty()) {
                System.out.println("----- 流程变量 -----");
                for (HistoricVariableInstance variable : variables) {
                    System.out.println("变量名: " + variable.getVariableName());
                    System.out.println("变量值: " + variable.getValue());
                    System.out.println("变量类型: " + variable.getVariableTypeName());
                    System.out.println("创建时间: " + formatDate(variable.getCreateTime()));
                    System.out.println("最后更新: " + (variable.getLastUpdatedTime() != null ?
                            formatDate(variable.getLastUpdatedTime()) : "N/A"));
                    System.out.println();
                }
            }

            // 5. 获取身份链接历史（参与者信息）
            List<HistoricIdentityLink> identityLinks = historyService
                    .getHistoricIdentityLinksForProcessInstance(processInstanceId);

            if (!identityLinks.isEmpty()) {
                System.out.println("----- 流程参与者 -----");
                for (HistoricIdentityLink identityLink : identityLinks) {
                    System.out.println("类型: " + identityLink.getType());
                    System.out.println("用户ID: " + (identityLink.getUserId() != null ?
                            identityLink.getUserId() : "N/A"));
                    System.out.println("组ID: " + (identityLink.getGroupId() != null ?
                            identityLink.getGroupId() : "N/A"));
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("获取流程日志失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 获取流程流转日志
    @Test
    public void getProcessInstanceLog() {
        String processInstanceId = "a96c9919-7818-11f0-8a53-8245ddc034b5";

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            System.out.println("未找到流程实例: " + processInstanceId);
            // 启动一个新的流程实例用于测试
            processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            processInstanceId = processInstance.getId();
            System.out.println("已启动新的流程实例: " + processInstanceId);
        }

        System.out.println("========== 流程实例基本信息 ==========");
        System.out.println("流程实例ID: " + processInstance.getId());
        System.out.println("流程实例名称: " + processInstance.getName());
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
        System.out.println("业务Key: " + processInstance.getBusinessKey());
        System.out.println("是否结束: " + processInstance.isEnded());
        System.out.println("是否暂停: " + processInstance.isSuspended());

        // 获取流程历史记录
        getHistoricActivityLog(processInstanceId);

        // 获取流程变量历史
        getHistoricVariableLog(processInstanceId);

        // 获取任务历史
        getHistoricTaskLog(processInstanceId);

        // 获取流程实例详细信息
        getDetailedProcessInstanceLog(processInstanceId);
    }

    // 获取历史活动日志
    private void getHistoricActivityLog(String processInstanceId) {
        try {
            List<HistoricActivityInstance> activityInstances = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list();

            System.out.println("\n========== 流程活动历史记录 ==========");
            if (activityInstances.isEmpty()) {
                System.out.println("暂无活动历史记录");
            } else {
                System.out.printf("%-30s %-20s %-20s %-20s %-15s%n", "活动ID", "活动名称", "活动类型", "开始时间", "持续时间");
                System.out.println("------------------------------------------------------------------------------------------------------------------------");

                for (HistoricActivityInstance activity : activityInstances) {
                    String duration = activity.getDurationInMillis() != null ?
                            formatDuration(activity.getDurationInMillis()) : "N/A";
                    System.out.printf("%-30s %-20s %-20s %-20s %-15s%n",
                            activity.getActivityId(),
                            activity.getActivityName() != null ? activity.getActivityName() : "N/A",
                            activity.getActivityType(),
                            formatDate(activity.getStartTime()),
                            duration);
                }
            }
        } catch (Exception e) {
            System.err.println("获取活动历史记录失败: " + e.getMessage());
        }
    }

    // 获取历史变量日志
    private void getHistoricVariableLog(String processInstanceId) {
        try {
            List<HistoricVariableInstance> variableInstances = historyService
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            System.out.println("\n========== 流程变量历史记录 ==========");
            if (variableInstances.isEmpty()) {
                System.out.println("暂无变量历史记录");
            } else {
                System.out.printf("%-20s %-20s %-20s %-20s%n", "变量名", "变量值", "类型", "创建时间");
                System.out.println("---------------------------------------------------------------------------");

                for (HistoricVariableInstance variable : variableInstances) {
                    System.out.printf("%-20s %-20s %-20s %-20s%n",
                            variable.getVariableName(),
                            variable.getValue() != null ? variable.getValue().toString() : "null",
                            variable.getVariableTypeName(),
                            formatDate(variable.getCreateTime()));
                }
            }
        } catch (Exception e) {
            System.err.println("获取变量历史记录失败: " + e.getMessage());
        }
    }

    // 获取历史任务日志
    private void getHistoricTaskLog(String processInstanceId) {
        try {
            List<HistoricTaskInstance> taskInstances = historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricTaskInstanceStartTime()
                    .asc()
                    .list();

            System.out.println("\n========== 任务历史记录 ==========");
            if (taskInstances.isEmpty()) {
                System.out.println("暂无任务历史记录");
            } else {
                System.out.printf("%-30s %-20s %-15s %-20s %-20s %-15s%n", "任务ID", "任务名称", "负责人", "开始时间", "结束时间", "持续时间");
                System.out.println("------------------------------------------------------------------------------------------------------------------------");

                for (HistoricTaskInstance task : taskInstances) {
                    String duration = task.getDurationInMillis() != null ?
                            formatDuration(task.getDurationInMillis()) : "N/A";
                    System.out.printf("%-30s %-20s %-15s %-20s %-20s %-15s%n",
                            task.getId(),
                            task.getName(),
                            task.getAssignee() != null ? task.getAssignee() : "N/A",
                            formatDate(task.getStartTime()),
                            task.getEndTime() != null ? formatDate(task.getEndTime()) : "未完成",
                            duration);
                }
            }
        } catch (Exception e) {
            System.err.println("获取任务历史记录失败: " + e.getMessage());
        }
    }

    // 获取流程实例详细信息
    private void getDetailedProcessInstanceLog(String processInstanceId) {
        try {
            HistoricProcessInstance processInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance != null) {
                System.out.println("\n========== 流程实例详细信息 ==========");
                System.out.println("流程实例ID: " + processInstance.getId());
                System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
                System.out.println("流程定义Key: " + processInstance.getProcessDefinitionKey());
                System.out.println("流程定义名称: " + processInstance.getProcessDefinitionName());
                System.out.println("业务Key: " + processInstance.getBusinessKey());
                System.out.println("启动用户: " + processInstance.getStartUserId());
                System.out.println("启动时间: " + formatDate(processInstance.getStartTime()));
                System.out.println("结束时间: " + (processInstance.getEndTime() != null ?
                        formatDate(processInstance.getEndTime()) : "N/A"));
                System.out.println("持续时间: " + (processInstance.getDurationInMillis() != null ?
                        formatDuration(processInstance.getDurationInMillis()) : "N/A"));
                System.out.println("删除原因: " + (processInstance.getDeleteReason() != null ?
                        processInstance.getDeleteReason() : "N/A"));
                System.out.println("超时时间: " + (processInstance.getEndTime() != null ?
                        formatDate(processInstance.getEndTime()) : "N/A"));
            }
        } catch (Exception e) {
            System.err.println("获取流程实例详细信息失败: " + e.getMessage());
        }
    }

    // 查询特定时间范围内的流程实例
    @Test
    public void queryProcessInstancesByTimeRange() {
        try {
            // 查询最近24小时启动的流程实例
            Date now = new Date();
            Date yesterday = new Date(now.getTime() - 24 * 60 * 60 * 1000);

            List<HistoricProcessInstance> processInstances = historyService
                    .createHistoricProcessInstanceQuery()
                    .startedAfter(yesterday)
                    .startedBefore(now)
                    .orderByProcessInstanceStartTime()
                    .desc()
                    .list();

            System.out.println("\n========== 最近24小时启动的流程实例 ==========");
            if (processInstances.isEmpty()) {
                System.out.println("暂无流程实例");
            } else {
                System.out.printf("%-36s %-30s %-20s %-20s%n", "流程实例ID", "流程定义Key", "启动时间", "状态");
                System.out.println("--------------------------------------------------------------------------------------------");

                for (HistoricProcessInstance instance : processInstances) {
                    String status = instance.getEndTime() != null ? "已完成" : "进行中";
                    System.out.printf("%-36s %-30s %-20s %-20s%n",
                            instance.getId(),
                            instance.getProcessDefinitionKey(),
                            formatDate(instance.getStartTime()),
                            status);
                }
            }
        } catch (Exception e) {
            System.err.println("查询时间范围内的流程实例失败: " + e.getMessage());
        }
    }

    // 查询特定用户的流程实例
    @Test
    public void queryProcessInstancesByUser() {
        try {
            // 假设查询特定用户启动的流程实例
            String userId = "admin"; // 实际使用时替换为真实的用户ID

            List<HistoricProcessInstance> processInstances = historyService
                    .createHistoricProcessInstanceQuery()
                    .startedBy(userId)
                    .orderByProcessInstanceStartTime()
                    .desc()
                    .list();

            System.out.println("\n========== 用户 " + userId + " 启动的流程实例 ==========");
            if (processInstances.isEmpty()) {
                System.out.println("暂无流程实例");
            } else {
                System.out.printf("%-36s %-30s %-20s %-20s%n", "流程实例ID", "流程定义Key", "启动时间", "状态");
                System.out.println("--------------------------------------------------------------------------------------------");

                for (HistoricProcessInstance instance : processInstances) {
                    String status = instance.getEndTime() != null ? "已完成" : "进行中";
                    System.out.printf("%-36s %-30s %-20s %-20s%n",
                            instance.getId(),
                            instance.getProcessDefinitionKey(),
                            formatDate(instance.getStartTime()),
                            status);
                }
            }
        } catch (Exception e) {
            System.err.println("查询用户流程实例失败: " + e.getMessage());
        }
    }

    // 查询未完成的流程实例
    @Test
    public void queryUnfinishedProcessInstances() {
        try {
            List<HistoricProcessInstance> processInstances = historyService
                    .createHistoricProcessInstanceQuery()
                    .unfinished()
                    .orderByProcessInstanceStartTime()
                    .desc()
                    .list();

            System.out.println("\n========== 未完成的流程实例 ==========");
            if (processInstances.isEmpty()) {
                System.out.println("暂无未完成的流程实例");
            } else {
                System.out.printf("%-36s %-30s %-20s%n", "流程实例ID", "流程定义Key", "启动时间");
                System.out.println("--------------------------------------------------------------------------------");

                for (HistoricProcessInstance instance : processInstances) {
                    System.out.printf("%-36s %-30s %-20s%n",
                            instance.getId(),
                            instance.getProcessDefinitionKey(),
                            formatDate(instance.getStartTime()));
                }
            }
        } catch (Exception e) {
            System.err.println("查询未完成流程实例失败: " + e.getMessage());
        }
    }

    // 查询已完成的流程实例
    @Test
    public void queryFinishedProcessInstances() {
        try {
            List<HistoricProcessInstance> processInstances = historyService
                    .createHistoricProcessInstanceQuery()
                    .finished()
                    .orderByProcessInstanceEndTime()
                    .desc()
                    .list();

            System.out.println("\n========== 已完成的流程实例 ==========");
            if (processInstances.isEmpty()) {
                System.out.println("暂无已完成的流程实例");
            } else {
                System.out.printf("%-36s %-30s %-20s %-20s %-15s%n", "流程实例ID", "流程定义Key", "启动时间", "结束时间", "持续时间");
                System.out.println("----------------------------------------------------------------------------------------------------------------");

                for (HistoricProcessInstance instance : processInstances) {
                    System.out.printf("%-36s %-30s %-20s %-20s %-15s%n",
                            instance.getId(),
                            instance.getProcessDefinitionKey(),
                            formatDate(instance.getStartTime()),
                            formatDate(instance.getEndTime()),
                            instance.getDurationInMillis() != null ?
                                    formatDuration(instance.getDurationInMillis()) : "N/A");
                }
            }
        } catch (Exception e) {
            System.err.println("查询已完成流程实例失败: " + e.getMessage());
        }
    }

    // 查询特定流程定义的流程实例
    @Test
    public void queryProcessInstancesByDefinition() {
        try {
            List<HistoricProcessInstance> processInstances = historyService
                    .createHistoricProcessInstanceQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .orderByProcessInstanceStartTime()
                    .desc()
                    .list();

            System.out.println("\n========== 流程定义 " + processDefinitionKey + " 的流程实例 ==========");
            if (processInstances.isEmpty()) {
                System.out.println("暂无流程实例");
            } else {
                System.out.printf("%-36s %-20s %-20s %-15s%n", "流程实例ID", "启动时间", "结束时间", "状态");
                System.out.println("------------------------------------------------------------------------------------------------");

                for (HistoricProcessInstance instance : processInstances) {
                    String status = instance.getEndTime() != null ? "已完成" : "进行中";
                    System.out.printf("%-36s %-20s %-20s %-15s%n",
                            instance.getId(),
                            formatDate(instance.getStartTime()),
                            instance.getEndTime() != null ? formatDate(instance.getEndTime()) : "N/A",
                            status);
                }
            }
        } catch (Exception e) {
            System.err.println("查询特定流程定义的流程实例失败: " + e.getMessage());
        }
    }

    // 查询特定任务的执行情况
    @Test
    public void queryTaskExecutionLog() {
        try {
            // 启动一个流程实例用于测试
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            // 查询该流程实例的任务历史
            List<HistoricTaskInstance> tasks = historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .orderByHistoricTaskInstanceStartTime()
                    .asc()
                    .list();

            System.out.println("\n========== 流程实例 " + processInstance.getId() + " 的任务执行日志 ==========");
            if (tasks.isEmpty()) {
                System.out.println("暂无任务记录");
            } else {
                System.out.printf("%-30s %-20s %-15s %-20s %-20s %-15s%n",
                        "任务ID", "任务名称", "负责人", "开始时间", "结束时间", "持续时间");
                System.out.println("------------------------------------------------------------------------------------------------------------------------");

                for (HistoricTaskInstance task : tasks) {
                    String duration = task.getDurationInMillis() != null ?
                            formatDuration(task.getDurationInMillis()) : "N/A";
                    System.out.printf("%-30s %-20s %-15s %-20s %-20s %-15s%n",
                            task.getId(),
                            task.getName(),
                            task.getAssignee() != null ? task.getAssignee() : "N/A",
                            formatDate(task.getStartTime()),
                            task.getEndTime() != null ? formatDate(task.getEndTime()) : "未完成",
                            duration);
                }
            }
        } catch (Exception e) {
            System.err.println("查询任务执行日志失败: " + e.getMessage());
        }
    }

    // 查询流程变量变更历史
    @Test
    public void queryVariableHistory() {
        try {
            // 启动一个流程实例用于测试
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            // 查询该流程实例的变量历史
            List<HistoricVariableInstance> variables = historyService
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .list();

            System.out.println("\n========== 流程实例 " + processInstance.getId() + " 的变量历史 ==========");
            if (variables.isEmpty()) {
                System.out.println("暂无变量记录");
            } else {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s%n",
                        "变量名", "变量值", "变量类型", "创建时间", "最后更新时间");
                System.out.println("------------------------------------------------------------------------------------------------------------------------");

                for (HistoricVariableInstance variable : variables) {
                    System.out.printf("%-20s %-20s %-20s %-20s %-20s%n",
                            variable.getVariableName(),
                            variable.getValue() != null ? variable.getValue().toString() : "null",
                            variable.getVariableTypeName(),
                            formatDate(variable.getCreateTime()),
                            variable.getLastUpdatedTime() != null ?
                                    formatDate(variable.getLastUpdatedTime()) : "N/A");
                }
            }
        } catch (Exception e) {
            System.err.println("查询变量历史失败: " + e.getMessage());
        }
    }

    // 格式化日期显示
    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    // 格式化持续时间显示
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
