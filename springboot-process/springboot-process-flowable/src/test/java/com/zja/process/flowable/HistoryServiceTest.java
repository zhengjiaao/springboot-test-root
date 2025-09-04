package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Flowable 的 HistoryService 用于查询历史数据
 *
 * @Author: zhengja
 * @Date: 2025-09-01 20:10
 */
public class HistoryServiceTest extends ProcessFlowableApplicationTests {

    @Autowired
    private HistoryService historyService;

    static final String processInstanceId = "92fdb871-872b-11f0-90b7-8245ddc034b5";

    /**
     * 获取某个流程实例的所有历史任务，查询历史任务记录（谁处理了哪些任务）
     */
    @Test
    public void testGetHistoricTasksByProcessInstanceId() {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceEndTime().asc() // 按完成时间排序
                .list();

        for (HistoricTaskInstance task : historicTasks) {
            System.out.println("任务ID：" + task.getId());
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务定义键：" + task.getTaskDefinitionKey());
            System.out.println("任务处理人：" + task.getAssignee());
            System.out.println("任务开始时间：" + task.getCreateTime());
            System.out.println("任务结束时间：" + task.getEndTime());
            System.out.println("任务状态：" + (task.getEndTime() == null ? "进行中" : "已完成"));
            System.out.println("任务持续时间：" + task.getDurationInMillis()); // 流程总耗时、各环节耗时
            System.out.println();
        }

        System.out.println(historicTasks);
    }

    // 流程审计报告
    @Test
    public void generateProcessAuditReport() {
        // String processInstanceId = "your-process-instance-id";

        // 获取流程实例信息
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println("流程实例ID：" + processInstance.getId());
        System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程开始时间：" + processInstance.getStartTime());
        System.out.println("流程结束时间：" + processInstance.getEndTime());
        System.out.println("流程持续时间：" + processInstance.getDurationInMillis());
        System.out.println("流程状态：" + (processInstance.getEndTime() == null ? "进行中" : "已完成"));
        System.out.println();

        // 获取所有活动历史
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (HistoricActivityInstance activity : activities) {
            System.out.println("活动名称：" + activity.getActivityName());
            System.out.println("活动ID：" + activity.getActivityId());
            System.out.println("活动流程定义ID：" + activity.getProcessDefinitionId());
            System.out.println("活动流程实例ID：" + activity.getProcessInstanceId());
            System.out.println("活动类型：" + activity.getActivityType());
            System.out.println("活动开始时间：" + activity.getStartTime());
            System.out.println("活动结束时间：" + activity.getEndTime());
            System.out.println("活动持续时间：" + activity.getDurationInMillis());
            System.out.println();
        }

        // 获取所有任务历史
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (HistoricTaskInstance task : tasks) {
            System.out.println("任务名称：" + task.getName());
            System.out.println("任务处理人：" + task.getAssignee());
            System.out.println("任务开始时间：" + task.getCreateTime());
            System.out.println("任务结束时间：" + task.getEndTime());
            System.out.println("任务状态：" + (task.getEndTime() == null ? "进行中" : "已完成"));
            System.out.println("任务持续时间：" + task.getDurationInMillis());
            System.out.println();
        }

        // 生成审计报告...
    }

    // 流程执行性能分析
    @Test
    public void analyzeProcessPerformance() {
        // 查询执行时间超过阈值的流程实例
        List<HistoricProcessInstance> slowProcesses = historyService.createHistoricProcessInstanceQuery()
                .finished()
                .list()
                .stream()
                .filter(p -> p.getDurationInMillis() > 3600000) // 超过1小时
                .collect(Collectors.toList());

        // 分析平均执行时间
        double avgDuration = slowProcesses.stream()
                .mapToLong(HistoricProcessInstance::getDurationInMillis)
                .average()
                .orElse(0);

        System.out.println("超过1小时流程实例数量：" + slowProcesses.size());
        System.out.println("平均执行时间：" + avgDuration);
    }

    // 用户工作量统计
    @Test
    public void calculateUserWorkload() {
        // 统计用户完成的任务数量
        Map<String, Long> userTaskCount = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .list()
                .stream()
                .collect(Collectors.groupingBy(
                        HistoricTaskInstance::getAssignee,
                        Collectors.counting()
                ));
        System.out.println("用户完成任务数量：" + userTaskCount);
    }

}
