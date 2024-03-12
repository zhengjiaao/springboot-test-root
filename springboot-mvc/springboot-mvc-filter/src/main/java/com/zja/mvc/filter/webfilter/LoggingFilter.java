package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 日志记录：
 * 场景：对请求和响应进行日志记录，用于跟踪和审计应用程序的行为。
 * 场景：记录请求和响应的日志，包括请求URL、参数、响应状态等，用于监控和故障排查。
 *
 * @author: zhengja
 * @since: 2024/03/11 16:25
 */
// @WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 记录请求信息
        logRequest(httpRequest);

        chain.doFilter(request, response);

        // 记录响应信息
        logResponse(httpRequest, httpResponse);
    }

    // 其他方法：init() 和 destroy()

    // private void logRequest(HttpServletRequest request) {
    //     // 记录请求的URL、方法、参数等信息
    //     String url = request.getRequestURL().toString();
    //     String method = request.getMethod();
    //     String queryString = request.getQueryString();
    //     // ...
    //     System.out.println("Request: " + method + " " + url + "?" + queryString);
    // }

    private void logRequest(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String timestamp = new Date().toString();

        // 将请求信息写入日志文件或其他输出渠道
        System.out.println("Request: " + timestamp + " - " + method + " " + url + "?" + queryString);
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response) {
        // 记录响应的状态码、内容长度等信息
        int statusCode = response.getStatus();
        int bufferSize = response.getBufferSize();
        // ...
        System.out.println("Response: " + statusCode + " " + bufferSize);
    }
}