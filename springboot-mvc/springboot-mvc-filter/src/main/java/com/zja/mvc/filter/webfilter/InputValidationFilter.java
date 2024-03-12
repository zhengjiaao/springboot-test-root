package com.zja.mvc.filter.webfilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 输入验证：
 * 场景：对请求的输入参数进行验证，确保输入的数据符合预期的格式和规则，以提高应用程序的安全性和可靠性。
 * @author: zhengja
 * @since: 2024/03/11 16:23
 */
@WebFilter(urlPatterns = "/api/*")
public class InputValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 验证输入参数
        if (!validateInputParameters(httpRequest)) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input parameters");
            return;
        }

        chain.doFilter(request, response);
    }

    // 其他方法：init() 和 destroy()

    private boolean validateInputParameters(HttpServletRequest request) {
        String parameter1 = request.getParameter("param1");
        String parameter2 = request.getParameter("param2");

        // 验证参数的格式和规则
        // ...

        Boolean isValid = Boolean.TRUE;
        return isValid;
    }
}