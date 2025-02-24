package com.zja.redis.redisson.service.pub;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发布订阅（Pub/Sub）：实现跨服务消息通信。
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:26
 */
@Service
public class MessagePublisher {

    @Autowired
    private RedissonClient redisson;

    // 发布消息
    public void publishMessage(String channel, String message) {
        RTopic topic = redisson.getTopic(channel);
        topic.publish(message);
    }
}