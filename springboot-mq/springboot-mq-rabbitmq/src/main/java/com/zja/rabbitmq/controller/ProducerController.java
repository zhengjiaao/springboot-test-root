/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-15 13:17
 * @Since:
 */
package com.zja.rabbitmq.controller;

import com.rabbitmq.client.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 */
@RestController
@RequestMapping("/msg/producer")
public class ProducerController {

    /**
     * RebbitMQ server 连接参数配置：主机名、端口号、vhost、用户名、密码
     */
    static String host = "192.168.159.136";
    static int port = 5672;
    static String virtualHost = "/";
    static String username = "admin";
    static String password = "pass";

    static String queue1Name = "topic_queue1";
    static String queue2Name = "topic_queue2";

    /**
     * 消费接收
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);
        // 3. 创建连接 Connection
        Connection connection = factory.newConnection();
        // 4. 创建频道 Channel
        Channel channel = connection.createChannel();

        Consumer consumer = new Consumer() {
            @Override
            public void handleConsumeOk(String s) {

            }

            @Override
            public void handleCancelOk(String s) {

            }

            @Override
            public void handleCancel(String s) throws IOException {

            }

            @Override
            public void handleShutdownSignal(String s, ShutdownSignalException e) {

            }

            @Override
            public void handleRecoverOk(String s) {

            }

            /*
             * handleDelivery(java.lang.String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes)
             * s: 标识
             * envelope: 获取一些信息，交换机路由key等
             * basicProperties: 配置信息
             * bytes: 接收到的数据
             * */
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println(new String(bytes));
            }
        };

        // 5. 接收消息
        /*
         * basicConsume(String queue, boolean autoAck, Consumer callback)
         * queue: 接收队列名称
         * autoAck: 是否自动确认(后期消息可靠性文章中进行讲解)
         * callback: 接收到消息执行的回调函数
         * */
        // 消费队列topic_queue1
        channel.basicConsume(queue1Name, true, consumer);
        // 消费队列topic_queue2
        channel.basicConsume(queue2Name, true, consumer);
    }

    @ApiOperation("消息发送")
    @GetMapping("/basicPublish/v1")
    public ResponseEntity producer1() throws IOException, TimeoutException {

        //1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);

        //2、获取连接
        Connection connection = factory.newConnection();
        //3、获取消息通道
        Channel channel = connection.createChannel();
        //4、创建交换机
        /*
         * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
         * exchange: 交换机名称
         * type: 交换机类型
         *           DIRECT("direct"),定向
         *           FANOUT("fanout"),广播 发送到每一个与该交换机绑定的队列
         *           TOPIC("topic"),通配符方式
         * durable: 是否持久化
         * autoDelete: 是否自动删除
         * internal: 内部使用 一般为false
         * arguments: 参数
         * */
        String exchangeName = "topic_exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);

        //5、创建队列
        /*
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * queue: 队列名称
         * durable: 是否持久化(当MQ关闭再启动时数据还在)
         * exclusive: 是否独占(只能有一个消费者监听该队列)
         * autoDelete: 当没有消费者时，是否自动删除队列
         * arguments： 参数
         * */
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        //6、绑定交换机与队列
        /*
         * queueBind(String queue, String exchange, String routingKey)
         * queue: 队列名称
         * exchange: 交换机名称
         * routingKey： 路由key
         * */
        channel.queueBind(queue1Name, exchangeName, "#.error");
        channel.queueBind(queue2Name, exchangeName, "#.info");
        channel.queueBind(queue2Name, exchangeName, "#.warning");
        channel.queueBind(queue2Name, exchangeName, "*.error");

        String body = "hello rabbitmq";
        channel.basicPublish(exchangeName, "log.error", null, body.getBytes());
        channel.basicPublish(exchangeName, "log.info", null, body.getBytes());
        channel.basicPublish(exchangeName, "log.warning", null, body.getBytes());

        //7、关闭通道，关闭连接
        channel.close();
        connection.close();

        return ResponseEntity.ok("发送成功");
    }


}
