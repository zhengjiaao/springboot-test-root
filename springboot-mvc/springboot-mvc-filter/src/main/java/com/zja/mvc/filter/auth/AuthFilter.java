package com.zja.mvc.filter.auth;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhengja
 * @since: 2024/04/24 16:30
 */
@Slf4j
public class AuthFilter implements Filter {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public AuthFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----AuthFilter init()----");
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        HandlerExecutionChain executionChain = this.requestMappingHandlerMapping.getHandler(request);
        if (executionChain == null) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        HandlerMethod method = (HandlerMethod) executionChain.getHandler();
        if (method.hasMethodAnnotation(UnAuth.class)) { // 存在 UnAuth 注解，则跳过认证
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authToken = request.getHeader("custom-auth-token");
        if (StringUtils.hasText(authToken)) {
            if (authToken.equals("default_auth_token")) { // 存在 "auth-token" 值为 "default_auth_token"，则跳过认证
                chain.doFilter(servletRequest, servletResponse);
                return;
            } else {
                this.unauthorizedAccessToResponse(response, "无效token，无权限访问!");
            }
        }

        Object user = session.getAttribute("user");
        if (ObjectUtils.isEmpty(user)) { // 用户未登录，认证不通过，返回错误提示：用户未登录，无权限访问!
            log.error("用户未登录，无权限访问!");
            this.unauthorizedAccessToResponse(response, "用户未登录，无权限访问!");
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("----AuthFilter destroy()----");
    }

    /**
     * 未经授权访问响应
     *
     * @param response 响应
     * @param msg      消息
     */
    private void unauthorizedAccessToResponse(HttpServletResponse response, String msg) {
        try (OutputStream os = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            // ObjectMapper objectMapper = new ObjectMapper();
            // objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 格式化
            // String serializedJson = objectMapper.writeValueAsString(msg);
            // response.setContentType("application/json;charset=UTF-8");
            // os.write(serializedJson.getBytes(StandardCharsets.UTF_8));

            response.setContentType("text/plain;charset=UTF-8");
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            log.error("用户未登录，无权限访问!", e);
        }
    }

}
