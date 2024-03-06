package com.zja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/** 配置自定义线程池：多线程执行任务
 *  然后在定时任务的类或者方法上添加@Async("taskExecutor")
 *  最后重启项目，每一个任务都是在不同的线程中
 * @author: zhengja
 * @since: 2019/8/13 14:26
 */
@Configuration
@EnableAsync //开启异步事件支持
public class AsynConfig {

    /*@Autowired
    TaskThreadPoolConfig config;*/

    //线程池维护线程的最少数量
    private int corePoolSize = 10;

    //线程池维护线程的最大数量-只有在缓冲队列满了之后才会申请超过核心线程数的线程
    private int maxPoolSize = 200;

    //允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    private int keepAliveSeconds = 20;

    //缓存队列
    private int queueCapacity = 60;

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("taskExecutorName-");
        executor.initialize();
        return executor;
    }
}
