package com.zja.jar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 16:34
 */
// @SpringBootApplication // 多个冲突
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    public void test(){
        System.out.println(orderService.getOrder());
    }

}
