package com.zja.retry.aop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 10:03
 */
@SpringBootTest
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @Test
    public void findUser1() throws Exception {
        userService.findUser1(2); // 会一直等待重试结束
    }

    @Test
    public void findUser2() throws Exception {
        String user2 = userService.findUser2(3);// 会一直等待重试结束
        System.out.println(user2);
    }
}
