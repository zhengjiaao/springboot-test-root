package com.zja.log.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: zhengja
 * @since: 2024/01/23 15:45
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void add_test() {
        userService.add(); // 测试日志显示级别
    }

    @Test
    public void exceptionLog_test() {
        userService.exceptionLog(); // 测试异常日志输出
    }
}
