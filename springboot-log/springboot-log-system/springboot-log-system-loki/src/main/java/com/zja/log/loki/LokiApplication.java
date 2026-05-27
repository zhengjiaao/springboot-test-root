package com.zja.log.loki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Loki 日志系统示例：Loki + Grafana
 * <p>
 * 说明：通过 Loki4j Logback Appender 将日志以 HTTP 方式推送到 Loki，
 * Loki 存储日志后通过 Grafana 进行可视化查询。
 * Loki 相比 Elasticsearch 更轻量，只索引标签不索引全文内容，存储成本更低。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class LokiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LokiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LokiApplication.class);
    }
}
