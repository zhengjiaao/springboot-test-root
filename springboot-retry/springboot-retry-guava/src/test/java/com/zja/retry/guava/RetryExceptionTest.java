package com.zja.retry.guava;

import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 14:03
 */
public class RetryExceptionTest {

    @Test
    public void RetryWithException_Test() {
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfException() // 若抛出异常就重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 设置最大执行次数3次
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS)).build(); // 设置重试等待时间1秒

        try {
            retryer.call(() -> performOperation());
        } catch (RetryException e) {
            System.err.println("All retry attempts failed. Reason: " + e.getCause().getMessage());
            // 可以根据具体异常情况进行不同的处理
        } catch (Exception e) {
            System.err.println("An unexpected exception occurred: " + e.getMessage());
            // 处理其他异常情况
        }
    }

    public static Boolean performOperation() {
        System.out.println("Performing the operation...");
        // 模拟一个可能会抛出异常的操作
        if (Math.random() < 0.8) {
            throw new RuntimeException("Random failure occurred");
        }
        return true;
    }
}
