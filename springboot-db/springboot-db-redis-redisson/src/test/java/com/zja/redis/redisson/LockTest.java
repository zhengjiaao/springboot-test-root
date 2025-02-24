package com.zja.redis.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2025-02-24 11:05
 */
@SpringBootTest
public class LockTest {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testConcurrentLock() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                RLock lock = redissonClient.getLock("testLock");
                try {
                    if (lock.tryLock(5, TimeUnit.SECONDS)) { // 尝试获取锁，最多等待5秒，上锁后30秒自动解锁
                        System.out.println("获取锁成功，执行任务");
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            });
        }

        executor.shutdown();
        boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
        if (!terminated) {
            System.err.println("线程池未能在规定时间内终止，将强制关闭线程池。");
            executor.shutdownNow();
        }
    }
}