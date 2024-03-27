package com.zja.juc.countDownLatch.service;

import java.util.concurrent.CountDownLatch;

/**
 * @author: zhengja
 * @since: 2024/03/19 13:17
 */
public class UserServiceTest {

    private static final UserService userService = new UserService();

    private static final CountDownLatch countDown = new CountDownLatch(1);


    public static void main(String[] args) {
        System.out.println("begin");
        for (int i = 0; i < 10000; i++) {
            User user = new User();
            user.setId((long) i);
            user.setName("addUser");
            user.setAddress("address-test");

            new Thread(() -> {
                long begin = System.currentTimeMillis();
                try {
                    countDown.await();

                    System.out.println(userService.addUser(user));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    long end = System.currentTimeMillis();
                    System.out.println("executeTime: " + (end - begin));
                }
            }).start();
        }

        countDown.countDown();
        System.out.println("end");
    }

}
