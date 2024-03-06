package com.zja.redis.listener.example1;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 配置处理过期键的监听器
 *
 * @author: zhengja
 * @since: 2024/03/06 17:02
 */
public class RedisKeyExpirationConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       RedisKeyExpirationListener expirationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(expirationListener, new PatternTopic("__keyevent@*__:expired")); // 指定要监听的事件类型为键过期事件
        return container;
    }

    // 常见的频道模式示例（PatternTopic）：
    //
    // __keyevent@*__:expired：匹配所有键过期事件。
    // __keyspace@*__:yourkey*：匹配以yourkey开头的所有键的事件。
    // __keyspace@0__:yourkey*：匹配以yourkey开头且位于数据库0的键的事件。

    // 你可以根据需要根据频道模式来订阅或监听Redis中的事件。例如：
    // 1.使用PatternTopic("__keyevent@*__:expired")来订阅所有键过期事件，
    // 2.使用PatternTopic("__keyspace@*__:yourkey*")来订阅以yourkey开头的键的事件。
}
