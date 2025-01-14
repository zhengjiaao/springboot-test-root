package com.zja.mq.rocketmq5.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 13:26
 */
@Service
@RocketMQMessageListener(
        consumerGroup = "${rocketmq.consumer.group}",
        topic = "${rocketmq.consumer.topic}",
        selectorType = SelectorType.TAG,
        selectorExpression = "dTag"
)
public class MessageConsumerDTag implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("MessageConsumerDTag D receive message：" + message);
    }
}