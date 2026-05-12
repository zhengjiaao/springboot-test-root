package com.zja.jwt.filters;

import com.zja.jwt.service.TokenStoreService;
import com.zja.jwt.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Token 过滤器
 * <p>
 * 职责：拦截请求，验证AccessToken有效性（签名+类型+过期+黑名单），构建SecurityContext
 * 设计原则：单次解析Token，复用Claims对象
 *
 * @Author: zhengja
 * @Date: 2025-07-11 13:45
 */
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final TokenStoreService tokenStoreService;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, TokenStoreService tokenStoreService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenStoreService = tokenStoreService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                // 1. 验证AccessToken（一次解析完成签名验证+类型检查+过期检查）
                Claims claims = jwtTokenUtil.validateAccessToken(token);

                // 2. 检查黑名单（已登出的Token）
                if (tokenStoreService.isAccessTokenBlacklisted(token)) {
                    log.warn("AccessToken已在黑名单中（已登出），拒绝访问");
                    chain.doFilter(request, response);
                    return;
                }

                // 3. 从Claims中提取用户信息（复用已解析的Claims，不重复解析）
                String username = jwtTokenUtil.getUsername(claims);
                List<String> roles = jwtTokenUtil.getRoles(claims);

                // 4. 构建认证信息（JWT中角色已不含ROLE_前缀，需补上以匹配Spring Security约定）
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException e) {
                // RefreshToken用于/api/auth/refresh接口属于正常场景，降低日志级别
                if (e.getMessage() != null && e.getMessage().contains("REFRESH")) {
                    log.debug("请求携带RefreshToken而非AccessToken，跳过认证设置");
                } else {
                    log.warn("JWT Token验证失败: {}", e.getMessage());
                }
            } catch (Exception e) {
                log.error("JWT Token处理异常: {}", e.getMessage(), e);
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
