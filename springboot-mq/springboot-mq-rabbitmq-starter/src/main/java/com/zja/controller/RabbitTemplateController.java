/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-14 16:38
 * @Since:
 */
package com.zja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.config.FanoutConfig;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 *
 * 消息消费后就会从Queue中消失（后续的几种模型都是如此）
 */
@RestController
@RequestMapping("/rabbit/template")
public class RabbitTemplateController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //------------------------- BasicQueue ----------------------------------

    /**
     * 消息接收  监听器  先建 simple.queue
     * @param msg 消息
     */
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMsg(String msg) {
        //被消费掉的消息，会从RabbitMQ服务端中消失，不能够再被消费
        System.out.println("消费方接收到消息：" + msg);
    }

    /**
     * 消息发送 rabbitTemplate
     */
    @ApiOperation("发送消息 BasicQueue 队列")
    @GetMapping("convertAndSend")
    public void convertAndSend() {
        // 队列名称 需手动创建
        String queueName = "simple.queue";
        // 消息
        String message = "Hello, Spring AMQP!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }


    //------------------------- WorkQueue ----------------------------------

    /**
     * 消息接收
     * 注意先手动创建 simple.queue2 队列
     */
    @RabbitListener(queues = "simple.queue2")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "simple.queue2")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2-接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    /**
     * 消息发送 模拟消息堆积
     */
    @ApiOperation(value = "发送消息 WorkQueue 队列", notes = "支持一对多发布消息,多个消费者可以提高消息消费速度，消息消费后就会从Queue中消失")
    @GetMapping("convertAndSend/v2")
    public void convertAndSend2() throws InterruptedException {
        // 队列名称 需手动创建
        String queueName = "simple.queue2";
        // 消息
        String message = "Message_";
        for (int i = 1; i <= 50; i++) {
            // 发送消息
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }


    //------------------------- Fanout ----------------------------------

    /**
     * 消息接收
     * @see FanoutConfig 查看 交换机和和队列绑定
     */
    @RabbitListener(queues = "fanout.queue1")
    public void listen1FanoutQueueMsg(String msg) {
        System.out.println("Listener1 get :" + msg);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listen2FanoutQueueMsg(String msg) {
        System.out.println("Listener2 get :" + msg);
    }

    /**
     * 消息发送
     */
    @ApiOperation("发送消息 Fanout 队列")
    @GetMapping("convertAndSend/v3")
    public void convertAndSend3() {
        // 交换机名称 会自动创建
        String exchangeName = "stone.fanout";
        // 消息
        String message = "Hello, Fanout!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }


    //------------------------- Direct ----------------------------------

    /**
     * 消息接收
     * 会自动创建 Queue、Exchange
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "stone.direct", type = ExchangeTypes.DIRECT),
            key = {"talkshow", "musicshow"} //配置订阅
    ))
    public void listenDirectQueue1(String msg) {
        System.out.println("DirectQueue1 :" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "stone.direct", type = ExchangeTypes.DIRECT),
            key = {"talkshow", "news"}
    ))
    public void listenDirectQueue2(String msg) {
        System.out.println("DirectQueue2 :" + msg);
    }

    /**
     * 消息发送
     */
    @ApiOperation("发送消息 Direct 队列")
    @GetMapping("convertAndSend/v4")
    public void convertAndSend4() {
        // 交换机名称 会自动创建
        String exchangeName = "stone.direct";
        // 消息
        String messageNews = "新闻：2022的第一场雪比往年来的稍晚一些";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "news", messageNews);
        // 消息
        String messageTalks = "脱口秀：大赛即将开始";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "talkshow", messageTalks);
    }


    //------------------------- Topic ----------------------------------

    /**
     * 消息接收
     * 会自动创建 Queue、Exchange
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "stone.topic", type = ExchangeTypes.TOPIC),
            key = {"China.#"}
    ))
    public void listenTopicQueue1(String msg) {
        System.out.println("TopicQueue1 :" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "stone.topic", type = ExchangeTypes.TOPIC),
            key = {"#.news"}
    ))
    public void listenTopicQueue2(String msg) {
        System.out.println("TopicQueue2 :" + msg);
    }

    /**
     * 消息发送
     */
    @ApiOperation("发送消息 Topic 队列")
    @GetMapping("convertAndSend/v5")
    public void convertAndSend5() {
        // 交换机名称
        String exchangeName = "stone.topic";
        // 消息
        String message = "新闻：china新闻消息";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
//        rabbitTemplate.convertAndSend(exchangeName, "China.news", message);
    }


    //------------------------- 消息转换器 默认 ----------------------------------

    /**
     * 消息接收
     * 先手动创建 queues
     */
    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(HashMap<String, Object> msg) {
        System.out.println("object.queue get msg is: " + msg);
    }

    /**
     * 消息发送
     */
    @ApiOperation("发送消息 默认消息转换器")
    @GetMapping("convertAndSend/v6")
    public void convertAndSend6() {
        // 准备消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("name", "Jackson");
        msg.put("age", 24);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    //------------------------- calss对象消息发送和接收 ----------------------------------

    /**
     * 消息接收
     * 先手动创建 queues
     */
    @RabbitListener(queues = "class.queue")
    public void listenCalssQueue(String msg) throws JsonProcessingException {
        System.out.println("class.queue get msg is: " + msg);

        ObjectMapper objectMapper = new ObjectMapper();
        UserDemo userDemo = objectMapper.readValue(msg, UserDemo.class);
        System.out.println(userDemo);
    }

    /**
     * 消息发送
     */
    @ApiOperation("发送消息 calss对象传输")
    @GetMapping("convertAndSend/v7")
    public void convertAndSend7() throws JsonProcessingException {
        // 准备消息
        UserDemo userDemo = new UserDemo();
        userDemo.setUsername("Jackson");
        userDemo.setAge(22);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(userDemo);

        // 发送消息
        rabbitTemplate.convertAndSend("class.queue", jsonStr);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDemo {
        private String username;
        private int age;
        private String sex;
    }

}
