package com.zja.redis.redisson.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 锁工具类封装
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:51
 */
@Component
@Slf4j
public class RedissonLockUtil {
    @Autowired
    private RedissonClient redissonClient;

    public void executeWithLock(String lockKey, Runnable task) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(5, 30, TimeUnit.SECONDS)) {
                task.run();
            } else {
                log.error("获取锁失败: {}", lockKey);
            }
        } catch (InterruptedException e) {
            log.error("锁获取中断", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}