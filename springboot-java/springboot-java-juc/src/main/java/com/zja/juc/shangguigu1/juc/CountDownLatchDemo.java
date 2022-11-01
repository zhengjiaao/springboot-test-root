package com.zja.juc.shangguigu1.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 输出结果：
 * 1 号同学离开了教室
 * 6 号同学离开了教室
 * 5 号同学离开了教室
 * 4 号同学离开了教室
 * 2 号同学离开了教室
 * 3 号同学离开了教室
 * main 班长锁门走人了
 */
public class CountDownLatchDemo {
    //6个同学陆续离开教室之后，班长锁门
    public static void main(String[] args) throws InterruptedException {

        //创建CountDownLatch对象，设置初始值
        CountDownLatch countDownLatch = new CountDownLatch(6);

        //6个同学陆续离开教室之后
        for (int i = 1; i <=6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 号同学离开了教室");

                //计数  -1
                countDownLatch.countDown();

            },String.valueOf(i)).start();
        }

        //等待
        countDownLatch.await();

        System.out.println(Thread.currentThread().getName()+" 班长锁门走人了");
    }
}
