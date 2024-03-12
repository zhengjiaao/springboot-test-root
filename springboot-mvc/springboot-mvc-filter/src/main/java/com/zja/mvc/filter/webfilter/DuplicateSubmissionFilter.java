package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 防止重复提交：
 * 场景：防止用户重复提交表单或重复执行某个操作，通过Token机制进行验证。
 *
 * @author: zhengja
 * @since: 2024/03/11 16:30
 */
// @WebFilter(urlPatterns = "/submit-form")
public class DuplicateSubmissionFilter implements Filter {

    private static final String TOKEN_ATTRIBUTE = "duplicateToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isDuplicateSubmission(httpRequest)) {
            // 非法的重复提交
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Duplicate form submission");
            return;
        }

        // 生成并保存Token
        String token = generateToken();
        httpRequest.getSession().setAttribute(TOKEN_ATTRIBUTE, token);

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()

    private boolean isDuplicateSubmission(HttpServletRequest request) {
        String token = request.getParameter("token");
        String sessionToken = (String) request.getSession().getAttribute(TOKEN_ATTRIBUTE);

        return token != null && sessionToken != null && token.equals(sessionToken);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
