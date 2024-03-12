package com.zja.mvc.interceptor.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志记录：拦截器可以用于记录请求的详细信息，如请求路径、请求参数、响应时间等，以便进行日志记录和监控。
 *
 * @author: zhengja
 * @since: 2024/03/12 15:31
 */
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在请求处理之前执行的操作
        // 记录请求的详细信息，如请求路径、请求参数等
        logRequestDetails(request);
        return true; // 继续执行请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理之后、视图渲染之前执行的操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求完成之后执行的操作，包括视图渲染完毕
        // 记录响应时间、响应状态码等信息
        logResponseDetails(response);
    }

    private void logRequestDetails(HttpServletRequest request) {
        // 记录请求的详细信息
        // 这里可以根据你的具体需求进行实现
    }

    private void logResponseDetails(HttpServletResponse response) {
        // 记录响应的详细信息
        // 这里可以根据你的具体需求进行实现
    }
}