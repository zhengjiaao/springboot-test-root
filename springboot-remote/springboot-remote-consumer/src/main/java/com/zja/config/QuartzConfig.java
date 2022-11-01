package com.zja.config;

import com.zja.task.QuartzJob;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**三种种配置Quartz方式
 * 方式二：使用 cron 表达式
 */
@Configuration
public class QuartzConfig {

    //SimpleTrigger和CronTrigger的区别：
    //  SimpleTrigger在具体的时间点执行一次或按指定时间间隔执行多次
    //  CronTrigger按Cron表达式的方式去执行更常用

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

}
