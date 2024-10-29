package com.zja.retry.guava.service;

import com.github.rholder.retry.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 13:42
 */
@Deprecated // 没起作用
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    // 重试器 retryer1: 失败后一直重试,以2000毫秒为间隔重不断试
    private final Retryer<Void> retryer1 = RetryerBuilder.<Void>newBuilder()
            // .retryIfExceptionOfType(Throwable.class) // 如果出现Throwable异常则重试
            // .retryIfRuntimeException()
            .retryIfException()
            .withWaitStrategy(WaitStrategies.fibonacciWait(2000, TimeUnit.MILLISECONDS)) // 等待重试策略
            .withStopStrategy(StopStrategies.neverStop()) // 停止策略
            .build();

    // 重试器 retryer2：重试3次后抛出异常，每次重试间隔等待2000毫秒
    private final Retryer<String> retryer2 = RetryerBuilder.<String>newBuilder()
            // .retryIfExceptionOfType(Throwable.class) // retryIfExceptionOfType()：重试条件，另外还有 retryIfResult，retryIfRuntimeException
            // .retryIfRuntimeException()
            .retryIfException()
            .withWaitStrategy(WaitStrategies.fixedWait(2000, TimeUnit.MILLISECONDS)) // 等待重试策略
            .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 停止策略，尝试3次
            .build();

    //  重试时机: 根据异常进行重试
    @Test
    public void findUser1() throws Exception {
        retryer1.call(userService.findUser1(1));
    }


    //  重试时机: 根据返回结果进行重试
    @Test
    public void findUser2() throws Exception {
        retryer2.call(userService.findUser2(1));
    }

}
