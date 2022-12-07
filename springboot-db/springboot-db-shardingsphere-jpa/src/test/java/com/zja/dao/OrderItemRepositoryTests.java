/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-06 15:48
 * @Since:
 */
package com.zja.dao;

import com.zja.entity.OrderEntity;
import com.zja.entity.OrderItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@SpringBootTest
public class OrderItemRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    @Test
    public void save() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            int userId = Math.abs(random.nextInt());

            OrderEntity order = new OrderEntity();
            order.setAddressId(i);
            order.setUserId(userId);
            order.setCreator("user-" + i);
            order.setUpdater(order.getCreator());
            System.out.println(order);
            orderRepository.save(order);

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setUserId(userId);
            System.out.println(orderItem);
            orderItemRepository.save(orderItem);
        });

        List<OrderItemEntity> entityList = orderItemRepository.findAll();
        entityList.forEach(s -> System.out.println(s));
    }
}
