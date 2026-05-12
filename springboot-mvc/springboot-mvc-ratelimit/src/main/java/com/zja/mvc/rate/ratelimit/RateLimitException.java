package com.zja.mvc.rate.ratelimit;

/**
 * 限流异常：当请求超出频率限制时抛出此异常
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
public class RateLimitException extends RuntimeException {

    private final String limitKey;
    private final long limit;
    private final long period;

    public RateLimitException(String message, String limitKey, long limit, long period) {
        super(message);
        this.limitKey = limitKey;
        this.limit = limit;
        this.period = period;
    }

    public String getLimitKey() {
        return limitKey;
    }

    public long getLimit() {
        return limit;
    }

    public long getPeriod() {
        return period;
    }

    @Override
    public String toString() {
        return "RateLimitException{" +
                "message='" + getMessage() + '\'' +
                ", limitKey='" + limitKey + '\'' +
                ", limit=" + limit +
                ", period=" + period +
                '}';
    }
}
