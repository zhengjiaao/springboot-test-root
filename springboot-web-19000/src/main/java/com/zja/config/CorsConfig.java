/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-28 20:17
 * @Since:
 */
package com.zja.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); //支持cookie 跨域
//        config.setAllowedOrigins(Arrays.asList("*"));
//        config.setAllowedHeaders(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList("*"));
//        config.setMaxAge(300L);//设置时间有效
//
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
}
