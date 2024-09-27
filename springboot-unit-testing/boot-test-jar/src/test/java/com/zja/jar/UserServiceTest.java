package com.zja.jar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: zhengja
 * @Date: 2024-09-20 14:36
 */
@SpringBootApplication
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testGetUserName(){
        String userName = userService.getUserName();
        System.out.println(userName);
    }

}
