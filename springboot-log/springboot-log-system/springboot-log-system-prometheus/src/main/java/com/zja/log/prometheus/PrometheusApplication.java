package com.zja.log.prometheus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Prometheus 指标监控示例
 * <p>
 * 说明：通过 Spring Boot Actuator + Micrometer 暴露 Prometheus 格式的监控指标，
 * Prometheus 定时拉取指标数据，Grafana 可视化展示。
 * <p>
 * 核心指标：JVM 内存/GC/线程、HTTP 请求统计、自定义业务指标
 * </p>
 * <p>
 * 指标端点：<a href="http://localhost:8080/actuator/prometheus">...</a>
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class PrometheusApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PrometheusApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PrometheusApplication.class);
    }
}
