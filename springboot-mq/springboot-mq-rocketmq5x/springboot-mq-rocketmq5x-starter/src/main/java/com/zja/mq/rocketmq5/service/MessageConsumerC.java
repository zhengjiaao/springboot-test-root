package com.zja.mq.rocketmq5.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 13:26
 */
@Service
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group}",  // 消费组，格式：group名称
        topic = "${rocketmq.consumer.topic}"  // 需要使用topic全称，所以进行topic名称的拼接，也可以自己设置  格式：topic名称
)
public class MessageConsumerC implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("MessageConsumer C receive message：" + message);
    }
}