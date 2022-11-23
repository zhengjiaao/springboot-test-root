package com.zja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务执行器配置
 */
@Configuration
public class AsynTaskExecutorConfig {

    //线程池维护线程的最少数量
    private int corePoolSize = 10;

    //线程池维护线程的最大数量-只有在缓冲队列满了之后才会申请超过核心线程数的线程
    private int maxPoolSize = 200;

    //允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    private int keepAliveSeconds = 20;

    //缓存队列
    private int queueCapacity = 10;

    /**
     * 线程池
     * 使用方式：在定时任务的类或者方法上添加 @Async("asynTaskExecutor")
     */
    @Bean("asynTaskExecutor")
    public Executor asynTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("AsynTaskExecutor-");
        executor.initialize();
        return executor;
    }

}
