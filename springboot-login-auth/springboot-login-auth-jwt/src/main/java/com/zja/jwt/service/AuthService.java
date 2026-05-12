package com.zja.jwt.service;

import com.zja.jwt.exception.InvalidTokenException;
import com.zja.jwt.model.AuthResponse;
import com.zja.jwt.model.TokenType;
import com.zja.jwt.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证业务服务
 * <p>
 * 职责：封装登录/刷新/登出的完整业务逻辑，Controller只做HTTP层转发
 *
 * @Author: zhengja
 * @Date: 2025-07-11
 */
@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenStoreService tokenStoreService;

    public AuthService(AuthenticationManager authManager,
                       JwtTokenUtil jwtTokenUtil,
                       TokenStoreService tokenStoreService) {
        this.authManager = authManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenStoreService = tokenStoreService;
    }

    /**
     * 用户登录
     */
    public AuthResponse login(String username, String password) {
        // 1. 认证
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. 提取角色（去除ROLE_前缀，JWT中只存纯角色名）
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());

        // 3. 生成双Token（AccessToken和RefreshToken均包含角色信息）
        String accessToken = jwtTokenUtil.generateToken(username, TokenType.ACCESS, roles);
        String refreshToken = jwtTokenUtil.generateToken(username, TokenType.REFRESH, roles);

        // 4. 存储RefreshToken到Redis
        tokenStoreService.storeRefreshToken(username, refreshToken, jwtTokenUtil.getRefreshTokenExpire());

        log.info("用户登录成功: {}", username);
        return new AuthResponse(accessToken, refreshToken, username, roles);
    }

    /**
     * 刷新Token（RefreshToken轮转）
     */
    public AuthResponse refreshToken(String refreshToken) {
        // 1. 验证Token类型
        if (!TokenType.REFRESH.name().equals(jwtTokenUtil.getTokenType(refreshToken))) {
            throw new InvalidTokenException("非法的RefreshToken");
        }

        // 2. 获取用户名和角色
        Claims claims = jwtTokenUtil.parseToken(refreshToken);
        String username = jwtTokenUtil.getUsername(claims);
        List<String> roles = jwtTokenUtil.getRoles(claims);

        // 3. 验证Redis中的RefreshToken
        if (!tokenStoreService.validateRefreshToken(username, refreshToken)) {
            throw new InvalidTokenException("RefreshToken已失效");
        }

        // 4. 生成新的双Token（轮转：旧RefreshToken失效，颁发新的双Token）
        String newAccessToken = jwtTokenUtil.generateToken(username, TokenType.ACCESS, roles);
        String newRefreshToken = jwtTokenUtil.generateToken(username, TokenType.REFRESH, roles);

        // 5. 存储新RefreshToken
        tokenStoreService.storeRefreshToken(username, newRefreshToken, jwtTokenUtil.getRefreshTokenExpire());

        log.info("Token刷新成功: {}", username);
        return new AuthResponse(newAccessToken, newRefreshToken, username, roles);
    }

    /**
     * 用户登出
     */
    public void logout(String accessToken) {
        try {
            Claims claims = jwtTokenUtil.parseToken(accessToken);
            String username = jwtTokenUtil.getUsername(claims);
            long remainingTime = jwtTokenUtil.getRemainingTime(claims);

            // 1. 将AccessToken加入黑名单（剩余有效期后自动清理）
            tokenStoreService.blacklistAccessToken(accessToken, remainingTime);

            // 2. 删除RefreshToken
            tokenStoreService.deleteRefreshToken(username);

            log.info("用户登出成功: {}", username);
        } catch (Exception e) {
            log.warn("登出处理异常: {}", e.getMessage());
        }
    }
}
