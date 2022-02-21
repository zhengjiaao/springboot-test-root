/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 14:51
 * @Since:
 */
package com.zja.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {

    /**
     * cf1就是执行一个任务，用的是默认ForkJoinPool的线程池，不带返回值，cf1.get()是阻塞获取值，因为不带返回值，所以获取的是null。、
     * cf2是执行一个带返回值的任务，里面就干一件事return hello world，此时主线程可以继续往下执行做其他事情，待任务执行完以后，thenAccept方法接收到返回的hello world，然后打印出来。
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture cf1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ":异步执行一个任务");
        });
        //通过阻塞获取执行结果
        System.out.println("cf1:"+cf1.get());

        CompletableFuture cf2 = CompletableFuture.supplyAsync(() -> "Hello World").thenAccept(result -> {
            System.out.println(result);
        });

        //继续做其他事情
        //...
    }
}
