package com.zja.mvc.rate.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流核心处理器：基于本地内存实现多种限流算法
 * <p>
 * 支持算法：
 * <ul>
 *   <li>固定窗口（FIXED_WINDOW）</li>
 *   <li>滑动窗口（SLIDING_WINDOW）</li>
 *   <li>令牌桶（TOKEN_BUCKET）</li>
 *   <li>漏桶（LEAKY_BUCKET）</li>
 * </ul>
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
@Component
public class RateLimitHandler {

    private static final Logger log = LoggerFactory.getLogger(RateLimitHandler.class);

    // ======================= 固定窗口 =======================
    /** key -> [windowStart(ms), count] */
    private final ConcurrentHashMap<String, long[]> fixedWindowMap = new ConcurrentHashMap<>();

    // ======================= 滑动窗口 =======================
    /** key -> 时间戳队列（每次请求记录时间戳，过期的移除） */
    private final ConcurrentHashMap<String, Deque<Long>> slidingWindowMap = new ConcurrentHashMap<>();

    // ======================= 令牌桶 =======================
    /**
     * key -> [tokens(浮点缩放*1000), lastRefillTime(ms), maxTokens*1000, refillRate(tokens/ms)*1000000]
     * 索引：0=当前令牌数(×1000), 1=上次补充时间(ms), 2=最大令牌数(×1000), 3=每毫秒补充速率(×1000000)
     */
    private final ConcurrentHashMap<String, long[]> tokenBucketMap = new ConcurrentHashMap<>();

    // ======================= 漏桶 =======================
    /**
     * key -> [lastLeakTime(ms), currentWater(×1000), capacity(×1000), leakRate(per ms, ×1000000)]
     */
    private final ConcurrentHashMap<String, long[]> leakyBucketMap = new ConcurrentHashMap<>();

    /**
     * 判断本次请求是否被允许（统一入口）
     *
     * @param key       限流 key
     * @param rateLimit 注解配置
     * @return true=允许通过，false=被限流
     */
    public boolean isAllowed(String key, RateLimit rateLimit) {
        long periodMs = rateLimit.timeUnit().toMillis(rateLimit.period());
        long limit = rateLimit.limit();
        RateLimitAlgorithm algorithm = rateLimit.algorithm();

        switch (algorithm) {
            case FIXED_WINDOW:
                return fixedWindowAllow(key, limit, periodMs);
            case SLIDING_WINDOW:
                return slidingWindowAllow(key, limit, periodMs);
            case TOKEN_BUCKET:
                long burstCapacity = rateLimit.burstCapacity() > 0 ? rateLimit.burstCapacity() : limit;
                return tokenBucketAllow(key, limit, periodMs, burstCapacity);
            case LEAKY_BUCKET:
                return leakyBucketAllow(key, limit, periodMs);
            default:
                return slidingWindowAllow(key, limit, periodMs);
        }
    }

    // =====================================================================
    // 固定窗口算法
    // =====================================================================

    /**
     * 固定窗口限流
     *
     * @param key      限流 key
     * @param limit    窗口内最大请求数
     * @param periodMs 窗口大小（毫秒）
     * @return true=允许
     */
    private synchronized boolean fixedWindowAllow(String key, long limit, long periodMs) {
        long now = System.currentTimeMillis();
        long[] state = fixedWindowMap.computeIfAbsent(key, k -> new long[]{now, 0L});

        long windowStart = state[0];
        long count = state[1];

        // 窗口是否已过期
        if (now - windowStart >= periodMs) {
            // 开启新窗口
            state[0] = now;
            state[1] = 1L;
            return true;
        }

        if (count < limit) {
            state[1]++;
            return true;
        }
        return false;
    }

    // =====================================================================
    // 滑动窗口算法
    // =====================================================================

    /**
     * 滑动窗口限流
     *
     * @param key      限流 key
     * @param limit    窗口内最大请求数
     * @param periodMs 窗口大小（毫秒）
     * @return true=允许
     */
    private boolean slidingWindowAllow(String key, long limit, long periodMs) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = slidingWindowMap.computeIfAbsent(key, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            long windowStart = now - periodMs;
            // 移除过期时间戳
            while (!timestamps.isEmpty() && timestamps.peekFirst() <= windowStart) {
                timestamps.pollFirst();
            }
            if (timestamps.size() < limit) {
                timestamps.addLast(now);
                return true;
            }
            return false;
        }
    }

    // =====================================================================
    // 令牌桶算法
    // =====================================================================

    /**
     * 令牌桶限流
     *
     * @param key          限流 key
     * @param limit        每个时间窗口补充的令牌数（即速率）
     * @param periodMs     补充令牌的时间窗口（毫秒）
     * @param burstCapacity 桶的最大容量
     * @return true=允许
     */
    private boolean tokenBucketAllow(String key, long limit, long periodMs, long burstCapacity) {
        long now = System.currentTimeMillis();
        // 缩放因子：避免浮点，将令牌数×1000存储
        final long scale = 1000L;
        // 每毫秒产生的令牌数 × scale × scale（refillRate 单位：令牌/ms × scale²）
        // refillRateScaled = (limit × scale) / periodMs
        final long maxTokensScaled = burstCapacity * scale;
        final long refillRateScaled = limit * scale / periodMs; // 每ms生产令牌数 × scale

        long[] state = tokenBucketMap.computeIfAbsent(key, k -> new long[]{maxTokensScaled, now, maxTokensScaled, refillRateScaled});

        synchronized (state) {
            long elapsed = now - state[1]; // 距上次补充时间（ms）
            // 补充令牌
            long newTokens = elapsed * state[3]; // = elapsed × refillRateScaled
            state[0] = Math.min(state[0] + newTokens, state[2]); // 不超过最大容量
            state[1] = now;

            if (state[0] >= scale) { // 消耗1个令牌（= scale单位）
                state[0] -= scale;
                return true;
            }
            return false;
        }
    }

    // =====================================================================
    // 漏桶算法
    // =====================================================================

    /**
     * 漏桶限流
     *
     * @param key      限流 key
     * @param limit    漏桶容量（最大积压请求数）
     * @param periodMs 处理所有 limit 个请求需要的时间（毫秒），即定义漏出速率
     * @return true=允许
     */
    private boolean leakyBucketAllow(String key, long limit, long periodMs) {
        long now = System.currentTimeMillis();
        final long scale = 1000L;
        final long capacityScaled = limit * scale;
        // 漏出速率：每毫秒漏出多少（× scale）
        final long leakRateScaled = limit * scale / periodMs;

        long[] state = leakyBucketMap.computeIfAbsent(key, k -> new long[]{now, 0L, capacityScaled, leakRateScaled});

        synchronized (state) {
            long elapsed = now - state[0]; // 流逝时间（ms）
            // 漏水
            long leaked = elapsed * state[3];
            state[1] = Math.max(0, state[1] - leaked);
            state[0] = now;

            if (state[1] + scale <= state[2]) { // 加水 1 个单位
                state[1] += scale;
                return true;
            }
            return false;
        }
    }

    /**
     * 清除指定 key 的所有限流状态（用于测试或手动重置）
     *
     * @param key 限流 key
     */
    public void reset(String key) {
        fixedWindowMap.remove(key);
        slidingWindowMap.remove(key);
        tokenBucketMap.remove(key);
        leakyBucketMap.remove(key);
        log.debug("[RateLimit] 已重置限流状态，key={}", key);
    }

    /**
     * 清除所有限流状态
     */
    public void resetAll() {
        fixedWindowMap.clear();
        slidingWindowMap.clear();
        tokenBucketMap.clear();
        leakyBucketMap.clear();
        log.info("[RateLimit] 已清除所有限流状态");
    }
}
