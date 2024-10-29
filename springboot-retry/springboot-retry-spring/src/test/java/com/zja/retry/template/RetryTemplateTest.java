package com.zja.retry.template;

import com.zja.retry.exception.DataAccessException;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.retry.*;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 手动调用 spring 重试 API
 *
 * @Author: zhengja
 * @Date: 2024-10-29 11:02
 */
public class RetryTemplateTest {

    // 有补偿 重试
    @Test
    public void testSimple() throws Exception {
        @Data
        class Foo {
            private String id;
        }

        RetryTemplate template = new RetryTemplate();
        // 超时时间重试策略，默认超时时间为1秒，在指定的超时时间内允许重试
        TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
        policy.setTimeout(3000L);

        template.setRetryPolicy(policy);
        Foo result = template.execute(
                // 可能触发重试的业务逻辑
                new RetryCallback<Foo, Exception>() {
                    @Override
                    public Foo doWithRetry(RetryContext context) {
                        try {
                            System.out.println("调用百度接口。。。。");
                            TimeUnit.MILLISECONDS.sleep(1000);
                            throw new RuntimeException("调用百度接口超时");
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                        }
                        return new Foo();
                    }

                },
                // 重试耗尽后的回调,配置了RecoveryCallback,超出重试次数后不会抛出异常,而是执行回调里的代码
                new RecoveryCallback<Foo>() {// 可以没有RecoveryCallback,超出重试次数后依然会抛出异常
                    @Override
                    public Foo recover(RetryContext context) throws Exception {
                        System.out.println("调用百度接口。。。recover");
                        return new Foo();
                    }
                });

        System.out.println("result=>" + result);
    }

    // 无补偿 重试
    @Test
    public void testSimple2() throws Exception {
        @Data
        class Foo {
            private String id;
        }

        RetryTemplate template = new RetryTemplate();
        // 超时时间重试策略，默认超时时间为1秒，在指定的超时时间内允许重试
        TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
        policy.setTimeout(3000L);

        template.setRetryPolicy(policy);
        Foo result = template.execute(
                // 可能触发重试的业务逻辑
                new RetryCallback<Foo, Exception>() {
                    @Override
                    public Foo doWithRetry(RetryContext context) {
                        try {
                            System.out.println("调用百度接口。。。。");
                            TimeUnit.MILLISECONDS.sleep(1000);
                            throw new RuntimeException("调用百度接口超时");
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                        }
                        return new Foo();
                    }

                });

        System.out.println("result=>" + result);
    }

    // 无状态重试： 是在一个循环中执行完重试策略，即重试上下文保持在一个线程上下文中，在一次调用中进行完整的重试策略判断。
    // 适用于无状态的场景，比如数据库连接，远程调用（远程调用某个查询方法）等。
    @Test
    public void testSimple3() throws Exception {
        RetryTemplate template = new RetryTemplate();
        // 重试策略：次数重试策略
        RetryPolicy retryPolicy = new SimpleRetryPolicy(3);
        template.setRetryPolicy(retryPolicy);

        // 退避策略：倍数回退策略
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(100);
        backOffPolicy.setMaxInterval(3000);
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setSleeper(new ThreadWaitSleeper());
        template.setBackOffPolicy(backOffPolicy);


        String result = template.execute(new RetryCallback<String, RuntimeException>() {
            @Override
            public String doWithRetry(RetryContext context) throws RuntimeException {
                System.out.println("retry count:" + context.getRetryCount());
                throw new RuntimeException("timeout");
            }
        }, new RecoveryCallback<String>() {
            @Override
            public String recover(RetryContext context) throws Exception {
                return "default";
            }
        });

        System.out.println("result=>" + result);
    }

    // 有状态重试：就是不在一个线程上下文完成重试，有2种场景需要使用有状态重试，事务操作需要回滚或者熔断器模式。
    // 适用于有状态的场景，比如,事务操作需要回滚或者熔断器模式等。
    // 在事务操作需要回滚场景
    @Test
    public void retryState() throws Exception {
        RetryTemplate template = new RetryTemplate();
        // 重试策略：次数重试策略
        RetryPolicy retryPolicy = new SimpleRetryPolicy(3);
        template.setRetryPolicy(retryPolicy);

        // 当前状态的名称，当把状态放入缓存时，通过该key查询获取
        Object key = "mykey";
        // 是否每次都重新生成上下文还是从缓存中查询，即全局模式（如熔断器策略时从缓存中查询）
        boolean isForceRefresh = true;
        // 对DataAccessException进行回滚
        BinaryExceptionClassifier rollbackClassifier = new BinaryExceptionClassifier(Collections.<Class<? extends Throwable>>singleton(DataAccessException.class));

        RetryState state = new DefaultRetryState(key, isForceRefresh, rollbackClassifier);

        String result = template.execute(new RetryCallback<String, RuntimeException>() {
            @Override
            public String doWithRetry(RetryContext context) throws RuntimeException {
                System.out.println("retry count:" + context.getRetryCount());
                // throw new TypeMismatchDataAccessException("");
                throw new RuntimeException("类型不匹配数据访问异常");
            }
        }, new RecoveryCallback<String>() {
            @Override
            public String recover(RetryContext context) throws Exception {
                System.out.println("recovery count:" + context.getRetryCount());
                return "default";
            }
        }, state);
    }

    @Test
    public void circuitBreakerRetryPolicy() {
        RetryTemplate template = new RetryTemplate();
        // 传入RetryPolicy（每个RetryPolicy实现都有自己的重试策略实现），是真正判断是否重试的策略，当重试失败时，则执行熔断策略；
        CircuitBreakerRetryPolicy retryPolicy = new CircuitBreakerRetryPolicy(new SimpleRetryPolicy(3));
        // 熔断器电路打开的超时时间
        retryPolicy.setOpenTimeout(5000);
        // 重置熔断器重新闭合的超时时间
        retryPolicy.setResetTimeout(20000);
        template.setRetryPolicy(retryPolicy);

        for (int i = 0; i < 10; i++) {
            try {
                Object key = "circuit";
                boolean isForceRefresh = false;
                RetryState state = new DefaultRetryState(key, isForceRefresh);

                String result = template.execute(
                        // 重试逻辑
                        new RetryCallback<String, RuntimeException>() {
                            @Override
                            public String doWithRetry(RetryContext context) throws RuntimeException {
                                System.out.println("retry count:" + context.getRetryCount());
                                throw new RuntimeException("timeout");
                            }
                        },
                        // 重试失败兜底
                        new RecoveryCallback<String>() {
                            @Override
                            public String recover(RetryContext context) throws Exception {
                                return "default";
                            }
                        }, state);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("catch=>" + e.getMessage());
            }
        }
    }
}
