package com.zja.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

/**
 * Security拦截swagger3地址，需认证访问
 *
 * @author: zhengja
 * @since: 2024/04/24 15:34
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 设置Security用户
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")      // 用户：username
                .password("{noop}password")     // 密码：password，使用 "{noop}" 前缀表示密码不进行加密
                // .roles("ADMIN", "USER")         // 角色：给用户赋予了 "USER" 角色
                .roles("USER")         // 角色：给用户赋予了 "USER" 角色
                .and()
                .withUser("user")       // 普通用户
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("USER");
        // 只有第一个用户有权限访问 swagger3地址。
    }

    // Security拦截Swagger3地址，需登录后才能访问地址
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().defaultSuccessUrl("/swagger-ui/index.html") // 设置成功登录后的跳转地址
                .and()
                .csrf().disable(); // 禁用了 CSRF 保护，以便在 Swagger UI 中进行请求。
    }

    // SecurityConfiguration 是一个用于配置 Swagger 3 安全性的类
/*
    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder()
                .clientId("your-client-id")         // 设置客户端 ID，用于进行身份验证。
                .clientSecret("your-client-secret") // 设置客户端密钥，用于进行身份验证。
                .realm("your-realm")                // 设置领域（Realm），表示所属领域或安全域。
                .appName("your-app-name")           // 设置应用程序名称。
                .scopeSeparator(",")                // 设置作用域分隔符。默认情况下，作用域使用空格分隔，这里将其设置为逗号分隔。
                .build();
    }
*/

}
