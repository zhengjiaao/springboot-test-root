package com.zja.log.efk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * EFK 日志系统示例：Elasticsearch + Fluentd + Kibana
 * <p>
 * 说明：应用将日志以 JSON 格式输出到文件/控制台，
 * Fluentd 采集 JSON 日志后转发到 Elasticsearch，
 * 通过 Kibana 进行可视化查询。适合容器化(K8s)环境。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class EfkApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EfkApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EfkApplication.class);
    }
}
