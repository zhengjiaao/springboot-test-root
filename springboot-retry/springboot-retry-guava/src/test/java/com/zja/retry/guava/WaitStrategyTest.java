package com.zja.retry.guava;

import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 14:09
 */
public class WaitStrategyTest {

    @Test
    public void RetryWithException_Test() {
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfException() // 捕获异常
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最多重试5次
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS)) // 固定等待时间间隔策略：在固定的时间间隔后重试操作。
                // .withWaitStrategy(WaitStrategies.exponentialWait(100, 10, TimeUnit.SECONDS)) // 指数退避策略：在每次重试时指数级增加等待时间间隔。
                // .withWaitStrategy(WaitStrategies.randomWait(1, TimeUnit.SECONDS, 10, TimeUnit.SECONDS)) // 随机等待时间间隔策略：在一定范围内随机选择等待时间间隔。
                // .withWaitStrategy(customWaitStrategy) // 自定义等待时间策略：根据自定义规则定义等待时间间隔。
                .build();

        try {
            retryer.call(() -> performOperation());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (RetryException e) {
            throw new RuntimeException("所有重试尝试均失败：", e);
        }
    }

    public static Boolean performOperation() {
        System.out.println("Performing the operation...");
        throw new RuntimeException("Something went wrong");
    }

    WaitStrategy customWaitStrategy = new WaitStrategy() {
        @Override
        public long computeSleepTime(Attempt failedAttempt) {
            // Custom logic to compute the sleep time
            return 3000; // 3 second
        }
    };
}
