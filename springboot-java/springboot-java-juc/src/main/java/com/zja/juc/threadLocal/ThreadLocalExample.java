package com.zja.juc.threadLocal;

/**
 * ThreadLocal 是一个线程局部变量，它提供了一种在多线程环境下保持变量的独立副本的机制。每个线程都拥有自己的变量副本，彼此之间互不干扰。
 *
 * @author: zhengja
 * @since: 2024/03/27 16:42
 */
public class ThreadLocalExample {
    private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        // 创建并启动两个线程
        Thread thread1 = new Thread(() -> {
            // 设置线程本地变量的值
            threadLocal.set(42);
            // 访问线程本地变量
            int value = threadLocal.get();
            System.out.println("线程1: " + value); // 输出: 线程1: 42
        });

        Thread thread2 = new Thread(() -> {
            // 设置线程本地变量的值
            threadLocal.set(30);
            // 访问线程本地变量
            int value = threadLocal.get();
            System.out.println("线程2: " + value); // 输出: 线程2: null
        });

        thread1.start();
        thread2.start();

        Thread.sleep(1000 * 5);
    }
}
