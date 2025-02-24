package com.zja.redis.redisson.service;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

/**
 * 滑动窗口限流（API 接口保护）: 限流过滤器
 * <p>
 * 限流策略：滑动窗口限流适用于突发流量，令牌桶适合平滑限流
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:55
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private RedissonClient redissonClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        RRateLimiter limiter = redissonClient.getRateLimiter("apiLimit:" + request.getRequestURI());
        // 每分钟允许100次请求 , 高版本语法
        limiter.trySetRate(RateType.OVERALL, 100, Duration.ofMinutes(1));
        // limiter.trySetRate(RateType.OVERALL, 100, 1, RateIntervalUnit.SECONDS);

        if (limiter.tryAcquire()) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("请求过于频繁");
        }
    }
}