package com.zja.mq.rocketmq5.service;

import com.zja.mq.rocketmq5.model.MessageView;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 17:20
 */
@Service
public class MessageViewProducer {

    @Value("${rocketmq.producer.topic:test-topic-view}")
    private String topic;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * syncSend：同步发送消息并返回 SendResult 对象，该对象包含消息发送的结果信息，如消息 ID、队列偏移量等。这有助于开发者确认消息是否成功发送。
     * <p>
     * 使用场景：syncSend：当需要确保消息成功发送并且可能需要根据发送结果进行后续处理时使用。
     * 消息转换：syncSend：需要手动指定消息体（可以是自定义对象或 Message 对象），并且可以选择是否使用 MessageBuilder 来构建消息。
     */
    public void sendMessage(MessageView message) {
        // 以下两种实现效果是一样的，都是转为json字符串发送消息到mq中
        // 发送消息，消息体可以是自定义对象，也可以是 Message 对象（org.springframework.messaging包中）
        SendResult sendResult1 = rocketMQTemplate.syncSend(topic, message); // 返回结果
        /*------------------------------------------------------------------------*/
        SendResult sendResult2 = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build());
    }

    /**
     * convertAndSend：发送消息但不返回任何结果，适用于不需要关心消息发送结果的场景。
     * <p>
     * 使用场景：convertAndSend：当只需要简单地发送消息而不关心发送结果时使用，适合高吞吐量场景以减少性能开销。
     * 消息转换：convertAndSend：自动将消息体转换为字符串或字节数组，默认使用 MappingJackson2MessageConverter 进行序列化，简化了消息构建过程。
     */
    public void sendMessageV2(MessageView message) {
        // 无返回值,消息发送效果与上述syncSend方法一致
        rocketMQTemplate.convertAndSend(topic, message);
    }

}
