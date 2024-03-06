//package com.dist.config;
//
//import com.dist.interceptor.RequestInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
///**
// * @author: zhengja
// * @since: 2019/6/25 16:05
// */
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurationSupport {
//
//    @Override
//    protected void addInterceptors(final InterceptorRegistry registry){
//// 注册自定义拦截器，添加拦截路径和排除拦截路径
//        registry.addInterceptor(new RequestInterceptor())
//                .addPathPatterns("/rest/resource/*")
//                .excludePathPatterns(
//                        "/swagger-ui.html"
//                );
//        super.addInterceptors(registry);
//    }
//}
