package com.zja.redis.redisson.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁: 控制对共享资源的并发访问。
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:24
 */
@Service
public class LockService {

    @Autowired
    private RedissonClient redisson;

    public void doTaskWithLock() {
        RLock lock = redisson.getLock("myLock");
        try {
            // 尝试加锁，最多等待10秒，锁有效期30秒
            boolean isLocked = lock.tryLock(10, 30, TimeUnit.SECONDS);
            if (isLocked) {
                // 执行业务逻辑
                System.out.println("Lock acquired, processing...");
                Thread.sleep(5000); // 模拟业务处理
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released");
            }
        }
    }
}