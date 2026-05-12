package com.zja.mvc.rate.ratelimit;

/**
 * 限流维度类型
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
public enum RateLimitType {

    /**
     * 全局限流：所有请求共享同一个计数器，不区分来源
     */
    GLOBAL,

    /**
     * IP 限流：按客户端 IP 地址进行限流
     */
    IP,

    /**
     * 用户限流：按认证用户标识进行限流（从请求头 X-User-Id 或 Principal 中获取）
     */
    USER,

    /**
     * 接口限流：按请求 URL + Method 进行限流（与 GLOBAL 类似，但 key 更精确）
     */
    URL,

    /**
     * IP + URL 组合限流：同一 IP 访问同一接口的频率限制
     */
    IP_URL,

    /**
     * 用户 + URL 组合限流：同一用户访问同一接口的频率限制
     */
    USER_URL,

    /**
     * 自定义 key 限流：通过 {@link RateLimit#customKey()} 指定 SpEL 表达式动态生成 key
     */
    CUSTOM
}
