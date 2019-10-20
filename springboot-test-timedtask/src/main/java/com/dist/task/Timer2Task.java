package com.dist.task;

import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** 1.Timer:是java自带的java.util.Timer类，这个类允许你调度一个java.util.TimerTask任务。
 * 使用这种方式可以让你的程序按照某一个频度执行，但不能在指定时间运行。一般用的较少
 *
 * 最大缺点：多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题
 * 2.ScheduledExecutorService: 也jdk自带的一个类；是基于线程池设计的定时任务类,每个调度任务都会分配到线程池中的一个线程去执行,也就是说,任务是并发执行,互不影响
 *
 * @author zhengja@dist.com.cn
 * @data 2019/8/13 13:57
 */
public class Timer2Task {

    public static void main(String[] args) {
        //1.Timer
        java.util.TimerTask timerTask = new java.util.TimerTask() {
            @Override
            public void run() {
                System.out.println("task  run:"+ new Date());
            }
        };
        //多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题
        Timer timer = new Timer();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。这里是每3秒执行一次
        timer.schedule(timerTask,10,3000);

        //2. ScheduledExecutorService
        //手动创建线程池效果会更好
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        // 参数：1、任务体 2、首次执行的延时时间
        //      3、任务执行间隔 4、间隔时间单位
        service.scheduleAtFixedRate(()->System.out.println("task ScheduledExecutorService "+new Date()), 0, 3, TimeUnit.SECONDS);
    }
}
