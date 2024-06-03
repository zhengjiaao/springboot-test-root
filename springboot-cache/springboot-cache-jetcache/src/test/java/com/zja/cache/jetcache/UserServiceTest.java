package com.zja.cache.jetcache;

import com.zja.cache.jetcache.model.User;
import com.zja.cache.jetcache.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * @Author: zhengja
 * @Date: 2024-05-31 10:12
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 支持 jupiter 测试方法按指定 @Order(1) 顺序执行
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    public void testGetUserById() {
        User user = userService.getUserById("111");
        System.out.println(user);
    }

    @Test
    @Order(2)
    public void testUpdateUser() {
        // 准备数据
        User user = new User(); // 创建要更新的用户对象
        user.setUserId("111");
        user.setName("李四");
        user.setAge(18);

        // 更新用户
        userService.updateUser(user);

        // 获取用户
        User user2 = userService.getUserById("111");
        System.out.println(user2);
    }

    @Test
    @Order(3)
    // @Disabled
    public void testDeleteUser() {
        // 准备数据
        String userId = "111";

        // 执行测试
        userService.deleteUser(userId);
    }

    // 对summaryOfToday方法的测试需要模拟更复杂的逻辑，包括缓存刷新、穿透保护等，这可能涉及到更多的验证和模拟操作
    // 这里仅给出一个简单的测试框架，实际实现需要根据具体业务逻辑进行扩展
    @Test
    @Order(4)
    public void testSummaryOfToday() throws InterruptedException {
        // 准备数据
        String categoryId = "category1";

        // 执行测试
        BigDecimal actualResult = userService.summaryOfToday(categoryId);
        System.out.println(actualResult);

        Thread.sleep(2 * 60 * 1000);
    }


    // 更多复杂示例测试

    // @Test
    // @Order(5)
    // public void testGetAll() {
    //     // 准备数据
    //     // 执行测试
    //     userService.getAll();
    // }

}
