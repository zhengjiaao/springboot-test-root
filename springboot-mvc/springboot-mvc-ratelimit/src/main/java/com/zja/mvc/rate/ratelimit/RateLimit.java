package com.zja.mvc.rate.ratelimit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口访问频率限制注解
 * <p>
 * 使用示例：
 * <pre>
 *   // 全局每秒最多10次
 *   {@code @RateLimit(limit = 10, period = 1, timeUnit = TimeUnit.SECONDS)}
 *
 *   // 按IP限流，每分钟最多60次，令牌桶算法
 *   {@code @RateLimit(limit = 60, period = 1, timeUnit = TimeUnit.MINUTES, type = RateLimitType.IP, algorithm = RateLimitAlgorithm.TOKEN_BUCKET)}
 *
 *   // 自定义key（SpEL），按用户名限流
 *   {@code @RateLimit(limit = 100, type = RateLimitType.CUSTOM, customKey = "#request.getHeader('X-User-Name')")}
 * </pre>
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流描述信息（用于日志和响应提示）
     */
    String message() default "请求过于频繁，请稍后再试";

    /**
     * 时间窗口内允许的最大请求次数
     * 默认：100次
     */
    long limit() default 100;

    /**
     * 时间窗口大小
     * 默认：1
     */
    long period() default 1;

    /**
     * 时间单位
     * 默认：秒（SECONDS）
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流维度类型
     * 默认：全局限流（GLOBAL）
     *
     * @see RateLimitType
     */
    RateLimitType type() default RateLimitType.GLOBAL;

    /**
     * 限流算法
     * 默认：滑动窗口（SLIDING_WINDOW）
     *
     * @see RateLimitAlgorithm
     */
    RateLimitAlgorithm algorithm() default RateLimitAlgorithm.SLIDING_WINDOW;

    /**
     * 自定义限流 key（SpEL 表达式），仅在 {@link RateLimitType#CUSTOM} 时生效。
     * <p>
     * 可用变量：
     * <ul>
     *   <li>{@code #request} - HttpServletRequest 对象</li>
     *   <li>{@code #args[n]} - 方法第 n 个参数</li>
     *   <li>{@code #p0}, {@code #p1}... - 方法参数别名</li>
     * </ul>
     * 示例：{@code "#request.getHeader('X-User-Id')"}
     */
    String customKey() default "";

    /**
     * 被限流时是否返回 429 HTTP 状态码（Too Many Requests）。
     * <p>
     * true：返回 HTTP 429；false：返回 HTTP 200，但响应体中包含错误信息。
     * 默认：true
     */
    boolean httpStatus429() default true;

    /**
     * 令牌桶算法专属：桶的最大容量（突发量），仅 {@link RateLimitAlgorithm#TOKEN_BUCKET} 时生效。
     * <p>
     * 0 表示与 {@link #limit()} 相同。
     * 默认：0
     */
    long burstCapacity() default 0;

    /**
     * 白名单 IP 列表（精确匹配），白名单内的 IP 不受限流约束。
     * 示例：{@code {"127.0.0.1", "192.168.1.100"}}
     */
    String[] whitelistIps() default {};

    /**
     * 是否记录限流日志
     * 默认：true
     */
    boolean enableLog() default true;
}
