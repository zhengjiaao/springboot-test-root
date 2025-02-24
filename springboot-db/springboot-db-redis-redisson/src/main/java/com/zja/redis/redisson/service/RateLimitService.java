package com.zja.redis.redisson.service;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 分布式限流器: 控制接口访问频率。
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:28
 */
@Service
public class RateLimitService {

    @Autowired
    private RedissonClient redisson;

    public boolean tryAccess(String apiKey) {
        RRateLimiter limiter = redisson.getRateLimiter(apiKey);
        // 每秒最多5个请求 , 高版本语法
        limiter.trySetRate(RateType.OVERALL, 5, Duration.ofSeconds(1));
        // limiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        return limiter.tryAcquire(1);
    }

}