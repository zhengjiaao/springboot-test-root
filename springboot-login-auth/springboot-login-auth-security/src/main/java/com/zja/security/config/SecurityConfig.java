package com.zja.security.config;

import com.zja.security.handler.SecurityAccessDeniedHandler;
import com.zja.security.handler.SecurityAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 安全配置（Session模式）
 * <p>
 * 基于Session/Cookie的认证方式，支持记住我、CSRF防护、权限控制
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final CustomUserDetailsService userDetailsService;

    @Value("${security.remember-me.key:unique-and-secret-key}")
    private String rememberMeKey;

    public SecurityConfig(SecurityAuthenticationEntryPoint authenticationEntryPoint,
                          SecurityAccessDeniedHandler accessDeniedHandler,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CORS配置
                .cors().and()
                // CSRF防护（Session模式建议开启，前后端分离可关闭）
                .csrf().disable()
                // 基于Session的认证
                .sessionManagement()
                .sessionFixation().migrateSession()
                .maximumSessions(1)          // 同一用户只允许一个Session
                .maxSessionsPreventsLogin(false)  // 新登录踢掉旧Session
                .and().and()
                // 异常处理
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)  // 未认证处理
                .accessDeniedHandler(accessDeniedHandler)            // 权限不足处理
                .and()
                // 请求授权配置
                .authorizeRequests()
                // 登录接口公开访问
                .antMatchers("/api/auth/login", "/api/auth/info").permitAll()
                // 公开演示接口
                .antMatchers("/api/demo/public").permitAll()
                // Swagger文档公开访问
                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                        "/swagger-resources/**", "/webjars/**").permitAll()
                // 静态资源公开访问（前端页面由JS控制认证跳转）
                .antMatchers("/", "/login.html", "/index.html", "/product.html", "/user.html",
                        "/js/**", "/css/**", "/favicon.ico").permitAll()
                // 其他请求需认证
                .anyRequest().authenticated()
                .and()
                // 记住我配置
                .rememberMe()
                .key(rememberMeKey)
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(7 * 24 * 3600)  // 7天有效期
                .and()
                // 登出配置
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(200);
                    response.getWriter().write("{\"code\":200,\"message\":\"登出成功\"}");
                })
                .deleteCookies("JSESSIONID", "remember-me")
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 同源部署无需跨域，保留CORS配置以备前后端分离场景
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
