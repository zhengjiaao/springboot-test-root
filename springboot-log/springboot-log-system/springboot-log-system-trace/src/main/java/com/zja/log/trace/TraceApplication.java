package com.zja.log.trace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Cloud Sleuth + Zipkin 分布式链路追踪示例
 * <p>
 * 说明：Sleuth 自动在日志中注入 traceId 和 spanId，
 * 日志格式变为：[appName, traceId, spanId, exportable]
 * Zipkin 收集链路数据并提供可视化 UI 展示。
 * <p>
 * Sleuth 自动追踪：
 * - HTTP 请求（RestTemplate、WebClient、Feign）
 * - 消息队列（RabbitMQ、Kafka）
 * - 定时任务（@Scheduled）
 * - 异步方法（@Async）
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class TraceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TraceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TraceApplication.class);
    }
}
