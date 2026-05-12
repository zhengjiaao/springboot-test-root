package com.zja.jwt.controller;

import com.zja.jwt.exception.InvalidTokenException;
import com.zja.jwt.model.AuthResponse;
import com.zja.jwt.model.LoginRequest;
import com.zja.jwt.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 登录认证 接口层
 * <p>
 * 职责：仅处理HTTP协议层（参数接收、响应封装），业务逻辑委托AuthService
 *
 * @author: zhengja
 * @since: 2025/07/11 13:41
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/auth")
@Api(tags = {"登录认证管理"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @ApiOperation("刷新Token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = extractBearerToken(authHeader);
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = extractBearerToken(authHeader);
        authService.logout(accessToken);
        return ResponseEntity.ok().build();
    }

    /**
     * 从Authorization头中提取Bearer Token
     */
    private String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("缺少有效的Authorization头");
        }
        return authHeader.substring(7);
    }
}
