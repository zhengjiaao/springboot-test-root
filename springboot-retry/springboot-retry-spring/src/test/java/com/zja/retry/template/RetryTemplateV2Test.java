package com.zja.retry.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryState;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import java.util.stream.IntStream;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 11:19
 */
@Slf4j
@SpringBootTest
public class RetryTemplateV2Test {

    /**
     * 运行重试方法
     *
     * @param retryTemplate
     * @throws Exception
     */
    public void run(RetryTemplate retryTemplate) throws Exception {
        Integer result = retryTemplate.execute(new RetryCallback<Integer, Exception>() {
            int i = 0;

            // 重试操作
            @Override
            public Integer doWithRetry(RetryContext retryContext) throws Exception {
                retryContext.setAttribute("value", i);
                log.info("重试 {} 次.", retryContext.getRetryCount());
                return checkLen(i++);
            }
        }, new RecoveryCallback<Integer>() {// 兜底回调
            @Override
            public Integer recover(RetryContext retryContext) throws Exception {
                log.info("重试{}次后，调用兜底方法", retryContext.getRetryCount());
                return Integer.MAX_VALUE;
            }

        });

        log.info("最终结果: {}", result);
    }


    /**
     * 根据i判断是否抛出异常
     *
     * @param num
     * @return
     * @throws Exception
     */
    public int checkLen(int num) throws Exception {
        // 小于5抛出异常
        if (num < 5) throw new Exception(num + " le 5");
        // 否则正常返回
        return num;
    }

    // SimpleRetryPolicy固定次数重试策略
    @Test
    public void retryFixTimes() throws Exception {
        // 重试模板
        RetryTemplate retryTemplate = new RetryTemplate();
        // 简单重试策略
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        // 最大重试次数3次
        simpleRetryPolicy.setMaxAttempts(3);
        // 模板设置重试策略
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        // 开始执行- 超过3次最大重试次数，触发了recoveryCall，并返回Integer最大值。
        run(retryTemplate);
    }


    // 根据返回结果值实现重试
    @Test
    public void retryWithResult() throws Exception {
        // 重试模板
        RetryTemplate retryTemplate = new RetryTemplate();

        // 设置为无限重试策略
        retryTemplate.setRetryPolicy(new AlwaysRetryPolicy() {
            private static final long serialVersionUID = 1213824522266301314L;

            @Override
            public boolean canRetry(RetryContext context) {
                // 小于1则重试
                return context.getAttribute("value") == null || Integer.parseInt(context.getAttribute("value").toString()) < 1;
            }
        });

        // 开始执行-如果value值小于1或者为null则进行重试，反之不在进行重试，触发RecoveryCallback回调，并返回Integer最大值。
        run(retryTemplate);
    }

    // AlwaysRetryPolicy无限重试策略,直到成功，此方式逻辑不当会导致死循环
    @Test
    public void retryAlwaysTimes() throws Exception {
        // 重试模板
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置为无限重试策略
        retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
        // 开始执行-直到i等于5则正常返回，之前将实现无限重试。
        run(retryTemplate);
    }

    // TimeoutRetryPolicy超时时间重试策略
    @Test
    public void retryTimeout() throws Exception {
        // 重试模板
        RetryTemplate retryTemplate = new RetryTemplate();

        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        // 超时时间为 1000 毫秒
        timeoutRetryPolicy.setTimeout(1000);

        // 固定时间的回退策略，需设置参数sleeper和backOffPeriod，sleeper指定等待策略，默认是Thread.sleep，即线程休眠，backOffPeriod指定休眠时间，默认1秒,单位毫秒
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        // 休眠时间400毫秒
        fixedBackOffPolicy.setBackOffPeriod(400);

        // 设置为超时时间重试策略
        retryTemplate.setRetryPolicy(timeoutRetryPolicy);
        // 设置为固定时间的回退策略
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        // 开始执行-设定1000ms后则认定为超时，每次重试等待时长400ms，故重试3次后即会超出超时阈值，触发RecoveryCallback回调，并返回Integer最大值。
        run(retryTemplate);
    }

    // 启用熔断器重试策略
    @Test
    public void retryCircuitBreakerTest() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 传入RetryPolicy（每个RetryPolicy实现都有自己的重试策略实现），是真正判断是否重试的策略，当重试失败时，则执行熔断策略；
        CircuitBreakerRetryPolicy retryPolicy = new CircuitBreakerRetryPolicy(new SimpleRetryPolicy(4));

        // 固定时间等待策略-每次重试等待300毫秒
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(300);

        // 熔断器电路打开的超时时间
        retryPolicy.setOpenTimeout(1500);
        // 重置熔断器重新闭合的超时时间
        retryPolicy.setResetTimeout(2000);

        // 设置重试策略
        retryTemplate.setRetryPolicy(retryPolicy);
        // 设置回退策略
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        long startTime = System.currentTimeMillis();

        //
        IntStream.range(0, 10).forEach(index -> {
            try {
                Thread.sleep(100);
                RetryState state = new DefaultRetryState("circuit", false);
                String result = retryTemplate.execute(
                        // 重试业务逻辑
                        new RetryCallback<String, RuntimeException>() {
                            @Override
                            public String doWithRetry(RetryContext context) throws RuntimeException {
                                log.info("重试 {} 次", context.getRetryCount());
                                if (System.currentTimeMillis() - startTime > 1300 && System.currentTimeMillis() - startTime < 1500) {
                                    return "retryCallback-success";
                                }
                                throw new RuntimeException("timeout");
                            }
                        },
                        // 重试失败回调
                        new RecoveryCallback<String>() {
                            @Override
                            public String recover(RetryContext context) throws Exception {
                                return "recoveryCallback-default";
                            }
                        }, state);
                log.info("result: {}", result);
            } catch (Exception e) {
                log.error("报错了: type:{}:{}", e.getClass().getName(), e.getMessage());
            }
        });
    }
}
