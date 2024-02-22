package com.zja.thread.dynamic.tp.config;

import org.dromara.dynamictp.core.executor.DtpExecutor;
import org.dromara.dynamictp.core.executor.OrderedDtpExecutor;
import org.dromara.dynamictp.core.support.DynamicTp;
import org.dromara.dynamictp.core.support.ThreadPoolBuilder;
import org.dromara.dynamictp.core.support.ThreadPoolCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.dromara.dynamictp.common.em.QueueTypeEnum.MEMORY_SAFE_LINKED_BLOCKING_QUEUE;
import static org.dromara.dynamictp.common.em.RejectedTypeEnum.CALLER_RUNS_POLICY;

/**
 * @author: zhengja
 * @since: 2024/02/22 11:30
 */
@Configuration
public class ThreadPoolConfiguration {

    /**
     * 通过{@link DynamicTp} 注解定义普通juc线程池，会享受到该框架增强能力，注解名称优先级高于方法名
     *
     * @return 线程池实例
     */
    @DynamicTp("jucThreadPoolExecutor")
    @Bean
    public ThreadPoolExecutor jucThreadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    }

    /**
     * 通过{@link DynamicTp} 注解定义spring线程池，会享受到该框架增强能力，注解名称优先级高于方法名
     *
     * @return 线程池实例
     */
    @DynamicTp("threadPoolTaskExecutor")
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    /**
     * 通过{@link ThreadPoolCreator} 快速创建一些简单配置的线程池，使用默认参数
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    @Bean
    public DtpExecutor dtpExecutor0() {
        return ThreadPoolCreator.createDynamicFast("dtpExecutor0");
    }

    /**
     * 通过{@link ThreadPoolBuilder} 设置详细参数创建动态线程池
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    @Bean
    public ThreadPoolExecutor dtpExecutor1() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("dtpExecutor1")
                .threadFactory("test-dtp-common")
                .corePoolSize(10)
                .maximumPoolSize(15)
                .keepAliveTime(40)
                .timeUnit(TimeUnit.SECONDS)
                // .workQueue(MEMORY_SAFE_LINKED_BLOCKING_QUEUE.getName(), 2000)
                .workQueue(MEMORY_SAFE_LINKED_BLOCKING_QUEUE.getName(), 100)
                .buildDynamic();
    }

    /**
     * 增强线程池 DtpExecutor
     * <p>
     * 通过{@link ThreadPoolBuilder} 设置详细参数创建动态线程池
     * eager，参考tomcat线程池设计，适用于处理io密集型任务场景，具体参数可以看代码注释
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    @Bean
    public DtpExecutor eagerDtpExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("eagerDtpExecutor")
                .threadFactory("test-eager")
                .corePoolSize(2)
                .maximumPoolSize(4)
                .queueCapacity(2000)
                .eager(true)
                .buildDynamic();
    }

    /**
     * 有序线程池 OrderedDtpExecutor
     * <p>
     * 通过{@link ThreadPoolBuilder} 设置详细参数创建动态线程池
     * ordered，适用于处理有序任务场景，任务要实现Ordered接口，具体参数可以看代码注释
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    @Bean
    public OrderedDtpExecutor orderedDtpExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("orderedDtpExecutor")
                .threadFactory("test-ordered")
                .corePoolSize(4)
                .maximumPoolSize(4)
                .queueCapacity(2000)
                .buildOrdered();
    }

    /**
     * 调度线程池 ScheduledDtpExecutor
     * <p>
     * 通过{@link ThreadPoolBuilder} 设置详细参数创建线程池
     * scheduled，适用于处理定时任务场景，具体参数可以看代码注释
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    @Bean
    public ScheduledExecutorService scheduledDtpExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("scheduledDtpExecutor")
                .corePoolSize(2)
                .threadFactory("test-scheduled")
                .rejectedExecutionHandler(CALLER_RUNS_POLICY.getName())
                .buildScheduled();
    }

    /**
     * 通过{@link ThreadPoolBuilder} 设置详细参数创建线程池
     * priority，适用于处理优先级任务场景，具体参数可以看代码注释
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    // @Bean
    // public PriorityDtpExecutor priorityDtpExecutor() {
    //     return ThreadPoolBuilder.newBuilder()
    //             .threadPoolName("priorityDtpExecutor")
    //             .corePoolSize(2)
    //             .maximumPoolSize(4)
    //             .threadFactory("test-priority")
    //             .rejectedExecutionHandler(CALLER_RUNS_POLICY.getName())
    //             .buildPriority();
    // }

}