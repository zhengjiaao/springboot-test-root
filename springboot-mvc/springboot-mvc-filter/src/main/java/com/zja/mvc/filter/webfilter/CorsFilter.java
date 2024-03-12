package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域请求处理
 * 场景：在Web应用中处理跨域请求，添加必要的CORS（跨域资源共享）头信息，以允许来自其他域的请求访问资源
 *
 * @author: zhengja
 * @since: 2024/03/11 16:22
 */
// @WebFilter(urlPatterns = "/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 添加CORS头信息
        addCorsHeaders(httpRequest, httpResponse);

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()

    private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        // 允许来自所有域的请求访问资源
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

        // 允许的请求头
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // 允许携带身份凭证（如Cookie）
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}