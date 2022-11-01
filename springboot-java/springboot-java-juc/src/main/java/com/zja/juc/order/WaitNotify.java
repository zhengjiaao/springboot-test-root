/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:28
 * @Since:
 */
package com.zja.juc.order;

public class WaitNotify {
    private static Object myLock1 = new Object();
    private static Object myLock2 = new Object();

    static class MyThread implements Runnable {

        String name;
        Object startLock;
        Object endLock;

        public MyThread(String name, Object startLock, Object endLock) {
            this.name = name;
            this.startLock = startLock;
            this.endLock = endLock;
        }

        @Override
        public void run() {
            if (startLock != null) {
                synchronized (startLock) {
                    //阻塞
                    try {
                        //wait() 让当前线程进入等待状态，同时，wait()也会让当前线程释放它所持有的锁。
                        //“直到其他线程调用此对象的 notify() 方法或 notifyAll() 方法”，当前线程被唤醒(进入“就绪状态”)
                        startLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //继续往下执行
            System.out.println(name + "开始执行");
            //todo 执行业务逻辑
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (endLock != null) {
                synchronized (endLock) {
                    //唤醒
                    //notify()和notifyAll(): 作用则是唤醒当前对象上的等待线程；notify()是唤醒单个线程，而notifyAll()是唤醒所有的线程。
                    endLock.notify();
                }
            }
        }
    }

    /**
     * 原理：
     * 线程t1、t2共用一把锁myLock1，t2先wait阻塞，等待t1执行完毕notify通知t2继续往下执行，
     * 线程t2、t3共用一把锁myLock2，t3先wait阻塞，等待t2执行完毕notify通知t3继续往下执行。
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(new MyThread("第一个线程", null, myLock1));
        Thread t2 = new Thread(new MyThread("第二个线程", myLock1, myLock2));
        Thread t3 = new Thread(new MyThread("第三个线程", myLock2, null));
        //打乱顺序执行
        t3.start();
        t1.start();
        t2.start();
    }
}
