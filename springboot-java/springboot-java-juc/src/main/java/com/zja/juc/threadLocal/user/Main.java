package com.zja.juc.threadLocal.user;

/**
 * @author: zhengja
 * @since: 2024/03/27 16:48
 */
public class Main {
    public static void main(String[] args) {
        User user1 = new User("Alice");
        User user2 = new User("Bob");

        // 创建两个线程，并在每个线程中设置并获取用户信息
        Thread thread1 = new Thread(new MyRunnable(user1));
        Thread thread2 = new Thread(new MyRunnable(user2));

        thread1.start(); // 输出: Thread: Thread-0, User: Alice
        thread2.start(); // 输出: Thread: Thread-1, User: Bob
    }
}