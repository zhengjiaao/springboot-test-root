package com.zja.log.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * ELK 日志系统示例：Elasticsearch + Logstash + Kibana
 * <p>
 * 说明：通过 Logstash TCP Appender 将日志以 JSON 格式发送到 Logstash，
 * Logstash 将日志写入 Elasticsearch，通过 Kibana 进行可视化查询。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class ElkApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ElkApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ElkApplication.class);
    }
}
