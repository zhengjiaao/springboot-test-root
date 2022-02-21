/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:43
 * @Since:
 */
package com.zja.juc.order;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch是计数器，它有两个方法，一个是await()，这是阻塞，一个是countDown()，这是计数-1功能，当计数为0的时候，await阻塞的代码才往下执行。
 */
public class CountDownLatchDemo {

    static class MyThread implements Runnable {
        CountDownLatch startCountDown;
        CountDownLatch endCountDown;

        public MyThread(CountDownLatch startCountDown, CountDownLatch endCountDown) {
            this.startCountDown = startCountDown;
            this.endCountDown = endCountDown;
        }

        @Override
        public void run() {
            //阻塞
            if (startCountDown != null) {
                try {
                    startCountDown.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //执行自己的业务逻辑
            System.out.println(Thread.currentThread().getName() + "开始执行");
            //todo 执行业务逻辑
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (endCountDown != null) {
                endCountDown.countDown();
            }
        }
    }

    /**
     * CountDownLatch 它可以让一个线程阻塞，也可以让多个线程阻塞，所以它是共享锁。可以允许多个线程同时抢占到锁，然后等到计数器归零的时候，同时唤醒。
     * state记录计数器
     * countDown的时候，实际上就是 state--
     */
    public static void main(String[] args) {
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        Thread t1 = new Thread(new MyThread(null, countDownLatch1), "第一个线程");
        Thread t2 = new Thread(new MyThread(countDownLatch1, countDownLatch2), "第二个线程");
        Thread t3 = new Thread(new MyThread(countDownLatch2, null), "第三个线程");
        //打乱顺序执行
        t3.start();
        t2.start();
        t1.start();
    }

}
