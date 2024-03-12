package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证和授权：
 * 场景：对请求进行身份认证和权限控制，确保只有经过认证和授权的用户可以访问特定的资源或执行特定的操作。
 *
 * @author: zhengja
 * @since: 2024/03/11 16:28
 */
// @WebFilter(urlPatterns = "/secured/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 检查用户是否已认证，如果未认证则跳转到登录页面
        if (!isAuthenticated(httpRequest)) {
            httpResponse.sendRedirect("/login");
            return;
        }

        // 检查用户是否有访问权限，如果没有权限则返回403 Forbidden
        if (!hasAccess(httpRequest)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()

    private boolean isAuthenticated(HttpServletRequest request) {
        // 根据业务逻辑检查用户是否已认证
        // ...

        boolean isAuthenticated = Boolean.TRUE;
        return isAuthenticated;
    }

    private boolean hasAccess(HttpServletRequest request) {
        // 根据业务逻辑检查用户是否有访问权限
        // ...
        boolean hasAccess = Boolean.TRUE;
        return hasAccess;
    }
}