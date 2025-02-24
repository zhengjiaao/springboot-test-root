package com.zja.redis.redisson.service;

import com.zja.redis.redisson.util.RedissonLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-02-24 15:40
 */
@Service
public class MyService {

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    public void performTaskWithLock(String taskId) {
        Runnable task = () -> {
            // 模拟需要加锁的任务
            System.out.println("执行任务: " + taskId);
            try {
                Thread.sleep(1000); // 模拟任务执行时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("任务完成: " + taskId);
        };

        redissonLockUtil.executeWithLock("myLockKey:" + taskId, task);
    }
}