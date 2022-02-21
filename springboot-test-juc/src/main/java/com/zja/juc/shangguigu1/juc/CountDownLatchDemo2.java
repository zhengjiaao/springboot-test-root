/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:02
 * @Since:
 */
package com.zja.juc.shangguigu1.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 输出结果：
 * 线程一等待线程二以及线程三执行完成之后再执行......
 * 线程二执行完成，执行countDown
 * 线程三执行完成，执行countDown
 * 线程一执行完了
 */
public class CountDownLatchDemo2 {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                Thread.currentThread().setName("线程一");
                // 等待一会
                System.out.println(Thread.currentThread().getName() + "等待线程二以及线程三执行完成之后再执行......");
                //await等待，其实就是将当前的线程进行挂起，或者就是等待刚才设置的state的值为0之后再尝试出队获取锁再执行
                countDownLatch.await();
                System.out.println(Thread.currentThread().getName() + "执行完了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            Thread.currentThread().setName("线程二");
            System.out.println(Thread.currentThread().getName() + "执行完成，执行countDown");
            //减一
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            Thread.currentThread().setName("线程三");
            System.out.println(Thread.currentThread().getName() + "执行完成，执行countDown");
            countDownLatch.countDown();
        }).start();
    }
}
