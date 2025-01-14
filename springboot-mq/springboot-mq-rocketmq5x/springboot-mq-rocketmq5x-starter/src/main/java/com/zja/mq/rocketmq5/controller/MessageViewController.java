package com.zja.mq.rocketmq5.controller;

import com.zja.mq.rocketmq5.model.MessageView;
import com.zja.mq.rocketmq5.service.MessageViewProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 17:46
 */
@RestController
@RequestMapping("/mq/view")
public class MessageViewController {

    @Autowired
    MessageViewProducer messageViewProducer;

    @GetMapping("/sendMessage")
    public String sendMessage(MessageView message) {
        messageViewProducer.sendMessage(message);
        return "success";
    }

    @GetMapping("/sendMessage/v2")
    public String sendMessageV2(MessageView message) {
        messageViewProducer.sendMessageV2(message);
        return "success";
    }
}
