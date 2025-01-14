package com.zja.mq.rocketmq5.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 11:24
 */
@Service
@RocketMQMessageListener(consumerGroup = "springboot-consumer-group", topic = "test-topic")
public class MessageConsumerB implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("MessageConsumer B receive message：" + message);
    }
}
