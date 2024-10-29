package com.zja.retry.guava;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import org.junit.jupiter.api.Test;

/**
 * 重试时机
 *
 * @Author: zhengja
 * @Date: 2024-10-29 14:24
 */
public class RetryIfTest {

    private int invokeCount = 0;

    public int realAction(int num) {
        invokeCount++;
        System.out.printf("当前执行第 %d 次,num:%d%n", invokeCount, num);
        if (num <= 0) {
            throw new IllegalArgumentException();
        }
        return num;
    }

    @Test
    public void RetryIf_Test() {
        Retryer<Integer> retryer = RetryerBuilder.<Integer>newBuilder()
                // 非正数进行重试
                // .retryIfException()
                .retryIfRuntimeException()
                // .retryIfExceptionOfType(IllegalArgumentException.class)
                // .retryIfException(t -> t instanceof IllegalArgumentException)
                // 偶数则进行重试
                .retryIfResult(result -> result % 2 == 0)
                // 设置最大执行次数3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)).build();

        try {
            invokeCount = 0;
            retryer.call(() -> realAction(0));
        } catch (Exception e) {
            System.out.println("执行0，异常：" + e.getMessage());
        }

        try {
            invokeCount = 0;
            retryer.call(() -> realAction(1));
        } catch (Exception e) {
            System.out.println("执行1，异常：" + e.getMessage());
        }

        try {
            invokeCount = 0;
            retryer.call(() -> realAction(2));
        } catch (Exception e) {
            System.out.println("执行2，异常：" + e.getMessage());
        }
    }
}
