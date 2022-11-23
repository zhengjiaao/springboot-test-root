/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-23 13:40
 * @Since:
 */
package com.zja.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Spring Task  @Scheduled 定时任务
 *
 * 默认，是串行的定时任务，支持启动多线程并行执行任务
 *
 * @Scheduled
 *      cron通过表达式来配置任务执行时间；
 *      fixedRate每隔多少*毫秒执行一次；
 *      fixedDelay上一次执行完毕时间点之后多少*秒再执行；
 *      initialDelay第一次延迟多少*秒后执行，之后按fixedDelay的规则每隔多少*秒执行一次
 */
@Component
public class SpringTaskExample {

    /**默认是fixedDelay 上一次执行完毕时间后执行下一轮*/
    @Scheduled(cron = "0/5 * * * * *")  //cron 表示 在指定时间执行
//    @Scheduled(cron = "0 9 15 * * ?")   //每天15点9分执行一次  ，具体参考 README.md 格式
    public void run() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println("SpringTaskExample " + Thread.currentThread().getName() + "=====>>>>>使用cron  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }

    /**fixedRate:上一次开始执行时间点之后5秒再执行*/
    @Scheduled(fixedRate = 5000)  //fixedRate 表示 每隔 5000 毫秒执行一次
    public void run1() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println("SpringTaskExample " + Thread.currentThread().getName() + "=====>>>>>使用fixedRate  {}" + "每隔五秒执行一次 时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }

    /**fixedDelay:上一次执行完毕时间点之后5秒再执行*/
    @Scheduled(fixedDelay = 5000)
    public void run2() throws InterruptedException {
        Thread.sleep(7000);
        System.out.println("SpringTaskExample " + Thread.currentThread().getName() + "=====>>>>>使用fixedDelay  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }

    /**第一次延迟2秒后执行，之后按fixedDelay的规则每5秒执行一次*/
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void run3() {
        System.out.println("SpringTaskExample " + Thread.currentThread().getName() + "=====>>>>>使用initialDelay  {}" + "时间：" + new Date() + "-" + (System.currentTimeMillis() / 1000));
    }
}
