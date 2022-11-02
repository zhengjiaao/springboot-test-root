package com.zja.config;

import com.zja.service.ScheduledTaskJob;
import com.zja.taskenum.ScheduledTaskEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;

/**Scheduled 动态定时任务调度
 * 创建定时任务线程池,初始化任务Map
 *
 * 目的：动态可配定时任务可实现不重启管理任务（动态定时任务）
 * 说明：任务：启动任务、停止任务、重启任务、启动所有正常状态的任务等
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 11:00
 */
@Configuration
public class ScheduledTaskConfig {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskConfig.class);

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        LOGGER.info("创建定时任务调度线程池 start");
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("ScheduledTaskExecutor-");
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        LOGGER.info("创建定时任务调度线程池 end");
        return threadPoolTaskScheduler;
    }

    /**
     * 初始化定时任务Map
     * key :任务key
     * value : 执行接口实现
     */
    @Bean(name = "scheduledTaskJobMap")
    public Map<String, ScheduledTaskJob> scheduledTaskJobMap() {
        return ScheduledTaskEnum.initScheduledTask();
    }


}
