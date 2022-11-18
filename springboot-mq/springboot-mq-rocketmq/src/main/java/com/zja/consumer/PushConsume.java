/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 11:28
 * @Since:
 */
package com.zja.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 推送 消费
 */
public class PushConsume {

    /**
     * 推送 获取消息
     */
    public static class Consumer {
        public static void main(String[] args) throws InterruptedException, MQClientException {
            // 初始化consumer，并设置consumer group name
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");

            // 设置NameServer地址
            consumer.setNamesrvAddr("localhost:9876");
            //订阅一个或多个topic，并指定tag过滤条件，这里指定*表示接收所有tag的消息
            consumer.subscribe("TopicTest", "*");
            //注册回调接口来处理从Broker中收到的消息
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                    // 返回消息消费状态，ConsumeConcurrentlyStatus.CONSUME_SUCCESS为消费成功
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            // 启动Consumer
            consumer.start();
            System.out.printf("Consumer Started.%n");
        }
    }


}
