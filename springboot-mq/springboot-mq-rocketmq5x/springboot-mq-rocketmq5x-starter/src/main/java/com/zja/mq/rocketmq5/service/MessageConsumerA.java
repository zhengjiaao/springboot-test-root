package com.zja.mq.rocketmq5.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 消费者：使用 @RocketMQMessageListener 注解来订阅主题并监听消息的到达，处理消息的消费逻辑。
 *
 * @Author: zhengja
 * @Date: 2025-01-13 11:24
 */
@Service
@RocketMQMessageListener(consumerGroup = "springboot-consumer-group", topic = "test-topic")
public class MessageConsumerA implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("MessageConsumer A receive message：" + message);
    }
}
