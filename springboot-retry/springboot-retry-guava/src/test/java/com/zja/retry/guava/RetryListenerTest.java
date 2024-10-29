package com.zja.retry.guava;

import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 13:25
 */
public class RetryListenerTest {

    public int realAction(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException();
        }
        return num;
    }

    // 重试监听器：RetryListener
    private static class MyRetryListener implements RetryListener {
        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            System.out.println("第" + attempt.getAttemptNumber() + "次执行");
        }
    }

    @Test
    public void RetryListener_Test() throws ExecutionException, RetryException {
        Retryer<Integer> retryer = RetryerBuilder.<Integer>newBuilder().retryIfException()
                .withRetryListener(new MyRetryListener())
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)).build(); // 设置最大执行次数3次
        retryer.call(() -> realAction(0));
    }


}
