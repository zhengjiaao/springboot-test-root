package com.zja.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Spring Task  @Scheduled 定时任务多线程运行
 *
 * 默认，使同一个线程中串行执行
 * 缺点：存在多个定时任务，若其中一个任务卡死，会导致其它任务无法执行
 */
@Component
@Async("asynTaskExecutor")
public class SpringTaskAsyncExample {

    /**默认是fixedDelay 上一次执行完毕时间后执行下一轮*/
    @Scheduled(cron = "0/5 * * * * *")  //cron 表示 在指定时间执行
    //@Scheduled(cron = "0 9 15 * * ?")   //每天15点9分执行一次  ，具体参考 README.md 格式
    public void run() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println("SpringTaskAsyncExample " + Thread.currentThread().getName() + "=====>>>>>使用cron  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }

    /**fixedRate:上一次开始执行时间点之后5秒再执行*/
    @Scheduled(fixedRate = 5000)  //fixedRate 表示 每隔 5000 毫秒执行一次
    public void run1() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println("SpringTaskAsyncExample " + Thread.currentThread().getName() + "=====>>>>>使用fixedRate  {}" + "每隔五秒执行一次 时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }

    /**fixedDelay:上一次执行完毕时间点之后5秒再执行*/
    @Scheduled(fixedDelay = 5000)
    public void run2() throws InterruptedException {
        Thread.sleep(7000);
        System.out.println("SpringTaskAsyncExample " + Thread.currentThread().getName() + "=====>>>>>使用fixedDelay  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }

    /**第一次延迟2秒后执行，之后按fixedDelay的规则每5秒执行一次*/
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void run3() {
        System.out.println("SpringTaskAsyncExample " + Thread.currentThread().getName() + "=====>>>>>使用initialDelay  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }
}
