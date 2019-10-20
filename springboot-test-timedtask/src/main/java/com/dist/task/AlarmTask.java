package com.dist.task;

import org.springframework.stereotype.Component;

import java.util.Date;

/**3.Spring Task   @Scheduled任务类 静态创建定时任务
 * 使同一个线程中串行执行，如果只有一个定时任务，这样做肯定没问题，当定时任务增多，如果一个任务卡死，会导致其他任务也无法执行
 *
 * fixedRate：定义一个按一定频率执行的定时任务
 * fixedDelay：定义一个按一定频率执行的定时任务，与上面不同的是，改属性可以配合initialDelay， 定义该任务延迟执行时间。
 * cron：通过表达式来配置任务执行时间
 *
 * @author zhengja@dist.com.cn
 * @data 2019/8/13 14:15
 */
@Component
//@EnableScheduling  //开启定时任务，项目启动自动执行定时任务
//@Async("taskExecutor")  //使用自定义线程池：定义异步任务-并行定时任务执行，默认串行的定时任务
//@Async  //使用默认线程池
public class AlarmTask {

    /**默认是fixedDelay 上一次执行完毕时间后执行下一轮*/
    //@Scheduled(cron = "0/5 * * * * *")  //cron 表示 在指定时间执行
    //@Scheduled(cron = "0 9 15 * * ?")   //每天15点9分执行一次  ，具体参考 HELP.md 格式
    public void run() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用cron  {}"+"时间："+new Date()+"-"+(System.currentTimeMillis()/1000));
    }

    /**fixedRate:上一次开始执行时间点之后5秒再执行*/
    //@Scheduled(fixedRate = 5000)  //fixedRate 表示 每隔 5000 毫秒执行一次
    public void run1() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedRate  {}"+"每隔五秒执行一次 时间："+new Date()+"-"+(System.currentTimeMillis()/1000));
    }

    /**fixedDelay:上一次执行完毕时间点之后5秒再执行*/
    //@Scheduled(fixedDelay = 5000)
    public void run2() throws InterruptedException {
        Thread.sleep(7000);
        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedDelay  {}"+"时间："+new Date()+"-"+(System.currentTimeMillis()/1000));
    }

    /**第一次延迟2秒后执行，之后按fixedDelay的规则每5秒执行一次*/
    //@Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void run3(){
        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用initialDelay  {}"+"时间："+new Date()+"-"+(System.currentTimeMillis()/1000));
    }
}
