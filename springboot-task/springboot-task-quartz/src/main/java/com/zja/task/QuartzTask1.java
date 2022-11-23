package com.zja.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * Quartz 方式一
 *
 * 3.Quartz 任务类：该类主要是继承了QuartzJobBean
 *  Quartz适用于分布式场景下的定时任务，可以根据需要多实例部署定时任务
 *  是一个功能比较强大的的调度器，可以让你的程序在指定时间执行，也可以按照某一个频度执行
 */
public class QuartzTask1 extends QuartzJobBean {

    /**
     * 执行定时任务
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("QuartzTask "+new Date()+" ，当前线程名称："+Thread.currentThread().getName());
    }

}
