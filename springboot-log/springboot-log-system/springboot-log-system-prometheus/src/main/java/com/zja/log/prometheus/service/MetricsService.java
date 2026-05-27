package com.zja.log.prometheus.service;

import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 系统监控服务：演示 Prometheus 各类指标类型
 * <p>
 * Prometheus 四种核心指标类型：
 * 1. Counter（计数器）：只增不减，如请求总数、错误总数
 * 2. Gauge（仪表盘）：可增可减，如当前在线用户数、队列深度
 * 3. Timer（计时器）：记录事件持续时间和次数
 * 4. DistributionSummary（分布摘要）：记录数据分布，如请求大小
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class MetricsService {

    private final Counter requestCounter;
    private final Counter errorCounter;
    private final AtomicInteger onlineUsers;
    private final Timer apiTimer;
    private final DistributionSummary requestSizeSummary;

    public MetricsService(MeterRegistry meterRegistry) {
        // Counter：API 请求总数
        this.requestCounter = Counter.builder("api_requests_total")
                .description("API请求总数")
                .tag("service", "metrics")
                .register(meterRegistry);

        // Counter：错误总数
        this.errorCounter = Counter.builder("api_errors_total")
                .description("API错误总数")
                .tag("service", "metrics")
                .register(meterRegistry);

        // Gauge：在线用户数
        this.onlineUsers = new AtomicInteger(0);
        Gauge.builder("online_users", onlineUsers, AtomicInteger::get)
                .description("当前在线用户数")
                .tag("service", "metrics")
                .register(meterRegistry);

        // Timer：API 响应时间
        this.apiTimer = Timer.builder("api_response_time")
                .description("API响应时间")
                .tag("service", "metrics")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        // DistributionSummary：请求大小分布
        this.requestSizeSummary = DistributionSummary.builder("api_request_size")
                .description("API请求大小分布(bytes)")
                .tag("service", "metrics")
                .publishPercentiles(0.5, 0.95)
                .register(meterRegistry);
    }

    @PostConstruct
    public void init() {
        onlineUsers.set(10);
    }

    /**
     * 模拟 API 调用（记录请求数、响应时间）
     */
    public String simulateApiCall(String api) {
        return apiTimer.record(() -> {
            requestCounter.increment();
            log.info("API调用, api={}", api);

            // 模拟处理
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(10, 200));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 模拟随机大小
            requestSizeSummary.record(ThreadLocalRandom.current().nextInt(100, 5000));

            return "OK";
        });
    }

    /**
     * 模拟错误
     */
    public void simulateError() {
        errorCounter.increment();
        log.error("模拟业务错误，错误计数器 +1");
    }

    /**
     * 用户上线
     */
    public int userOnline() {
        int count = onlineUsers.incrementAndGet();
        log.info("用户上线, 当前在线人数={}", count);
        return count;
    }

    /**
     * 用户下线
     */
    public int userOffline() {
        int count = onlineUsers.decrementAndGet();
        log.info("用户下线, 当前在线人数={}", count);
        return count;
    }
}
