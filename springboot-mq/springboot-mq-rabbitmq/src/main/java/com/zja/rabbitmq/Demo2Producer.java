/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-15 13:15
 * @Since:
 */
package com.zja.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Demo2Producer extends BaseConfig {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";

    public static void main(String[] args) throws Exception {
        // 工厂模式
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);

        Connection connection = factory.newConnection(); // 获取连接
        Channel channel = connection.createChannel(); // 获取消息通道

        String msg = "Hello world, RabbitMQ"; // 消息
        /**
         * 发送消息 basicPublish
         * String exchange, 交换机
         * String routingKey, 路由键
         * BasicProperties props, 消息属性（需要单独声明）
         * byte[] body，消息体
         */
        channel.basicPublish(EXCHANGE_NAME, "my.best", null, msg.getBytes());

        channel.close();
        connection.close();
    }

}
