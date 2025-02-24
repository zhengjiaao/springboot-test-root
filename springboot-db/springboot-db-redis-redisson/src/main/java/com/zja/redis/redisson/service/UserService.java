package com.zja.redis.redisson.service;

import com.zja.redis.redisson.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 分布式缓存: 整合 Spring Cache 实现自动缓存。
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:26
 */
@Service
public class UserService {


    // 查询缓存
    @Cacheable(value = "userCache", key = "#id")
    public User getUserById(String id) {
        // 模拟数据库查询
        System.out.println("Querying database for user: " + id);
        return new User(id, "User " + id);
    }

    // 增加或更新
    @CachePut(value = "userCache", key = "#user.id")
    public User saveUser(User user) {
        // 模拟数据库保存操作
        System.out.println("Saving user to database: " + user);
        return user;
    }

    // 删除
    @CacheEvict(value = "userCache", key = "#id")
    public void deleteUser(String id) {
        // 模拟数据库删除操作
        System.out.println("Deleting user from database: " + id);
    }

}