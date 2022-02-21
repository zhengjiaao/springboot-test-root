/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 14:30
 * @Since:
 */
package com.zja.juc.executor;

import lombok.SneakyThrows;

import java.util.concurrent.*;

public class ThreadPoolExecutorDemo {
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
