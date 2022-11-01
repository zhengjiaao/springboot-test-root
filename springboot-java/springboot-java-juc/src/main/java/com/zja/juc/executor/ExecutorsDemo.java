/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 14:19
 * @Since:
 */
package com.zja.juc.executor;

import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * Executors是一个工具类
 *
 * 创建线程的两种方式:
 * 第一种是通过Executors提供的工厂方法来实现，有下面四种方式.
 * 第二种是通过构造方法来实现.
 */
public class ExecutorsDemo {

    //创建线程 工厂方法的四种方式
    Executor executor1 = Executors.newFixedThreadPool(10);
    Executor executor2 = Executors.newSingleThreadExecutor();
    Executor executor3 = Executors.newCachedThreadPool();
    Executor executor4 = Executors.newScheduledThreadPool(10);

    //推荐
    //创建线程 构造方法来实现
    private static ExecutorService executor5 = new ThreadPoolExecutor(1, //核心线程数量
            1,      //最大线程数
            0L,        //超时时间,超出核心线程数量以外的线程空余存活时间
            TimeUnit.MILLISECONDS,  //存活时间单位
            new ArrayBlockingQueue<Runnable>(2),    //保存执行任务的队列
            Executors.defaultThreadFactory(),       //创建新线程使用的工厂
            new ThreadPoolExecutor.AbortPolicy()    //当任务无法执行的时候的处理方式
    );

    @SneakyThrows
    public static void main(String[] args) {
        //无返回值
        executor5.execute(() -> System.out.println("jack"));
        //带返回值
        String jack = executor5.submit(() -> {
            return "jack2";
        }).get();

        System.out.println(jack);
    }
}
