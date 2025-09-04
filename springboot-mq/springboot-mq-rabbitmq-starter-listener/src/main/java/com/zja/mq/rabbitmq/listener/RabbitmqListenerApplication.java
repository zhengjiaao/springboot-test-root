package com.zja.mq.rabbitmq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2025/08/01 15:16
 */
@SpringBootApplication
public class RabbitmqListenerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqListenerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RabbitmqListenerApplication.class);
    }

    /**
     * 消息接收  监听器  先建 simple.queue
     *
     * @param msg 消息
     */
    /*@RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMsg(String msg) {
        // 被消费掉的消息，会从RabbitMQ服务端中消失，不能够再被消费
        System.out.println("消费方接收到消息：" + msg);
    }*/

    /**
     * 消息接收  监听器  先建 ningbo-queue 队列
     *
     * @param msg 消息
     */
    // @RabbitListener(queues = "ningbo-queue", ackMode = "AUTO") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    @RabbitListener(queues = "ningbo-queue") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    // @RabbitListener(queues = "ningbo-queue-test") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    // @RabbitListener(queues = "ningbo-queue-test-zja") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    public void listenSimpleQueueMsg(String msg) {
        // 被消费掉的消息，会从RabbitMQ服务端中消失，不能够再被消费
        System.out.println("消费方接收到消息1：" + msg);
    }

    /**
     * 消息接收  监听器  先建 ningbo-queue 队列
     *
     * @param msg 消息
     */
    // @RabbitListener(queues = "ningbo-queue") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    // @RabbitListener(queues = "ningbo-queue-test") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    @RabbitListener(queues = "ningbo-queue-test-zja") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    public void listenSimpleQueueMsg2(String msg) {
        // 被消费掉的消息，会从RabbitMQ服务端中消失，不能够再被消费
        System.out.println("消费方接收到消息2：" + msg);
    }

    /**
     * 消息接收  监听器  先建 ningbo-queue 队列
     *
     * @param msg 消息
     */
    // @RabbitListener(queues = "ningbo-queue") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    @RabbitListener(queues = "ningbo-queue-test") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    // @RabbitListener(queues = "ningbo-queue-test-zja") // AckMode.AUTO 自动确认模式（默认值），如果消息处理成功，容器会自动发送确认;如果处理过程中抛出异常，消息会被拒绝并可能重新入队
    public void listenSimpleQueueMsg3(String msg) {
        // 被消费掉的消息，会从RabbitMQ服务端中消失，不能够再被消费
        System.out.println("消费方接收到消息3：" + msg);
    }

    /**
     * 消息接收  监听器  先建 ningbo-queue 队列
     *
     * @param message 消息
     */
    // @RabbitListener(queues = "ningbo-queue")
    /*@RabbitListener(queues = "ningbo-queue-test-zja")
    public void listenSimpleQueueMsg(Message message, Channel channel) {
        // 被消费掉的消息，会从RabbitMQ服务端中消失，不能够再被消费
        try {
            System.out.println("消费方接收到消息：" + message.toString());
        } catch (Exception e) {
            throw new RuntimeException("接收消息报错：",e);
        }
    }*/

}