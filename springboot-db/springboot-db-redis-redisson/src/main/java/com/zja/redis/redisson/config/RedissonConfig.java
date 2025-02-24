package com.zja.redis.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Redisson 配置类（支持单机、哨兵、集群模式）
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:22
 */
@Configuration
public class RedissonConfig {

    // 单节点模式
    @Bean
    public RedissonClient redissonClient(RedisProperties redisProps) {
        Config config = new Config();
        // 单节点模式
        config.useSingleServer()
                .setAddress("redis://" + redisProps.getHost() + ":" + redisProps.getPort())
                .setPassword(redisProps.getPassword())
                .setDatabase(redisProps.getDatabase())
                .setConnectionPoolSize(64) // 连接池优化：根据业务量调整 connectionPoolSize 和 connectionMinimumIdleSize
                .setConnectionMinimumIdleSize(10);
        return Redisson.create(config);
    }

    // 哨兵/集群模式配置: 哨兵模式配置示例
    /*@Bean
    public RedissonClient redissonSentinel(RedisProperties props) {
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName(props.getSentinel().getMaster())
                .addSentinelAddress(convertNodes(props.getSentinel().getNodes()))
                .setPassword(props.getPassword())
                .setDatabase(props.getDatabase());
        return Redisson.create(config);
    }

    private String[] convertNodes(List<String> nodes) {
        return nodes.stream()
                .map(node -> "redis://" + node)
                .toArray(String[]::new);
    }*/
}
