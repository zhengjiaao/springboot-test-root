
package com.dist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**前后端分开部署情况下存在跨域访问的问题，如果前后端部署在一个域下则注释掉下面的配置
 * @company: Dist
 * @date：2017/5/17
 * @author: ChenYanping
 * desc：跨域的相关配置
 */
@Configuration
@ConditionalOnExpression("${dist.cors.allow}")
public class CorsConfig {

    @Value("${dist.cors.mapping}")
    private String mapping;

    @Value("${dist.cors.origin}")
    private String origin;

    @Value("${dist.cors.method}")
    private String method;


    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin(this.origin);
        //是否发送Cookie信息
        config.setAllowCredentials(true);
        //放行哪些原始域(请求方式)
        config.addAllowedMethod(this.method);
        //放行哪些原始域(头部信息)
        config.addAllowedHeader(this.origin);
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        //config.addExposedHeader(HttpHeaders.SERVER);

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration(this.mapping, config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}

