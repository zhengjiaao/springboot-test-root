/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 11:23
 * @Since:
 */
package com.zja.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * 延迟消息发送
 */
public class DelayedMessageSending {

    /**
     * 延迟消息
     */
    public static class ScheduledMessageProducer {
        public static void main(String[] args) throws Exception {
            // Instantiate a producer to send scheduled messages
            DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");
            // Launch producer
            producer.start();
            int totalMessagesToSend = 100;
            for (int i = 0; i < totalMessagesToSend; i++) {
                Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes());
                // This message will be delivered to consumer 10 seconds later.
                message.setDelayTimeLevel(3);
                // Send the message
                producer.send(message);
            }

            // Shutdown producer after use.
            producer.shutdown();
        }

    }


}
