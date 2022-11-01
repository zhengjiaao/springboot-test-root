package com.zja.config;

import com.zja.task.QuartzJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**三种种配置Quartz方式
 * 方式一：使用Trigger
 * 方式二：使用 cron 表达式
 * 方式三: 动态设置任务-配合方式二, 与方式一有冲突,需要注释掉方式一
 * @author zhengja@dist.com.cn
 * @data 2019/8/13 15:22
 */
@Configuration
public class QuartzConfig {

    //SimpleTrigger和CronTrigger的区别：SimpleTrigger在具体的时间点执行一次或按指定时间间隔执行多次，CronTrigger按Cron表达式的方式去执行更常用


    //Quartz 方式一 使用Trigger 启动方式-项目启动自动执行, 与方式三冲突,测试 需注释掉方式三
    /**
     * 定时任务执行
     * @return
     */
    /*@Bean
    public JobDetail quartzTaskDetail(){
        return JobBuilder.newJob(QuartzTask.class).withIdentity("quartzTask").storeDurably().build();
    }

    *//**
     * 任务触发器
     * @return
     *//*
    @Bean
    public Trigger quartzTaskTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)  //设置时间周期单位秒
                .repeatForever();  //重复执行
        return TriggerBuilder.newTrigger().forJob(quartzTaskDetail())
                .withIdentity("quartzTask")
                .withSchedule(scheduleBuilder)
                .build();
    }

    *//**
     * 定时任务执行
     * @return
     *//*
    @Bean
    public JobDetail quartzTaskDetail2(){
        return JobBuilder.newJob(QuartzTask2.class).withIdentity("quartzTask2").storeDurably().build();
    }

    *//**
     * 任务触发器
     * @return
     *//*
    @Bean
    public Trigger quartzTaskTrigger2(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10)  //设置时间周期单位秒,执行间隔
                .repeatForever();  //永远重复执行，也可以改成设置重复执行次数.withRepeatCount(1));
        return TriggerBuilder.newTrigger().forJob(quartzTaskDetail2())
                .withIdentity("quartzTask2")
                .withSchedule(scheduleBuilder)
                .build();
    }*/


    //Quartz 方式二 使用 cron 表达式 :启动方式-项目启动自动执行
    // 定义方法，做什么
    @Bean(name = "job1")
    public MethodInvokingJobDetailFactoryBean job1(QuartzJob quartzJob){  //参数：可以传类/可以是接口，但是类要注入到 bean里，不然找不到
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        factoryBean.setConcurrent(true);
        // 使用哪个对象
        factoryBean.setTargetObject(quartzJob);
        // 使用对象中的哪个方法
        factoryBean.setTargetMethod("quartzMethod1");

        return factoryBean;
    }

    // 定义什么时候做，使用 cron 表达式
    @Bean(name = "cron1")
    public CronTriggerFactoryBean cron1(@Qualifier("job1")MethodInvokingJobDetailFactoryBean job1){
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        // 设置job对象
        factoryBean.setJobDetail( job1.getObject() );
        // 设置执行时间
        factoryBean.setCronExpression("0/5 * * * * ?");
        return  factoryBean;
    }

    // 定义方法，做什么
    @Bean(name = "job2")
    public MethodInvokingJobDetailFactoryBean job2(QuartzJob quartzJob){
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        factoryBean.setConcurrent(true);
        // 使用哪个对象
        factoryBean.setTargetObject(quartzJob);
        // 使用哪个方法
        factoryBean.setTargetMethod("quartzMethod2");

        return  factoryBean;
    }

    // 定义什么时候做，使用 cron 表达式
    @Bean(name = "cron2")
    public CronTriggerFactoryBean cron2(@Qualifier("job2")MethodInvokingJobDetailFactoryBean job2){
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        // 设置job对象
        factoryBean.setJobDetail( job2.getObject() );
        // 设置执行时间
        factoryBean.setCronExpression("0/10 * * * * ?");
        return  factoryBean;
    }


    //方式三 :与方式一冲突,测试时注释掉 方式一
    // 定义 任务，传入 triggers
    @Bean(name = "sch")
    public SchedulerFactoryBean scheduler1(Trigger ... triggers){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        // 设置 triggers
        factoryBean.setTriggers( triggers );
        // 自动运行
        factoryBean.setAutoStartup(true);

        return factoryBean;
    }


}
