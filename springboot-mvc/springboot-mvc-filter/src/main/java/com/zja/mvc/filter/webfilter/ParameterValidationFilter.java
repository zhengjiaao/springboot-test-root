package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 参数验证和转换：
 * 场景：对请求参数进行验证和转换，确保其有效性和一致性
 *
 * @author: zhengja
 * @since: 2024/03/11 16:38
 */
// @WebFilter(urlPatterns = "/api/*")
public class ParameterValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 验证请求参数的有效性，进行转换和处理

        chain.doFilter(request, response);

        // 在响应返回给客户端之前进行后处理的逻辑
    }

    // 其他方法：init() 和 destroy()
}