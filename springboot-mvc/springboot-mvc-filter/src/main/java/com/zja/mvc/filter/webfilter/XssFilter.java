package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/03/11 16:29
 */
// @WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        chain.doFilter(new XssRequestWrapper(httpRequest), httpResponse);
    }

    // 其他方法：init() 和 destroy()

    private static class XssRequestWrapper extends HttpServletRequestWrapper {

        public XssRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return sanitize(value);
        }

        // 其他重写方法：getParameterValues()、getParameterMap()等

        private String sanitize(String value) {
            // 执行XSS过滤操作，例如使用常见的XSS过滤器库或自定义过滤规则
            // 对输入的字符串进行XSS过滤，例如：移除<script>标签等
            // ...

            String sanitizedValue = value;
            return sanitizedValue;
        }
    }
}