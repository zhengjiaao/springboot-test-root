package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问控制：
 * 场景：对特定URL或资源进行访问控制，例如身份验证、权限检查等
 *
 * @author: zhengja
 * @since: 2024/03/11 16:34
 */
// @WebFilter(urlPatterns = "/admin/*")
public class AccessControlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 进行访问控制，例如检查用户是否已登录或具有所需的权限

        if (hasAccess(httpRequest)) {
            chain.doFilter(request, response);
        } else {
            // 返回权限不足的错误响应或重定向到登录页面
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }

    // 其他方法：init() 和 destroy()

    private boolean hasAccess(HttpServletRequest request) {
        // 判断是否具有访问权限的逻辑

        return Boolean.TRUE;
    }
}