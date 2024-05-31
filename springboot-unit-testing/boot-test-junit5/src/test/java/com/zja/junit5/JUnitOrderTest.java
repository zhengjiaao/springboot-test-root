package com.zja.junit5;

import org.junit.jupiter.api.*;

/**
 * @Author: zhengja
 * @Date: 2024-05-31 16:17
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 支持 jupiter 测试方法按指定 @Order(1) 顺序执行
public class JUnitOrderTest {

    @Test
    @Order(1)
    public void testGetUserById() {
        System.out.println("testGetUserById");
    }

    @Test
    @Order(2)
    public void testUpdateUser() {
        System.out.println("testUpdateUser");
    }

    @Test
    @Order(3)
    @Disabled
    public void testDeleteUser() {
        System.out.println("testDeleteUser");
    }
}
