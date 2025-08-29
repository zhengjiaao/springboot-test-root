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
import java.util.Date;
import java.util.List;

/**
 * 自动检测并完成流程中的任务
 *
 * @Author: zhengja
 * @Date: 2025-08-14 15:06
 */
public class ProcessTaskTest extends ProcessFlowableApplicationTests {

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
     * 一次性走通整个流程到结束
     * 自动完成流程中的所有任务直到流程结束
     */
    @Test
    public void completeEntireProcessToEnd() {
        try {
            // 1. 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 2. 持续完成任务直到流程结束
            completeAllTasksUntilProcessEnds(processInstanceId);

            // 3. 验证流程是否已结束
            ProcessInstance finishedInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (finishedInstance == null) {
                System.out.println("流程实例已成功结束: " + processInstanceId);

                // 查询历史记录确认流程已完成
                HistoricProcessInstance historicInstance = processEngine.getHistoryService()
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (historicInstance != null && historicInstance.getEndTime() != null) {
                    System.out.println("流程结束时间: " + historicInstance.getEndTime());
                    System.out.println("流程持续时间: " + formatDuration(historicInstance.getDurationInMillis()));
                    System.out.println("流程已完全执行并结束");
                }
            } else {
                System.out.println("流程实例仍在运行中: " + processInstanceId);
            }

        } catch (Exception e) {
            System.err.println("执行完整流程失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 持续完成任务直到流程结束
     *
     * @param processInstanceId 流程实例ID
     */
    private void completeAllTasksUntilProcessEnds(String processInstanceId) {
        try {
            int taskCount = 0;
            System.out.println("\n开始自动完成流程任务...");

            // 循环处理任务直到流程结束
            while (true) {
                // 检查流程是否已经结束
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (processInstance == null || processInstance.isEnded()) {
                    System.out.println("流程已结束，停止任务处理");
                    break;
                }

                // 查询当前所有待办任务
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .list();

                if (tasks.isEmpty()) {
                    System.out.println("暂无待处理任务，流程可能在等待事件或其他条件");
                    break;
                }

                // 处理所有当前任务
                for (Task task : tasks) {
                    taskCount++;
                    System.out.println("正在处理任务 " + taskCount + ": " + task.getName() + " (ID: " + task.getId() + ")");

                    // 可以在这里添加任务处理逻辑，如设置变量等
                    handleTaskProcessing(task);

                    // 完成任务
                    taskService.complete(task.getId());
                    System.out.println("任务完成: " + task.getName());
                }

                // 短暂等待，避免过于频繁的查询
                Thread.sleep(100);
            }

            System.out.println("总共处理了 " + taskCount + " 个任务");

        } catch (Exception e) {
            System.err.println("自动完成任务过程中发生错误: " + e.getMessage());
            throw new RuntimeException("流程执行中断", e);
        }
    }

    /**
     * 处理任务执行逻辑（可自定义任务处理）
     *
     * @param task 当前任务
     */
    private void handleTaskProcessing(Task task) {
        try {
            // 根据任务类型或名称进行不同的处理
            System.out.println("  处理任务详情:");
            System.out.println("    任务ID: " + task.getId());
            System.out.println("    任务名称: " + task.getName());
            System.out.println("    任务描述: " + task.getDescription());
            System.out.println("    负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));
            System.out.println("    创建时间: " + task.getCreateTime());

            // 示例：为特定任务设置变量
            if (task.getName().contains("审批") || task.getName().contains("审核")) {
                // 设置审批相关变量
                taskService.setVariable(task.getId(), "approved", true);
                taskService.setVariable(task.getId(), "approver", "system");
                taskService.setVariable(task.getId(), "approvalTime", new Date());
                System.out.println("    已设置审批相关变量");
            }

            // 示例：为特定任务设置业务变量
            if (task.getName().contains("处理") || task.getName().contains("处理")) {
                taskService.setVariable(task.getId(), "processed", true);
                taskService.setVariable(task.getId(), "processor", "autoProcessor");
                System.out.println("    已设置处理相关变量");
            }

            // 可以根据实际业务需求添加更多处理逻辑

        } catch (Exception e) {
            System.err.println("处理任务时发生错误: " + e.getMessage());
        }
    }

    /**
     * 完整的流程执行演示（包含详细日志）
     */
    @Test
    public void completeEntireProcessWithDetailedLogging() {
        try {
            System.out.println("========== 完整流程执行演示 ==========");

            // 1. 启动流程实例
            long startTime = System.currentTimeMillis();
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("1. 启动流程实例成功: " + processInstanceId);
            System.out.println("   启动时间: " + new Date(startTime));
            System.out.println("   流程定义ID: " + processInstance.getProcessDefinitionId());

            // 2. 显示流程初始状态
            showProcessStatus(processInstanceId, "初始状态");

            // 3. 执行完整流程
            completeProcessWithMonitoring(processInstanceId);

            // 4. 显示最终状态
            showProcessStatus(processInstanceId, "最终状态");

            // 5. 输出执行统计
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("\n========== 执行统计 ==========");
            System.out.println("总执行时间: " + formatDuration(duration));
            System.out.println("流程实例ID: " + processInstanceId);

            // 6. 验证流程结束
            ProcessInstance finalInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (finalInstance == null) {
                System.out.println("✓ 流程成功结束");
            } else {
                System.out.println("⚠ 流程仍在运行中");
                System.out.println("  状态: " + (finalInstance.isEnded() ? "已结束" : "进行中"));
                System.out.println("  挂起: " + finalInstance.isSuspended());
            }

        } catch (Exception e) {
            System.err.println("完整流程执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 带监控的完整流程执行
     *
     * @param processInstanceId 流程实例ID
     */
    private void completeProcessWithMonitoring(String processInstanceId) {
        try {
            int totalTasks = 0;
            long processStartTime = System.currentTimeMillis();

            System.out.println("\n2. 开始执行流程任务:");

            while (true) {
                // 检查流程状态
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                // 如果流程已结束，退出循环
                if (processInstance == null || processInstance.isEnded()) {
                    System.out.println("   流程已结束，停止任务处理");
                    break;
                }

                // 获取当前任务
                List<Task> currentTasks = taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .list();

                if (currentTasks.isEmpty()) {
                    System.out.println("   暂无待处理任务，流程可能在等待外部事件");
                    // 等待一段时间后再次检查
                    Thread.sleep(500);
                    continue;
                }

                // 处理当前所有任务
                for (Task task : currentTasks) {
                    totalTasks++;
                    long taskStartTime = System.currentTimeMillis();

                    System.out.println("   处理任务 #" + totalTasks + ": " + task.getName());
                    System.out.println("     任务ID: " + task.getId());
                    System.out.println("     负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));

                    // 处理任务
                    handleTaskProcessing(task);

                    // 完成任务
                    taskService.complete(task.getId());

                    long taskDuration = System.currentTimeMillis() - taskStartTime;
                    System.out.println("     任务完成，耗时: " + formatDuration(taskDuration));
                    System.out.println("     ---");
                }

                // 短暂等待
                Thread.sleep(200);
            }

            long processDuration = System.currentTimeMillis() - processStartTime;
            System.out.println("3. 流程执行完成:");
            System.out.println("   总处理任务数: " + totalTasks);
            System.out.println("   总执行时间: " + formatDuration(processDuration));

        } catch (Exception e) {
            System.err.println("流程执行监控过程中发生错误: " + e.getMessage());
            throw new RuntimeException("流程执行中断", e);
        }
    }

    /**
     * 显示流程状态
     *
     * @param processInstanceId 流程实例ID
     * @param statusDescription 状态描述
     */
    private void showProcessStatus(String processInstanceId, String statusDescription) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                System.out.println("   " + statusDescription + ": 流程实例不存在（可能已结束）");
                return;
            }

            System.out.println("   " + statusDescription + ":");
            System.out.println("     流程状态: " + (processInstance.isEnded() ? "已结束" : "进行中"));
            System.out.println("     是否挂起: " + processInstance.isSuspended());

            // 显示当前活跃活动
            List<String> activeActivities = runtimeService.getActiveActivityIds(processInstanceId);
            if (!activeActivities.isEmpty()) {
                System.out.println("     活跃活动: " + activeActivities);
            }

            // 显示当前任务
            List<Task> currentTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            if (!currentTasks.isEmpty()) {
                System.out.println("     当前任务数量: " + currentTasks.size());
                for (Task task : currentTasks) {
                    System.out.println("       - " + task.getName() + " (负责人: " +
                            (task.getAssignee() != null ? task.getAssignee() : "未分配") + ")");
                }
            }

        } catch (Exception e) {
            System.err.println("显示流程状态时发生错误: " + e.getMessage());
        }
    }

    /**
     * 格式化持续时间显示
     *
     * @param durationInMillis 持续时间（毫秒）
     * @return 格式化后的时间字符串
     */
    private String formatDuration(long durationInMillis) {
        if (durationInMillis < 1000) {
            return durationInMillis + "ms";
        }

        long seconds = durationInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;

        if (hours > 0) {
            return String.format("%d小时%d分钟%d秒", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d分钟%d秒", minutes, seconds);
        } else {
            return String.format("%d秒", seconds);
        }
    }

    /**
     * 批量执行多个流程实例到结束
     */
    @Test
    public void completeMultipleProcessesToEnd() {
        try {
            System.out.println("========== 批量执行流程实例 ==========");

            // 启动多个流程实例
            List<String> processInstanceIds = new ArrayList<>();
            int instanceCount = 3;

            for (int i = 0; i < instanceCount; i++) {
                ProcessInstance instance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
                processInstanceIds.add(instance.getId());
                System.out.println("启动流程实例 " + (i + 1) + ": " + instance.getId());
            }

            // 并行执行所有流程
            System.out.println("\n开始并行执行所有流程...");
            long startTime = System.currentTimeMillis();

            for (String processInstanceId : processInstanceIds) {
                System.out.println("\n执行流程: " + processInstanceId);
                completeAllTasksUntilProcessEnds(processInstanceId);
            }

            long endTime = System.currentTimeMillis();
            System.out.println("\n========== 批量执行完成 ==========");
            System.out.println("总执行时间: " + formatDuration(endTime - startTime));
            System.out.println("处理流程实例数: " + instanceCount);

            // 验证所有流程都已结束
            System.out.println("\n验证流程结束状态:");
            for (String processInstanceId : processInstanceIds) {
                ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (instance == null) {
                    System.out.println("✓ " + processInstanceId + ": 已结束");
                } else {
                    System.out.println("⚠ " + processInstanceId + ": 仍在运行");
                }
            }

        } catch (Exception e) {
            System.err.println("批量执行流程失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 执行流程到指定环节后停止
     * 支持执行到指定环节就停止，后续环境需手动再执行
     */
    @Test
    public void executeProcessToSpecificActivity() {
        try {
            // 1. 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V2");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 2. 执行到指定环节（例如执行前2个任务后停止）
            String targetActivityId = null; // 可以指定特定环节ID，null表示执行指定数量的任务
            int maxTasksToExecute = 2; // 执行2个任务后停止

            executeProcessToTargetActivity(processInstanceId, targetActivityId, maxTasksToExecute);

            // 3. 显示停止后的状态
            System.out.println("\n========== 流程执行停止后的状态 ==========");
            showProcessStatus(processInstanceId, "停止后状态");

            // 4. 显示当前待办任务，需要手动处理
            List<Task> pendingTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            if (!pendingTasks.isEmpty()) {
                System.out.println("\n========== 需要手动处理的任务 ==========");
                for (Task task : pendingTasks) {
                    System.out.println("任务ID: " + task.getId());
                    System.out.println("  任务名称: " + task.getName());
                    System.out.println("  负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));
                    System.out.println("  创建时间: " + task.getCreateTime());
                    System.out.println("  需要手动完成此任务以继续流程");
                    System.out.println("---");
                }
            }

            System.out.println("\n流程执行已暂停，剩余任务需要手动处理...");

        } catch (Exception e) {
            System.err.println("执行流程到指定环节失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 执行流程到指定环节或指定任务数量后停止
     *
     * @param processInstanceId 流程实例ID
     * @param targetActivityId  目标环节ID（可为null）
     * @param maxTasksToExecute 最大执行任务数（当targetActivityId为null时使用）
     */
    private void executeProcessToTargetActivity(String processInstanceId, String targetActivityId, int maxTasksToExecute) {
        try {
            int executedTaskCount = 0;
            System.out.println("\n开始执行流程到指定环节...");

            while (true) {
                // 检查流程是否已经结束
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (processInstance == null || processInstance.isEnded()) {
                    System.out.println("流程已结束，停止任务处理");
                    break;
                }

                // 检查是否达到指定任务数量
                if (maxTasksToExecute > 0 && executedTaskCount >= maxTasksToExecute) {
                    System.out.println("已执行 " + executedTaskCount + " 个任务，达到指定数量，停止执行");
                    break;
                }

                // 检查是否到达指定环节
                if (targetActivityId != null && isAtTargetActivity(processInstanceId, targetActivityId)) {
                    System.out.println("已到达目标环节 " + targetActivityId + "，停止执行");
                    break;
                }

                // 查询当前所有待办任务
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .list();

                if (tasks.isEmpty()) {
                    System.out.println("暂无待处理任务，流程可能在等待事件或其他条件");
                    break;
                }

                // 处理当前任务（只处理一个任务，然后重新检查条件）
                Task task = tasks.get(0);
                executedTaskCount++;
                System.out.println("正在处理任务 " + executedTaskCount + ": " + task.getName() + " (ID: " + task.getId() + ")");

                // 处理任务
                handleTaskProcessing(task);

                // 完成任务
                taskService.complete(task.getId());
                System.out.println("任务完成: " + task.getName());

                // 短暂等待
                Thread.sleep(100);
            }

            System.out.println("总共执行了 " + executedTaskCount + " 个任务");

        } catch (Exception e) {
            System.err.println("执行到指定环节过程中发生错误: " + e.getMessage());
            throw new RuntimeException("流程执行中断", e);
        }
    }

    /**
     * 检查是否到达指定环节
     *
     * @param processInstanceId 流程实例ID
     * @param targetActivityId  目标环节ID
     * @return 是否到达指定环节
     */
    private boolean isAtTargetActivity(String processInstanceId, String targetActivityId) {
        try {
            List<String> activeActivities = runtimeService.getActiveActivityIds(processInstanceId);
            return activeActivities.contains(targetActivityId);
        } catch (Exception e) {
            System.err.println("检查当前环节时发生错误: " + e.getMessage());
            return false;
        }
    }

    /**
     * 执行流程到指定任务名称后停止
     */
    @Test
    public void executeProcessToSpecificTaskName() {
        try {
            // 1. 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 2. 执行到指定任务名称后停止
            // String targetTaskName = "审批任务"; // 指定任务名称
            String targetTaskName = "环节B"; // 指定任务名称
            executeProcessToTargetTaskName(processInstanceId, targetTaskName);

            // 3. 显示停止后的状态
            System.out.println("\n========== 流程执行停止后的状态 ==========");
            showProcessStatus(processInstanceId, "停止后状态");

            // 4. 显示当前待办任务
            List<Task> pendingTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            System.out.println("\n========== 当前待办任务 ==========");
            if (pendingTasks.isEmpty()) {
                System.out.println("暂无待办任务");
            } else {
                for (Task task : pendingTasks) {
                    System.out.println("任务ID: " + task.getId());
                    System.out.println("  任务名称: " + task.getName());
                    System.out.println("  负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));
                    System.out.println("  状态: " + (task.getName().equals(targetTaskName) ? "目标任务（需要手动处理）" : "普通任务"));
                    System.out.println("---");
                }
            }

        } catch (Exception e) {
            System.err.println("执行流程到指定任务名称失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 执行流程到指定任务名称后停止
     *
     * @param processInstanceId 流程实例ID
     * @param targetTaskName    目标任务名称
     */
    private void executeProcessToTargetTaskName(String processInstanceId, String targetTaskName) {
        try {
            int executedTaskCount = 0;
            System.out.println("\n开始执行流程到指定任务名称: " + targetTaskName);

            while (true) {
                // 检查流程是否已经结束
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (processInstance == null || processInstance.isEnded()) {
                    System.out.println("流程已结束，停止任务处理");
                    break;
                }

                // 查询当前所有待办任务
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .list();

                if (tasks.isEmpty()) {
                    System.out.println("暂无待处理任务，流程可能在等待事件或其他条件");
                    break;
                }

                // 检查是否有目标任务
                Task targetTask = null;
                for (Task task : tasks) {
                    if (targetTaskName.equals(task.getName())) {
                        targetTask = task;
                        break;
                    }
                }

                // 如果有目标任务，停止执行
                if (targetTask != null) {
                    System.out.println("检测到目标任务 [" + targetTaskName + "]，停止自动执行");
                    System.out.println("目标任务详情:");
                    System.out.println("  任务ID: " + targetTask.getId());
                    System.out.println("  负责人: " + (targetTask.getAssignee() != null ? targetTask.getAssignee() : "未分配"));
                    break;
                }

                // 处理当前所有非目标任务
                for (Task task : tasks) {
                    executedTaskCount++;
                    System.out.println("正在处理任务 " + executedTaskCount + ": " + task.getName() + " (ID: " + task.getId() + ")");

                    // 处理任务
                    handleTaskProcessing(task);

                    // 完成任务
                    taskService.complete(task.getId());
                    System.out.println("任务完成: " + task.getName());
                }

                // 短暂等待
                Thread.sleep(100);
            }

            System.out.println("总共执行了 " + executedTaskCount + " 个任务");

        } catch (Exception e) {
            System.err.println("执行到指定任务名称过程中发生错误: " + e.getMessage());
            throw new RuntimeException("流程执行中断", e);
        }
    }

    /**
     * 分阶段执行流程（支持断点续执行）
     */
    @Test
    public void executeProcessInStages() {
        try {
            System.out.println("========== 分阶段执行流程 ==========");

            // 1. 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("1. 启动流程实例: " + processInstanceId);

            // 2. 第一阶段：执行前1个任务
            System.out.println("\n2. 开始第一阶段执行...");
            executeProcessToTargetActivity(processInstanceId, null, 1);
            System.out.println("第一阶段执行完成");
            showProcessStatus(processInstanceId, "第一阶段后状态");

            // 3. 第二阶段：执行剩余任务
            System.out.println("\n3. 开始第二阶段执行...");
            completeAllTasksUntilProcessEnds(processInstanceId);
            System.out.println("第二阶段执行完成");

            // 4. 最终状态
            System.out.println("\n4. 最终状态:");
            showProcessStatus(processInstanceId, "最终状态");

            // 5. 验证流程结束
            ProcessInstance finalInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (finalInstance == null) {
                System.out.println("✓ 流程成功结束");
            }

        } catch (Exception e) {
            System.err.println("分阶段执行流程失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 手动完成指定任务
     */
    @Test
    public void manuallyCompleteSpecificTask() {
        try {
            // 1. 启动流程实例并执行到指定环节
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 2. 执行前1个任务
            executeProcessToTargetActivity(processInstanceId, null, 1);

            // 3. 显示当前待办任务
            List<Task> pendingTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            System.out.println("\n========== 当前待办任务 ==========");
            for (int i = 0; i < pendingTasks.size(); i++) {
                Task task = pendingTasks.get(i);
                System.out.println((i + 1) + ". 任务ID: " + task.getId());
                System.out.println("   任务名称: " + task.getName());
                System.out.println("   负责人: " + (task.getAssignee() != null ? task.getAssignee() : "未分配"));
            }

            if (!pendingTasks.isEmpty()) {
                // 4. 手动完成第一个待办任务
                Task taskToComplete = pendingTasks.get(0);
                System.out.println("\n手动完成任务: " + taskToComplete.getName());

                // 可以添加任务处理逻辑
                taskService.setVariable(taskToComplete.getId(), "manuallyProcessed", true);
                taskService.setVariable(taskToComplete.getId(), "manualProcessor", "tester");

                // 完成任务
                taskService.complete(taskToComplete.getId());
                System.out.println("任务完成: " + taskToComplete.getName());

                // 5. 继续自动完成剩余任务
                System.out.println("\n继续自动完成剩余任务...");
                completeAllTasksUntilProcessEnds(processInstanceId);
            }

        } catch (Exception e) {
            System.err.println("手动完成任务失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 执行流程并在指定环节设置断点（暂停执行）
     */
    @Test
    public void executeProcessWithBreakpoint() {
        try {
            System.out.println("========== 带断点的流程执行 ==========");

            // 1. 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleProcess-V1");
            String processInstanceId = processInstance.getId();
            System.out.println("启动流程实例: " + processInstanceId);

            // 2. 设置断点 - 在特定环节暂停
            String breakpointActivityId = "breakpointActivity"; // 假设的断点环节ID
            System.out.println("设置断点环节: " + breakpointActivityId);

            // 3. 执行到断点
            executeProcessToBreakpoint(processInstanceId, breakpointActivityId);

            // 4. 断点状态
            System.out.println("\n========== 流程在断点处暂停 ==========");
            showProcessStatus(processInstanceId, "断点状态");

            // 5. 模拟人工干预后继续执行
            System.out.println("\n模拟人工干预后继续执行...");
            completeAllTasksUntilProcessEnds(processInstanceId);

            System.out.println("流程执行完成");

        } catch (Exception e) {
            System.err.println("带断点的流程执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 执行流程到断点处暂停
     *
     * @param processInstanceId    流程实例ID
     * @param breakpointActivityId 断点环节ID
     */
    private void executeProcessToBreakpoint(String processInstanceId, String breakpointActivityId) {
        try {
            int executedTaskCount = 0;
            System.out.println("\n开始执行流程到断点...");

            while (true) {
                // 检查流程是否已经结束
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (processInstance == null || processInstance.isEnded()) {
                    System.out.println("流程已结束，停止任务处理");
                    break;
                }

                // 检查是否到达断点
                if (isAtTargetActivity(processInstanceId, breakpointActivityId)) {
                    System.out.println("已到达断点环节 " + breakpointActivityId + "，暂停执行");
                    // 在断点处可以执行一些特殊处理
                    handleBreakpointReached(processInstanceId, breakpointActivityId);
                    break;
                }

                // 查询当前所有待办任务
                List<Task> tasks = taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .list();

                if (tasks.isEmpty()) {
                    System.out.println("暂无待处理任务，流程可能在等待事件或其他条件");
                    break;
                }

                // 处理当前任务
                for (Task task : tasks) {
                    // 检查任务所属环节是否为断点
                    if (breakpointActivityId.equals(task.getTaskDefinitionKey())) {
                        System.out.println("即将到达断点环节，暂停执行");
                        System.out.println("断点任务: " + task.getName() + " (ID: " + task.getId() + ")");
                        handleBreakpointReached(processInstanceId, breakpointActivityId);
                        return;
                    }

                    executedTaskCount++;
                    System.out.println("正在处理任务 " + executedTaskCount + ": " + task.getName() + " (ID: " + task.getId() + ")");

                    // 处理任务
                    handleTaskProcessing(task);

                    // 完成任务
                    taskService.complete(task.getId());
                    System.out.println("任务完成: " + task.getName());
                }

                // 短暂等待
                Thread.sleep(100);
            }

            System.out.println("在断点处暂停，已执行 " + executedTaskCount + " 个任务");

        } catch (Exception e) {
            System.err.println("执行到断点过程中发生错误: " + e.getMessage());
            throw new RuntimeException("流程执行中断", e);
        }
    }

    /**
     * 处理到达断点时的逻辑
     *
     * @param processInstanceId    流程实例ID
     * @param breakpointActivityId 断点环节ID
     */
    private void handleBreakpointReached(String processInstanceId, String breakpointActivityId) {
        try {
            System.out.println(">>> 流程在环节 [" + breakpointActivityId + "] 处暂停 <<<");
            System.out.println(">>> 需要人工干预或特殊处理 <<<");

            // 可以在这里添加断点处理逻辑
            // 例如：发送通知、记录日志、等待外部信号等

        } catch (Exception e) {
            System.err.println("处理断点时发生错误: " + e.getMessage());
        }
    }


}
