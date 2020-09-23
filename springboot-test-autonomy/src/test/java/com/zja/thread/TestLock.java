package com.zja.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/5 13:04
 */
public class TestLock {

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方

    public static void main(String[] args) {
        final TestLock testLock = new TestLock();

        new Thread(){
            public void run() {
                testLock.insert(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                testLock.insert(Thread.currentThread());
            };
        }.start();
    }

    public void insert(Thread thread){
        if (lock.tryLock()){
            try {
                System.out.println(thread.getName() + "得到了锁");
                for (int i = 0; i < 5; i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                System.out.println(thread.getName() + "释放了锁");
                lock.unlock();
            }
        }else {
            System.out.println(thread.getName()+"获取锁失败");
        }
    }

    /**
     * lockInterruptibly()响应中断的使用方法
     * 运行之后，发现myThread2能够被正确中断
     */
    @Test
    public void testLockInterruptibly(){
        TestLock testLock = new TestLock();

        MyThread myThread = new MyThread(testLock);
        MyThread myThread2 = new MyThread(testLock);
        myThread.start();
        myThread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myThread2.interrupt();
    }

    public void insert2(Thread thread) throws InterruptedException {
        //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        lock.lockInterruptibly();

        try {
            System.out.println(thread.getName()+"得到了锁");
            long startTime =System.currentTimeMillis();
            for ( ; ;){
                if (System.currentTimeMillis() - startTime >= Integer.MAX_VALUE)
                    break;
                //插入数据
            }
        }finally {
            System.out.println(Thread.currentThread().getName()+"执行finally");
            lock.unlock();
            System.out.println(thread.getName()+"释放了锁");
        }
    }

    class MyThread extends Thread {
        private TestLock testLock = null;

        public MyThread(TestLock testLock) {
            this.testLock = testLock;
        }

        @Override
        public void run() {

            try {
                testLock.insert2(Thread.currentThread());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "被中断");
            }
        }
    }
}
