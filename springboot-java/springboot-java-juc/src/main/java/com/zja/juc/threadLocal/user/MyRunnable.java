package com.zja.juc.threadLocal.user;

/**
 * @author: zhengja
 * @since: 2024/03/27 16:48
 */
public class MyRunnable implements Runnable {
    private User user;

    public MyRunnable(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        // 在当前线程中设置用户信息
        UserContext.setUser(user);

        // 获取当前线程的用户信息
        User currentUser = UserContext.getUser();
        System.out.println("Thread: " + Thread.currentThread().getName() + ", User: " + currentUser.getUsername());

        // 清除用户信息
        UserContext.clear();
    }
}