package com.zja.redis.listener.example1;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 处理过期键的逻辑
 *
 * @author: zhengja
 * @since: 2024/03/06 17:02
 */
@Component
public class RedisKeyExpirationListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        // 处理过期键的逻辑
        System.out.println("Key expired: " + expiredKey);
    }
}