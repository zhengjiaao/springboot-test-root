package com.zja.mq.rocketmq5.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 13:25
 */
@Service
public class MessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步发送消息：Producer 发出一条消息后，会在收到 MQ 返回的 ACK 之后才发下一条消息。
     * 该方式的消息可靠性最高，但消息发送效率太低。
     *
     * @param topic   订阅主题 Topic
     * @param tags    订阅 tags
     * @param message 消息内容
     */
    public void syncSend(String topic, String tags, String message) {
        // springboot不支持使用header传递tags，根据要求，需要在topic后进行拼接 formats: `topicName:tags`，不拼接标识无tag
        String destination = StringUtils.isBlank(tags) ? topic : topic + ":" + tags;
        SendResult sendResult = rocketMQTemplate.syncSend(destination,
                MessageBuilder.withPayload(message)
                        .setHeader(MessageConst.PROPERTY_KEYS, "yourKey")   // 指定业务key
                        .build());
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", topic, sendResult);

        // 发送消息，消息体可以是自定义对象，也可以是 Message 对象（org.springframework.messaging包中）
        // SendResult sendResult = rocketMQTemplate.syncSend(destination, message);
        /*------------------------------------------------------------------------*/
        // rocketMQTemplate.syncSend(destination, MessageBuilder.withPayload(message).build())
    }

    /**
     * 发送单向消息：Producer 仅负责发送消息，不等待、不处理 MQ 的 ACK。
     * 该发送方式时 MQ 也不返回 ACK。该方式的消息发送效率最高，但消息可靠性较差。
     */
    public void sendOneWayMessage(String topic, String message) {
        rocketMQTemplate.sendOneWay(topic, message);
        System.out.println("One way message sent: " + message);
    }

    /**
     * 异步类型消息：Producer 发出消息后无需等待 MQ 返回 ACK，直接发送下一条消息。
     * 该方式的消息可靠性可以得到保障，消息发送效率也可以。
     */
    public void asyncSendMessage(String topic, String message) {
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("Async message sent: " + message);
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("Async message error: " + e);
            }
        });
        System.out.println("Message sent: " + message);
    }

    /**
     * 发送顺序消息：严格按照消息的发送顺序进行消费的消息(FIFO)。
     */
    public void sendOrderlyMessage(String topic, String message, String shardingKey) {
        for (int i = 0; i < 10; i++) {
            String orderlyMessage = message + i;
            rocketMQTemplate.syncSendOrderly(topic, orderlyMessage, shardingKey);
            System.out.println("Orderly message sent: " + orderlyMessage + " with shardingKey: " + shardingKey);
        }
    }

    /**
     * 发送延迟消息
     * 延迟级别：1  2  3   4   5  6  7  8  9  10 11 12 13 14  15  16  17 18
     * 延迟时间：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * <p>
     * 当消息写入到Broker后，在指定的时长后才可被消费处理的消息，称为延时消息。
     * 延时消息的延迟时长不支持随意时长的延迟，是通过特定的延迟等级来指定的。
     * 延时等级默认有18个，可以在broker.conf中增加配置，然后重启broker
     * </p>
     */
    public void sendDelayedMessage(String topic, String message, int delayLevel) {
        if (delayLevel < 1 || delayLevel > 18) {
            throw new IllegalArgumentException("Delay level must be between 1 and 18");
        }
        Message<?> msg = MessageBuilder.withPayload(message).build();
        rocketMQTemplate.syncSend(topic, msg, 3000, delayLevel);
        System.out.println("Delayed message sent: " + message + " with delayLevel: " + delayLevel);
    }

    // RocketMQ 还支持事务消息、批量消息、消息过滤等
    public void sendTransactionMessage(String topic, String message) {
        // 发送事务消息
        rocketMQTemplate.sendMessageInTransaction(topic, MessageBuilder.withPayload(message).build(), null);
        System.out.println("Transaction message sent: " + message);
    }

    public void sendBatchMessage(String topic, List<String> messages) {
        // 发送批量消息
        List<Message<?>> messageList = new ArrayList<>();
        for (String msg : messages) {
            messageList.add(MessageBuilder.withPayload(msg).build());
        }
        rocketMQTemplate.syncSend(topic, messageList);
        System.out.println("Batch messages sent: " + messages);
    }

    public void sendFilterMessage(String topic, String message, String filterTag) {
        // 发送过滤消息
        Message<?> msg = MessageBuilder.withPayload(message)
                .setHeader(RocketMQHeaders.TAGS, filterTag)
                .build();
        rocketMQTemplate.syncSend(topic, msg);
        System.out.println("Filter message sent: " + message + " with tag: " + filterTag);
    }
}
