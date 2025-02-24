package com.zja.redis.redisson.service;

import com.zja.redis.redisson.model.User;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户认证集成（Redis 存储凭证）：用户详情服务
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:57
 */
@Service
public class RedisUserService {

    @Autowired
    private RedissonClient redissonClient;

    // 注册用户
    public void registerUser(String username, String password) {
        RMap<String, String> userMap = redissonClient.getMap("users");
        userMap.put(username, password);
    }

    // 根据用户名加载用户信息
    public User loadUserByUsername(String username) {
        RMap<String, String> userMap = redissonClient.getMap("users");
        String password = userMap.get(username);
        return new User(username, password);
    }

}
