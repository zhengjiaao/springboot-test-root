package com.zja.mvc.filter.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/03/11 16:11
 */
// @WebFilter(urlPatterns = "/*")
// @Component
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码放在这里
        System.out.println("MyFilter的实现 init 方法");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 预处理代码放在这里
        System.out.println("MyFilter的实现 doFilter 方法");
        // 将请求和响应传递给过滤器链中的下一个过滤器
        chain.doFilter(request, response);

        // 后处理代码放在这里
    }

    @Override
    public void destroy() {
        // 清理代码放在这里
        System.out.println("MyFilter的实现 destroy 方法");
    }
}