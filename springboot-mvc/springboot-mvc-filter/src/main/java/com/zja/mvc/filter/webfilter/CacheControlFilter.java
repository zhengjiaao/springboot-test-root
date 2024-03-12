package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 缓存控制：
 * 场景：设置响应的缓存策略，以提高性能和减少网络流量。
 *
 * @author: zhengja
 * @since: 2024/03/11 16:32
 */
// @WebFilter(urlPatterns = "/*")
public class CacheControlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置缓存策略
        httpResponse.setHeader("Cache-Control", "public, max-age=3600"); // 缓存1小时

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()
}