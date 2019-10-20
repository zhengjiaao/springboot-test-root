package com.dist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义-线程池配置
 * @author zhengja@dist.com.cn
 * @data 2019/5/7 16:45
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 配置线程池
     * @return
     */
    @Bean(name = "asyncPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        // 线程池维护线程的最少数量
        pool.setCorePoolSize(5);
        // 线程池维护线程的最大数量-只有在缓冲队列满了之后才会申请超过核心线程数的线程
        pool.setMaxPoolSize(2000);
        //线程名称后缀
        pool.setThreadNamePrefix("asyncPoolTaskExecutor-");
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

   /* 参数说明：
    corePoolSize：线程池维护线程的最少数量
    keepAliveSeconds：允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    maxPoolSize：线程池维护线程的最大数量,只有在缓冲队列满了之后才会申请超过核心线程数的线程
    queueCapacity：缓存队列
    rejectedExecutionHandler：线程池对拒绝任务（无线程可用）的处理策略。这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务。
    还有一个是AbortPolicy策略：处理程序遭到拒绝将抛出运行时RejectedExecutionException*/
}
