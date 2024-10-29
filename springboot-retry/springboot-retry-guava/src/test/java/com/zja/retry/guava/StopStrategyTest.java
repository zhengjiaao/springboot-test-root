package com.zja.retry.guava;

import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * 停止策略
 *
 * @Author: zhengja
 * @Date: 2024-10-29 14:30
 */
public class StopStrategyTest {

    @Test
    public void StopStrategy_Test() {
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfException()
                .withStopStrategy(StopStrategies.stopAfterDelay(3, TimeUnit.SECONDS)) // 在3秒后停止重试
                // .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 重试3次后停止重试
                // .withStopStrategy(StopStrategies.neverStop()) //  永远不停止重试
                // .withStopStrategy(new CustomStopStrategy()) // 自定义策略
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                .build();

        try {
            retryer.call(() -> performOperation());
        } catch (RetryException e) {
            System.err.println("All retry attempts failed. Reason: " + e.getCause().getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected exception occurred: " + e.getMessage());
        }
    }

    public static Boolean performOperation() {
        System.out.println("Performing the operation...");
        if (Math.random() < 0.9) {
            throw new RuntimeException("Random failure occurred");
        }
        return true;
    }

    static class CustomStopStrategy implements StopStrategy {
        private int maxAttempts = 3;

        @Override
        public boolean shouldStop(Attempt attempt) {
            long attemptNumber = attempt.getAttemptNumber();

            if (attemptNumber >= maxAttempts) {
                return true;
            }

            return false;
        }
    }
}
