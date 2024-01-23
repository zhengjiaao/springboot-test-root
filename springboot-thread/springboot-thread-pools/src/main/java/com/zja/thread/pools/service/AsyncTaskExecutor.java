package com.zja.thread.pools.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: zhengja
 * @since: 2024/01/22 17:38
 */
@Service
public class AsyncTaskExecutor {

    @Async // 默认使用线程池：SimpleAsyncTaskExecutor：不使用线程池，每个任务都会创建一个新的线程。这可能会导致线程数量的增长。
    // @Async("taskExecutor")
    public void add(String name) throws InterruptedException {
        // 异步执行的方法体
        System.out.println("currentThread=" + Thread.currentThread().getName() + " ,currentThreadId=" + Thread.currentThread().getId() + " ,name=" + name);
        Thread.sleep(1000);
    }

    @Async("taskExecutor2")
    public void add2(String name) throws InterruptedException {
        // 异步执行的方法体
        System.out.println("currentThread=" + Thread.currentThread().getName() + " ,currentThreadId=" + Thread.currentThread().getId() + " ,name=" + name);
        Thread.sleep(1000);
    }


    @Async("taskExecutor3")
    public void add3(String name) throws InterruptedException {
        // 异步执行的方法体
        System.out.println("currentThread=" + Thread.currentThread().getName() + " ,currentThreadId=" + Thread.currentThread().getId() + " ,name=" + name);
        Thread.sleep(1000);
    }

    @Async("taskExecutor4")
    public void add4(String name) throws InterruptedException {
        // 异步执行的方法体
        System.out.println("currentThread=" + Thread.currentThread().getName() + " ,currentThreadId=" + Thread.currentThread().getId() + " ,name=" + name);
        Thread.sleep(1000);
    }

    @Async("taskExecutor5")
    public void add5(String name) throws InterruptedException {
        // 异步执行的方法体
        System.out.println("currentThread=" + Thread.currentThread().getName() + " ,currentThreadId=" + Thread.currentThread().getId() + " ,name=" + name);
        Thread.sleep(1000);
    }

    // 自定义的饱和策略
    @Async("taskExecutor6")
    public void add6(String name) throws InterruptedException {
        // 异步执行的方法体
        System.out.println("currentThread=" + Thread.currentThread().getName() + " ,currentThreadId=" + Thread.currentThread().getId() + " ,name=" + name);
        Thread.sleep(1000);
    }
}
