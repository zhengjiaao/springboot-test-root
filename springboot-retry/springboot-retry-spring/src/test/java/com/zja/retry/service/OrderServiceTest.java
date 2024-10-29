package com.zja.retry.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 10:36
 */
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void findOrder1() throws Exception {
        orderService.findOrder1(1); // 会一直等待重试结束
    }

    @Test
    public void findOrder2() throws Exception {
        String order2 = orderService.findOrder2(1);// 会一直等待重试结束
        System.out.println(order2);
    }

}
