package com.zja.task;

import java.util.Date;

/**
 * Quartz Job 方式二
 */
public class QuartzJob {

    public String quartzMethod1() {
        System.out.println("执行方法：quartzMethod1  " + "当前线程名称：" + Thread.currentThread().getName() + " " + new Date());
        return "quartzMethod1";
    }

    public void quartzMethod2() {
        System.out.println("执行方法：quartzMethod2  " + "当前线程名称：" + Thread.currentThread().getName() + " " + new Date());
    }
}
