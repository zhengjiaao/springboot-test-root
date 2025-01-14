package com.zja.mq.rocketmq5.controller;

import com.zja.mq.rocketmq5.service.MessageProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 13:24
 */
@RestController
@RequestMapping("/mq")
public class MessageController {

    @Resource
    private MessageProducer messageProducer;

    // 需要使用topic全称，所以进行topic名称的拼接，也可以自己设置  格式：topic名称
    @Value("${rocketmq.producer.topic:test-topic}")
    private String topic;

    @GetMapping("/syncSend")
    public ResponseEntity<String> syncSend(@RequestParam String message) {
        messageProducer.syncSend(topic, "tag", message);
        return ResponseEntity.ok("Message sent: " + message);
    }

    @GetMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        messageProducer.sendOneWayMessage(topic, message);
        return ResponseEntity.ok("Message sent: " + message);
    }

    @GetMapping("/sendSyncMessage")
    public ResponseEntity<String> sendSyncMessage(@RequestParam String message) {
        messageProducer.asyncSendMessage(message, "tag");
        return ResponseEntity.ok("Message sent: " + message);
    }

    @GetMapping("/sendOrderlyMessage")
    public ResponseEntity<String> sendOrderlyMessage(@RequestParam String message) {
        messageProducer.sendOrderlyMessage(topic, message, "orderKey");
        return ResponseEntity.ok("Message sent: " + message);
    }

    @GetMapping("/sendDelayedMessage")
    public ResponseEntity<String> sendDelayedMessage(@RequestParam String message, @RequestParam int delayLevel) {
        messageProducer.sendDelayedMessage(topic, message, delayLevel);
        return ResponseEntity.ok("Delayed message sent: " + message + " with delayLevel: " + delayLevel);
    }

    @GetMapping("/sendTransactionMessage")
    public ResponseEntity<String> sendTransactionMessage(@RequestParam String message) {
        messageProducer.sendTransactionMessage(topic, message);
        return ResponseEntity.ok("Transaction message sent: " + message);
    }

    @GetMapping("/sendBatchMessage")
    public ResponseEntity<String> sendBatchMessage(@RequestParam String message) {
        messageProducer.sendBatchMessage(topic, java.util.Arrays.asList(message, message, message));
        return ResponseEntity.ok("Batch message sent: " + message);
    }

    @GetMapping("/sendFilterMessage")
    public ResponseEntity<String> sendFilterMessage(@RequestParam String message, @RequestParam String filterTag) {
        messageProducer.sendFilterMessage(topic, message, filterTag);
        return ResponseEntity.ok("Filter message sent: " + message + " with filterTag: " + filterTag);
    }
}
