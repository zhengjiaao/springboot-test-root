/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 11:04
 * @Since:
 */
package com.zja.concurrency;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: zhengja
 * @since: 2023/11/07 11:04
 */
public class RateLimiterUnitTest {

    @Test
    public void testRateLimiter() throws InterruptedException {
        // 创建速率限制器，每秒允许2个请求
        RateLimiter rateLimiter = RateLimiter.create(2.0);

        // 第一个请求
        boolean isFirstRequestAllowed = rateLimiter.tryAcquire();
        assertTrue(isFirstRequestAllowed);

        // 第二个请求
        boolean isSecondRequestAllowed = rateLimiter.tryAcquire();
        assertTrue(isSecondRequestAllowed); // todo 这里有个问题，并不是如期通过

        // 第三个请求
        boolean isThirdRequestAllowed = rateLimiter.tryAcquire();
        assertFalse(isThirdRequestAllowed); // 当速率限制器每秒只允许2个请求时，第三个请求将不会被立即允许。

        // 等待1秒后再次尝试请求
        Thread.sleep(1000);

        // 第四个请求
        boolean isFourthRequestAllowed = rateLimiter.tryAcquire();
        assertTrue(isFourthRequestAllowed);
    }
}
