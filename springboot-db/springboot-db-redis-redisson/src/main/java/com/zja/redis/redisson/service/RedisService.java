package com.zja.redis.redisson.service;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Redisson 基础数据操作示例
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:48
 */
@Service
public class RedisService {
    @Autowired
    private RedissonClient redissonClient;

    public void setValue(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    public String getValue(String key) {
        return String.valueOf(redissonClient.getBucket(key).get());
    }
}