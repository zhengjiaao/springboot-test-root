package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求重定向：
 * 场景：根据某些条件，将请求重定向到不同的URL
 *
 * @author: zhengja
 * @since: 2024/03/11 16:35
 */
// @WebFilter(urlPatterns = "/redirect/*")
public class RedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 根据条件判断是否需要重定向
        if (shouldRedirect(httpRequest)) {
            String newUrl = getNewUrl(httpRequest);

            // 执行重定向
            httpResponse.sendRedirect(newUrl);
            return;
        }

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()

    private boolean shouldRedirect(HttpServletRequest request) {
        // 判断是否需要重定向的逻辑

        return Boolean.FALSE;
    }

    private String getNewUrl(HttpServletRequest request) {
        // 获取新的重定向URL的逻辑

        return "/NewUrl";
    }
}
