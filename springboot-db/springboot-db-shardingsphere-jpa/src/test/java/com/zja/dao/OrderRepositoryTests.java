/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-05 15:26
 * @Since:
 */
package com.zja.dao;

import com.zja.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void save() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            OrderEntity order = new OrderEntity();
            order.setAddressId(i);
            order.setUserId(Math.abs(random.nextInt()));
            order.setCreator("user-" + i);
            order.setUpdater(order.getCreator());
            System.out.println(order);
            orderRepository.save(order);
        });

        List<OrderEntity> entityList = orderRepository.findAll();
        entityList.forEach(s -> System.out.println(s));
    }
}
