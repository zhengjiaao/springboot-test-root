package com.zja.jwt.util;

import com.zja.jwt.model.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * JWT Token 工具类
 * <p>
 * 职责：Token的生成、解析、验证
 * 设计原则：单次解析，避免重复parse；构造器注入配置
 *
 * @Author: zhengja
 * @Date: 2025-07-11 13:38
 */
@Slf4j
@Component
public class JwtTokenUtil {

    private final String secret;
    private final long accessTokenExpire;
    private final long refreshTokenExpire;

    public JwtTokenUtil(@org.springframework.beans.factory.annotation.Value("${jwt.secret}") String secret,
                        @org.springframework.beans.factory.annotation.Value("${jwt.access-token-expire}") long accessTokenExpire,
                        @org.springframework.beans.factory.annotation.Value("${jwt.refresh-token-expire}") long refreshTokenExpire) {
        this.secret = secret;
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
    }

    // ========== Token 生成 ==========

    /**
     * 生成Token（含角色权限信息）
     */
    public String generateToken(String username, TokenType type, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type.name());
        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles);
        }

        long expire = type == TokenType.ACCESS ? accessTokenExpire : refreshTokenExpire;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成Token（不含角色）
     */
    public String generateToken(String username, TokenType type) {
        return generateToken(username, type, null);
    }

    // ========== Token 解析（单次解析，返回Claims复用） ==========

    /**
     * 解析Token，返回Claims
     * 注意：调用方应捕获JwtException/ExpiredJwtException处理异常情况
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // ========== Token 验证（组合方法，一次解析完成所有校验） ==========

    /**
     * 验证AccessToken的完整有效性
     * 一次性完成：签名验证、Token类型检查、过期检查
     *
     * @param token AccessToken字符串
     * @return 解析后的Claims，验证通过时非null
     * @throws io.jsonwebtoken.JwtException Token无效或类型不匹配时抛出
     */
    public Claims validateAccessToken(String token) {
        Claims claims = parseToken(token);
        String type = (String) claims.get("type");
        if (!TokenType.ACCESS.name().equals(type)) {
            throw new io.jsonwebtoken.JwtException("非法Token类型，期望ACCESS实际为" + type);
        }
        return claims; // 未过期则正常返回（parseToken内部已验证签名和过期）
    }

    // ========== Claims 信息提取（接收已解析的Claims，避免重复解析） ==========

    /**
     * 从Claims中获取用户名
     */
    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    /**
     * 从Claims中获取角色列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return (List<String>) roles;
        }
        return Collections.emptyList();
    }

    /**
     * 从Claims中获取Token剩余有效时间（毫秒）
     */
    public long getRemainingTime(Claims claims) {
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    /**
     * 从Token中获取用户名（仍支持直接解析，供非Filter场景使用）
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 从Token中获取类型（仍支持直接解析）
     */
    public String getTokenType(String token) {
        return (String) parseToken(token).get("type");
    }

    // ========== 配置访问 ==========

    public long getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public long getRefreshTokenExpire() {
        return refreshTokenExpire;
    }
}
