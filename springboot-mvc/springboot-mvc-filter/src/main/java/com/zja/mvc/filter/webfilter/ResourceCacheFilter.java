package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源缓存：
 * 场景：设置静态资源的缓存策略，使浏览器能够缓存资源并减少重复请求。
 *
 * @author: zhengja
 * @since: 2024/03/11 16:36
 */
// @WebFilter(urlPatterns = {"/css/*", "/js/*", "/images/*"})
public class ResourceCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置资源缓存策略，如Cache-Control、Expires等响应头字段

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()
}
