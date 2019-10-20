package com.dist.config;

import com.dist.dao.TaskTimeDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**AsyncConfigurer :配置默认线程池
 * 使用默认线程池的时候，只需在方法上加上@Async即可
 *
 * SchedulingConfigurer: 动态创建定时任务（配合数据库动态执行）
 *
 * @author zhengja@dist.com.cn
 * @data 2019/8/13 16:05
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ExecutorConfig implements SchedulingConfigurer, AsyncConfigurer {

    private static final Logger LOG = LogManager.getLogger(ExecutorConfig.class.getName());

    //线程池维护线程的最少数量
    private int corePoolSize = 10;

    //线程池维护线程的最大数量-只有在缓冲队列满了之后才会申请超过核心线程数的线程
    private int maxPoolSize = 200;

    //允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    private int keepAliveSeconds = 20;

    //缓存队列
    private int queueCapacity = 10;


    @Autowired
    private TaskTimeDao taskTimeDao;

    /*@Autowired
    TaskThreadPoolConfig config;*/

    /**
     * 默认线程池配置
     * 注意：该线程池被所有的异步任务共享，而不属于某一个异步任务
     * 描述：配置异步任务的线程池
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("ExecutorTask-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //线程池对拒绝任务（无线程可用）的处理策略。这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 异步任务中异常处理
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                LOG.error("==========================" + ex.getMessage() + "=======================", ex);
                LOG.error("exception method:" + method.getName());
            }
        };
    }

    /**配置动态定时任务（数据库动态修改配置）：修改之后，不需要重启项目，配置可以立即生效；
     * 如果数据库里某个定时任务格式配置错误，只关闭配置错误的这个，不会影响其它的定时任务执行。
     * 关闭的定时任务，就算数据库里配置从新改成正确的，关闭的任务也不会重新启动，只能重启项目。
     *
     * @param scheduledTaskRegistrar
     */
    @Transactional  //防止出现no session
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        /*scheduledTaskRegistrar.addTriggerTask( //1.添加任务内容(Runnable)
                () -> System.out.println("执行定时任务2: " + LocalDateTime.now().toLocalTime()),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    TaskTimeEntity timeEntity = taskTimeDao.queryByTaskKey("scheduledTask04");
                    String cron = timeEntity.getTaskCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                        System.out.println("时间格式不合法，定时任务停止，请修改完成后重启项目 !!!");
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

        scheduledTaskRegistrar.addTriggerTask( //1.添加任务内容(Runnable)
                () -> System.out.println("执行定时任务3: " + LocalDateTime.now().toLocalTime()),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    TaskTimeEntity timeEntity = taskTimeDao.queryByTaskKey("scheduledTask05");
                    String cron = timeEntity.getTaskCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                        System.out.println("时间格式不合法，定时任务停止，请修改完成后重启项目 !!!");
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

        scheduledTaskRegistrar.addTriggerTask( //1.添加任务内容(Runnable)
                () -> test(),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    TaskTimeEntity timeEntity = taskTimeDao.queryByTaskKey("scheduledTask06");
                    String cron = timeEntity.getTaskCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                        System.out.println("时间格式不合法，定时任务停止，请修改完成后重启项目 !!!");
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );*/

        /*TaskScheduler taskScheduler = taskScheduler();
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler);*/
    }

    /**定时任务多线程处理：
     * 并行任务使用策略：多线程处理
     * @return ThreadPoolTaskScheduler 线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(corePoolSize);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    private String test(){
        System.out.println("进入定时任务4: "+ LocalDateTime.now().toLocalTime());
        return "4";
    }
}
