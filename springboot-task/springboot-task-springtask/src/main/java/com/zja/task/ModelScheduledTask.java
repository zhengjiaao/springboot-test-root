package com.zja.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

/**
 * 模型运行定时任务（启动模型，更新模型状态） 为了解决：@Scheduled 注解中不能使用变量
 *
 * @Author: zhengja
 * @Date: 2025-09-19 14:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelScheduledTask implements SchedulingConfigurer {

    // private final IndicatorModelService indicatorModelService;

    @Value("${feign.dme.model-running-check-interval:5}")
    private int modelRunningCheckInterval; // 模型运行检查间隔，单位：秒

    @Value("${feign.dme.model-running-status-update-interval:5}")
    private int modelRunningStatusUpdateInterval; // 模型运行状态更新间隔，单位：秒

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 动态配置 runModel 任务
        taskRegistrar.addFixedRateTask(
                // () -> indicatorModelService.startModelAsync(),
                this::startModelAsync,
                modelRunningCheckInterval * 1000L
        );

        // 动态配置 updateStatus 任务
        taskRegistrar.addFixedRateTask(
                // () -> indicatorModelService.checkAndUpdateRunningModelStatusAsync(),
                this::checkAndUpdateRunningModelStatusAsync,
                modelRunningStatusUpdateInterval * 1000L
        );
    }

    public void startModelAsync() {
        // 启动模型
        System.out.println("启动模型");
    }

    // 更新模型状态
    public void checkAndUpdateRunningModelStatusAsync() {
        System.out.println("更新模型状态");
    }
}