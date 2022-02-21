/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:48
 * @Since:
 */
package com.zja.juc.order;

import java.util.concurrent.CompletableFuture;

/**
 * Java 8 CompletableFuture
 */
public class CompletableFutureDemo {
    static class MyThread implements Runnable {
        @Override
        public void run() {
            System.out.println("执行 : " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyThread(), "线程1");
        Thread t2 = new Thread(new MyThread(), "线程2");
        Thread t3 = new Thread(new MyThread(), "线程3");
        CompletableFuture.runAsync(t1::start).thenRun(t2::start).thenRun(t3::start);
    }
}
