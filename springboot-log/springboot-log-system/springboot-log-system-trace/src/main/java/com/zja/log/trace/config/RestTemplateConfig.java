package com.zja.log.trace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置
 * <p>
 * Sleuth 会自动拦截 RestTemplate 的请求，
 * 在请求头中注入 traceId/spanId，实现跨服务链路传播。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
