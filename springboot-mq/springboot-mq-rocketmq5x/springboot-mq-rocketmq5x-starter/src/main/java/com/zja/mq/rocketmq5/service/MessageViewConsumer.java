package com.zja.mq.rocketmq5.service;

import com.zja.mq.rocketmq5.model.MessageView;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 16:55
 */
@Service
@RocketMQMessageListener(
        topic = "test-topic-view", // 主题
        consumerGroup = "test-group-view") // 消费者组
public class MessageViewConsumer implements RocketMQListener<MessageView> {
    @Override
    public void onMessage(MessageView messageView) {
        System.out.println("MessageViewConsumer receive message：" + messageView);
    }
}