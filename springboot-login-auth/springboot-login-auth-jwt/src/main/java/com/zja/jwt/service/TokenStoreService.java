package com.zja.jwt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token 存储服务（Redis实现）
 * <p>
 * 职责：纯Redis存储操作，不依赖JWT解析逻辑
 * 调用方负责计算过期时间等业务参数
 *
 * @Author: zhengja
 * @Date: 2025-07-11 13:40
 */
@Slf4j
@Service
public class TokenStoreService {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String ACCESS_TOKEN_BLACKLIST_PREFIX = "access_token_blacklist:";

    public TokenStoreService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ========== RefreshToken 管理 ==========

    /**
     * 存储RefreshToken
     *
     * @param username       用户名
     * @param refreshToken   RefreshToken值
     * @param expireMillis   过期时间（毫秒）
     */
    public void storeRefreshToken(String username, String refreshToken, long expireMillis) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(key, refreshToken, expireMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 验证RefreshToken是否与Redis中存储的一致
     */
    public boolean validateRefreshToken(String username, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + username;
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    /**
     * 删除RefreshToken（登出时调用）
     */
    public void deleteRefreshToken(String username) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.delete(key);
    }

    // ========== AccessToken 黑名单管理 ==========

    /**
     * 将AccessToken加入黑名单
     *
     * @param accessToken     AccessToken值
     * @param remainingMillis 剩余有效时间（毫秒），过期后自动清理
     */
    public void blacklistAccessToken(String accessToken, long remainingMillis) {
        if (remainingMillis > 0) {
            String key = ACCESS_TOKEN_BLACKLIST_PREFIX + accessToken;
            redisTemplate.opsForValue().set(key, "1", remainingMillis, TimeUnit.MILLISECONDS);
            log.info("AccessToken已加入黑名单，剩余有效时间: {}ms", remainingMillis);
        }
    }

    /**
     * 检查AccessToken是否在黑名单中
     */
    public boolean isAccessTokenBlacklisted(String accessToken) {
        String key = ACCESS_TOKEN_BLACKLIST_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
