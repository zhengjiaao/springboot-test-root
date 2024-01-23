package com.zja.thread.pools.config;

import com.zja.thread.pools.rejected.ThrowingExceptionRejectedExecutionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:31
 * 线程池流程：当任务数量超过核心线程数时[CorePoolSize]，多余的任务将被放入任务队列中[QueueCapacity]，例如最多可容纳25个任务。当任务队列已满并且仍有新的任务提交时，线程池会创建新的线程执行任务，直到线程数达到最大线程数[MaxPoolSize]。
 * <p>
 * 异常说明：
 * 1.当任务数量超过[MaxPoolSize+QueueCapacity]时，会抛出[TaskRejectedException]任务被拒绝异常.
 * 2.
 */
@Configuration
public class ThreadPoolsConfig {

    /**
     * 固定大小线程池:这种配置适用于负载相对稳定的场景，线程池中的线程数固定不变。适合执行耗时短且数量有限的任务。
     * 异常说明：由于[QueueCapacity=0],当任务数量超过[MaxPoolSize]时，会抛出[TaskRejectedException]任务被拒绝异常。
     */
    @Bean
    public Executor taskExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(0); // 任务超过MaxPoolSize=5时，队列无法存放任务，则会抛异常。
        executor.setThreadNamePrefix("FixedThreadPool-");
        executor.initialize();
        return executor;
    }

    /**
     * 缓存线程池:这种配置适用于需要处理大量耗时较短的任务的场景，线程池根据任务的数量自动调整线程池的大小。
     */
    @Bean
    public Executor taskExecutor3() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(0);
        executor.setMaxPoolSize(Integer.MAX_VALUE); // 每个任务会创建一个新的线程
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("CachedThreadPool-");
        executor.initialize();
        return executor;
    }

    /**
     * 可调整大小线程池:这种配置适用于负载波动较大的场景，线程池的大小根据任务的数量和队列的长度动态调整。
     * 异常说明：当任务数量超过[MaxPoolSize+QueueCapacity]时，会抛出[TaskRejectedException]任务被拒绝异常,队列已满。
     */
    @Bean
    public Executor taskExecutor4() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 设置线程池的核心线程数为5。这意味着线程池会始终保持5个核心线程在运行状态，即使没有任务执行。
        executor.setMaxPoolSize(10); // 设置线程池的最大线程数为10。当任务数量超过核心线程数并且任务队列已满时，线程池会创建新的线程，直到线程数达到最大线程数。
        executor.setQueueCapacity(25); // 设置线程池的任务队列容量为25。当任务数量超过核心线程数时，未被立即执行的任务将被放入任务队列中，等待执行。
        executor.setThreadNamePrefix("AdjustableThreadPool-"); // 每个线程的名称将以此前缀开头，方便识别线程池中的线程。
        executor.initialize(); // 初始化线程池
        return executor;
    }

    /***
     * 单线程线程池:这种配置适用于需要按顺序执行任务的场景，线程池中只有一个线程。
     * 异常说明：由于[QueueCapacity=0],当任务数量超过[MaxPoolSize]时，会抛出[TaskRejectedException]任务被拒绝异常。
     */
    @Bean
    public Executor taskExecutor5() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("SingleThreadExecutor-");
        executor.initialize();
        return executor;
    }

    /***
     * 单线程线程池:这种配置适用于需要按顺序执行任务的场景，线程池中只有一个线程。
     * 异常说明：由于[QueueCapacity=0],当任务数量超过[MaxPoolSize]时，会抛出[TaskRejectedException]任务被拒绝异常。
     * 异常解决方案：设置自定义的饱和策略
     */
    @Bean
    public Executor taskExecutor6() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("SingleThreadExecutor-");

        // 设置自定义的饱和策略
        executor.setRejectedExecutionHandler(new ThrowingExceptionRejectedExecutionHandler());

        executor.initialize();
        return executor;
    }
}
