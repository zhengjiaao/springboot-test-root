package com.zja.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 模型运行定时任务（启动模型，更新模型状态） 使用 @Scheduled 注解
 *
 * @Author: zhengja
 * @Date: 2025-09-19 14:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelScheduledTaskV2 {

    // private final IndicatorModelService indicatorModelService;

    /**
     * 运行模型
     */
    @Scheduled(fixedRate = 5 * 1000) // 表示每隔 5s 执行一次，上一次开始执行时间点之后5秒再执行
    public void runModel() {
        // indicatorModelService.startModelAsync();
        startModelAsync();
    }

    /**
     * 更新模型运行状态
     */
    @Scheduled(fixedRate = 5 * 1000)
    public void updateStatus() {
        // indicatorModelService.checkAndUpdateRunningModelStatusAsync();
        checkAndUpdateRunningModelStatusAsync();
    }

    public void startModelAsync() {
        // 启动模型
        System.out.println("启动模型V2");
    }

    // 更新模型状态
    public void checkAndUpdateRunningModelStatusAsync() {
        System.out.println("更新模型状态V2");
    }

}
