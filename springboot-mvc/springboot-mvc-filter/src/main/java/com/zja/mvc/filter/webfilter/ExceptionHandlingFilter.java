package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 异常处理：
 * 场景：捕获请求处理过程中的异常，进行适当的处理和响应
 *
 * @author: zhengja
 * @since: 2024/03/11 16:38
 */
// @WebFilter(urlPatterns = "/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            // 处理异常并返回自定义的错误响应
        }
    }

    // 其他方法：init() 和 destroy()
}