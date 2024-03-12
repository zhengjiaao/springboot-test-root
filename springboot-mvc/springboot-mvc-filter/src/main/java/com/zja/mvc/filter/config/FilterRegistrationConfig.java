package com.zja.mvc.filter.config;

import com.zja.mvc.filter.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;

/**
 * 手动注册过滤器
 *
 * @author: zhengja
 * @since: 2024/03/12 9:19
 */
// @Configuration
public class FilterRegistrationConfig {

    @Bean
    public FilterRegistrationBean<MyFilter> myFilterRegistration() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    // or

/*    @Bean
    public MyFilter myFilter() {
        return new MyFilter();
    }

    @Bean
    public FilterRegistrationBean<MyFilter> myFilterRegistration() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(myFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }*/

    // 设置过滤器顺序

    @Bean
    public FilterRegistrationBean<MyFilter1> myFilter1Registration() {
        FilterRegistrationBean<MyFilter1> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter1());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // 设置过滤器执行顺序

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> myFilter2Registration() {
        FilterRegistrationBean<MyFilter2> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter2());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2); // 设置过滤器执行顺序

        return registrationBean;
    }

    public static class MyFilter1 implements Filter {
        // MyFilter1的实现
        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
            // 初始化代码放在这里
            System.out.println("MyFilter1的实现 init 方法");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("MyFilter1的实现 doFilter 方法");
            // 将请求和响应传递给过滤器链中的下一个过滤器
            chain.doFilter(request, response);
        }
    }

    public static class MyFilter2 implements Filter {
        // MyFilter2的实现
        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
            // 初始化代码放在这里
            System.out.println("MyFilter2的实现 init 方法");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("MyFilter2的实现 init 方法");
            // 将请求和响应传递给过滤器链中的下一个过滤器
            chain.doFilter(request, response);
        }
    }

}
