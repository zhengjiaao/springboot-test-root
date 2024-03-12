package com.zja.mvc.interceptor.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: zhengja
 * @since: 2024/03/12 15:28
 */
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyInterceptor 拦截器 preHandle 方法");
        // 在请求处理之前执行的操作
        return true; // 返回true表示继续执行请求，返回false表示中断请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyInterceptor 拦截器 postHandle 方法");
        // 在请求处理之后、视图渲染之前执行的操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyInterceptor 拦截器 afterCompletion 方法");
        // 在整个请求完成之后执行的操作，包括视图渲染完毕
    }
}