/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-19 10:29
 * @Since:
 */
package com.zja.kafka.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<?, String> kafkaTemplate;


    public static final String TOPIC = "testTopic";

    /**
     * 消息接收
     */
    @KafkaListener(topics = TOPIC, groupId = "testGroup", topicPartitions = {})
    public void receive(@Payload String message) {
        System.out.println("Consumer= " + message);
    }

    /**
     * 消息发送
     */
    @GetMapping("send")
    @ApiOperation("消息发送")
    public void send() {
        String message = "Hello Kafka！";
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Provider=" + message);
    }
}
