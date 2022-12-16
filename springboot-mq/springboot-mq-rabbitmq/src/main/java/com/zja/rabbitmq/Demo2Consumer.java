/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-15 13:15
 * @Since:
 */
package com.zja.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Demo2Consumer extends BaseConfig {

    // 交换机
    private static final String EXCHANGE_NAME = "SIMPLE_EXCHANGE";
    // 消息队列
    private static final String QUEUE_NAME = "SIMPLE_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建消费者，并接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override // 回调函数
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 接收到的消息是字节数组形式，需要转成字符串
                String msg = new String(body, "UTF-8");
                System.out.println("Received message : '" + msg + "'");

                if (msg.contains("拒收")) {
                    // 拒绝消息
                    // requeue：是否重新入队列，true：是；false：直接丢弃，相当于告诉队列可以直接删除掉
                    // TODO 如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
                    channel.basicReject(envelope.getDeliveryTag(), false);
                } else if (msg.contains("异常")) {
                    // 批量拒绝
                    // requeue：是否重新入队列
                    // TODO 如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
                    channel.basicNack(envelope.getDeliveryTag(), true, false);
                } else {
                    // 手工应答
                    // 如果不应答，队列中的消息会一直存在，重新连接的时候会重复消费
                    channel.basicAck(envelope.getDeliveryTag(), true);
                }
            }
        };

        /**
         * 开始获取消息，push模式
         * String queue，
         * boolean autoAck, 上面开启了手动应答basicAck，所以这里是false；当没basicAck一般为true
         * Consumer callback
         */
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

}
