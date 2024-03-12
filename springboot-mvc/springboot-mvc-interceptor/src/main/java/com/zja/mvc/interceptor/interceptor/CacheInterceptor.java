package com.zja.mvc.interceptor.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 缓存控制：拦截器可以用于对响应进行缓存控制，例如设置缓存头以减少重复请求。
 *
 * @author: zhengja
 * @since: 2024/03/12 15:31
 */
public class CacheInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在请求处理之前执行的操作
        // 检查是否存在缓存，如果存在则返回缓存的响应
        if (cacheExists()) {
            // 返回缓存的响应
            return false; // 中断请求
        }
        return true; // 继续执行请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理之后、视图渲染之前执行的操作
        // 如果需要缓存响应，可以在这里进行缓存操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求完成之后执行的操作，包括视图渲染完毕
    }

    private boolean cacheExists() {
        // 检查是否存在缓存
        // 这里可以根据你的具体需求进行实现
        return Boolean.FALSE;
    }
}