package com.zja.log.plg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * PLG 可观测性系统示例：Prometheus（指标） + Loki（日志） + Grafana（可视化）
 * <p>
 * 说明：PLG 是云原生环境下主流的可观测性方案：
 * - Prometheus：采集应用指标（QPS、响应时间、JVM、自定义业务指标等）
 * - Loki：采集应用日志（轻量级日志聚合）
 * - Grafana：统一可视化面板，同时展示指标和日志
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class PlgApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PlgApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlgApplication.class);
    }
}
