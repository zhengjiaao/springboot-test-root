package com.zja.mvc.filter.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/04/24 17:02
 */
@Configuration
public class AuthFilterConfig {

    @Bean
    public AuthFilter clientAuthFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new AuthFilter(requestMappingHandlerMapping);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> clientFilterRegistrationBean(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(authFilter); // 注册 Filter
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/rest/user/*");
        // urlPatterns.add("/get/*");
        filterRegistrationBean.setUrlPatterns(urlPatterns); // 设置Filter拦截路径
        filterRegistrationBean.setOrder(0); // 设置Filter执行顺序

        return filterRegistrationBean;
    }

}
