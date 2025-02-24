package com.zja.redis.redisson.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁进阶实现: 带看门狗的自动续期锁
 * <p>
 * 锁超时处理：结合看门狗机制避免业务未完成锁过期，设置合理的 lockWatchdogTimeout（默认30秒）
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:50
 */
@Service
public class LockOrderService {
    @Autowired
    private RedissonClient redissonClient;

    public void createOrder(String orderId) {
        RLock lock = redissonClient.getLock("orderLock:" + orderId);
        try {
            // 尝试获取锁，等待10秒，锁默认30秒自动续期（看门狗机制）
            boolean locked = lock.tryLock(10, -1, TimeUnit.SECONDS);
            if (locked) {
                // 执行业务逻辑（如扣减库存）
                processOrder(orderId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void processOrder(String orderId) {
        // 模拟耗时操作
        System.out.println("处理订单：" + orderId);
    }
}