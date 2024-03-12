package com.zja.mvc.interceptor.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证和授权：拦截器可以用于对请求进行身份验证和授权检查，确保只有经过身份验证和授权的用户可以访问特定的资源。
 *
 * @author: zhengja
 * @since: 2024/03/12 15:30
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在请求处理之前执行的操作
        // 检查用户是否已经登录或是否具有访问权限
        if (!userIsAuthenticated()) {
            // 未经身份验证的用户，重定向到登录页面或返回错误信息
            response.sendRedirect("/login");
            return false; // 中断请求
        }
        return true; // 继续执行请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理之后、视图渲染之前执行的操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求完成之后执行的操作，包括视图渲染完毕
    }

    private boolean userIsAuthenticated() {
        // 执行身份验证逻辑，返回是否已经登录的结果
        // 这里可以根据你的具体需求进行实现
        return Boolean.TRUE;
    }
}
