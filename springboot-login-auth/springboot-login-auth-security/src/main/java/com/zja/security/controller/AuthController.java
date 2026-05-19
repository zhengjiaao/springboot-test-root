package com.zja.security.controller;

import com.zja.security.config.CustomUserDetailsService;
import com.zja.security.model.ChangePasswordRequest;
import com.zja.security.model.LoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录认证 接口层
 * <p>
 * 基于Spring Security Session模式的登录认证
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/auth")
@Api(tags = {"登录认证管理"})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request,
                                                      HttpServletRequest httpRequest,
                                                      HttpServletResponse httpResponse) {
        // 1. 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. 将认证信息存入SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 创建Session
        httpRequest.getSession(true);

        // 4. 处理记住我
        if (Boolean.TRUE.equals(request.getRememberMe())) {
            // 设置记住我Cookie
            String rememberMeKey = "unique-and-secret-key-for-remember-me";
            TokenBasedRememberMeServices rememberMeServices =
                    new TokenBasedRememberMeServices(rememberMeKey,
                            username -> (UserDetails) authentication.getPrincipal());
            rememberMeServices.setTokenValiditySeconds(7 * 24 * 3600);
            rememberMeServices.loginSuccess(httpRequest, httpResponse, authentication);
        }

        // 5. 构建响应
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("username", userDetails.getUsername());
        data.put("roles", roles);
        data.put("sessionId", httpRequest.getSession().getId());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", data);

        log.info("用户登录成功: {}", userDetails.getUsername());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public ResponseEntity<Void> logout() {
        // SecurityConfig中已配置logout处理，此接口作为显式登出入口
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    @ApiOperation("修改当前用户密码")
    public ResponseEntity<Map<String, Object>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {
        userDetailsService.changePassword(
                userDetails.getUsername(), request.getOldPassword(), request.getNewPassword());
        log.info("用户修改密码成功: {}", userDetails.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "密码修改成功，请重新登录");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info")
    @ApiOperation("获取当前登录用户信息")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "未登录");
            result.put("data", new HashMap<>());
            return ResponseEntity.ok(result);
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("username", userDetails.getUsername());
        data.put("roles", roles);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);

        return ResponseEntity.ok(result);
    }
}
