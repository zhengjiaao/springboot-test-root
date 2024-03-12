package com.zja.mvc.interceptor.config;

import com.zja.mvc.interceptor.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: zhengja
 * @since: 2024/03/12 15:28
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加你的拦截器，并指定拦截的路径
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
    }

    // 静态资源也会被拦截
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 虚拟映射：映射本地路径和资源路径
         */
        // 访问地址：http://127.0.0.1:8080/public/file
        registry.addResourceHandler("/public/file/**").addResourceLocations("file:D:/picture/");
    }
}
