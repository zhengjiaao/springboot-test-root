package com.zja.redis.redisson.service.pub;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 发布订阅（Pub/Sub）：实现跨服务消息通信。
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:27
 */
@Component
public class MessageSubscriber {

    @Autowired
    private RedissonClient redisson;

    // 订阅消息
    @PostConstruct
    public void subscribe() {
        RTopic topic = redisson.getTopic("myChannel");
        topic.addListener(String.class, (channel, msg) -> {
            System.out.println("Received message: " + msg);
        });
    }
}