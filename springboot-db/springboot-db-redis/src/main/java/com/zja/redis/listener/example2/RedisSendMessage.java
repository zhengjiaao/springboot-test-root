package com.zja.redis.listener.example2;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis发送方:发送消息
 *
 * @author: zhengja
 * @since: 2024/03/06 16:39
 */
@Component
public class RedisSendMessage {

    // 传string会出现乱码
    @Resource
    private RedisTemplate redisTemplate;
    // 解决string乱码问题
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis-发布
     *
     * @param body 发布的内容
     */
    public void sendMessage(String body) {
        /**
         * 使用redisTemplate的convertAndSend()函数，
         * String channel, Object message
         * channel代表管道，
         * message代表发送的信息
         */
        System.out.println("body===" + body);
        stringRedisTemplate.convertAndSend("topic1", body);
    }
}
