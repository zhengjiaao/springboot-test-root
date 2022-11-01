package com.zja.thread;


import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/18 16:59
 */
public class CreateThread {
    //start() 作用是启动相应的线程,调用结束并不表示相应线程已经开始运行，这个线程可能稍后运行，也可能永远也不会运行
    //run()相当于线程的任务处理逻辑的入口方法，它由Java虚拟机在运行相应线程时直接调用，而不是由应用代码进行调用

    /****************************java创建线程的四种方式*******************************/

    //1.继承Thread类实现多线程

    /**
     * run()为线程类的核心方法，相当于主线程的main方法，是每个线程的入口
     * a.一个线程调用 两次start()方法将会抛出线程状态异常，也就是的start()只可以被调用一次
     * b.native生明的方法只有方法名，没有方法体。是本地方法，不是抽象方法，而是调用c语言方法,
     * registerNative()方法包含了所有与线程相关的操作系统方法
     * c. run()方法是由jvm创建完本地操作系统级线程后回调的方法，不可以手动调用（否则就是普通方法）
     * <p>
     * 开启线程：
     * MyThread mThread1=new MyThread();
     * MyThread mThread2=new MyThread();
     * mThread1.start();
     * mThread2.start();
     */
    class MyThread extends Thread {
        public MyThread() {
            System.out.println("MyThread");
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                System.out.println(Thread.currentThread() + ":" + i);
            }
        }
    }

    //1.继承Thread类实现多线程
    @Test
    public void test1() {
        MyThread mThread1 = new MyThread();
        MyThread mThread2 = new MyThread();
        mThread1.start();
        mThread2.start();
        mThread1.run();
    }


    //2、覆写Runnable()接口实现多线程，而后同样覆写run().推荐此方式

    /**
     * a.覆写Runnable接口实现多线程可以避免单继承局限
     * b.当子类实现Runnable接口，此时子类和Thread的代理模式（子类负责真是业务的操作，thread负责资源调度与线程创建辅助真实业务
     * 线程启动：
     * MyThread Thread1=new MyThread();
     * Thread mThread1=new Thread(Thread1,"线程1");
     * Thread mThread2=new Thread(Thread1,"线程2");
     * mThread1.start();
     * mThread2.start();
     */
    class MyThread2 implements Runnable {
        public int count = 20;

        @Override
        public void run() {
            while (count > 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "-当前剩余票数:" + count--);
            }
        }
    }

    //2.覆写Runnable()接口实现多线程，而后同样覆写run().推荐此方式
    @Test
    public void test2() {
        MyThread2 myThread2 = new MyThread2();
        Thread mThread1 = new Thread(myThread2, "线程1");
        Thread mThread2 = new Thread(myThread2, "线程2");
        mThread1.start();
        mThread2.start();
    }

    //继承Thread和实现Runnable接口的区别
    //a.实现Runnable接口避免多继承局限
    // b.实现Runnable()可以更好的体现共享的概念


    //3、覆写Callable接口实现多线程（JDK1.5）

    /**
     * a.核心方法叫call()方法，有返回值
     * b.有返回值
     * 启动方式：
     * Callable<String> callable  =new MyThread3();
     * FutureTask <String>futureTask=new FutureTask<>(callable);
     */
    class MyThread3 implements Callable<String> {
        private int count = 20;

        @Override
        public String call() throws Exception {
            for (int i = count; i > 0; i--) {
                //Thread.yield();
                System.out.println(Thread.currentThread().getName() + "当前票数：" + i);
            }
            return "sale out";
        }
    }

    @Test
    public void test3(){
        Callable<String> callable  =new MyThread3();
        FutureTask<String> futureTask=new FutureTask<>(callable);
        Thread mThread=new Thread(futureTask);
        Thread mThread2=new Thread(futureTask);
        Thread mThread3=new Thread(futureTask);
//		mThread.setName("hhh");
        mThread.start();
        mThread2.start();
        mThread3.start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /*****************4、通过线程池启动多线程*********************/
    //通过Executor 的工具类可以创建三种类型的普通线程池：

    //4.1 FixThreadPool(int n); 固定大小的线程池

    /**
     * 使用于为了满足资源管理需求而需要限制当前线程数量的场合。使用于负载比较重的服务器
     */
    public void TestFixThreadPool() {

        //会提示手动创建线程池会更好-所以，不推荐这样用
        ExecutorService ex = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            ex.submit(new Runnable() {

                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        System.out.println(Thread.currentThread().getName() + j);
                    }

                }
            });
        }
        ex.shutdown();
    }

    //4.2 SingleThreadPoolExecutor :单线程池

    /**
     * 需要保证顺序执行各个任务的场景
     */
    public void TestSingleThreadPoolExecutor() {
        ExecutorService ex = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        System.out.println(Thread.currentThread().getName() + j);
                    }

                }
            });
        }
        ex.shutdown();
    }

    //4.3 CashedThreadPool(); 缓存线程池
    public void TestCashedThreadPool() {
        ExecutorService ex = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            ex.submit(new Runnable() {

                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        System.out.println(Thread.currentThread().getName() + j);
                    }
                }
            });
        }
        ex.shutdown();
    }
}
