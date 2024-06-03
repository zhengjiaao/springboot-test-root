package com.zja.cache.jetcache.service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.zja.cache.jetcache.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * 简单用法：
 * <p>
 * User user = userCache.get(12345L);
 * userCache.put(12345L, loadUserFromDataBase(12345L));
 * userCache.remove(12345L);
 * userCache.computeIfAbsent(1234567L, (key) -> loadUserFromDataBase(1234567L));
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-05-31 10:10
 */
@Component
public class JetCacheAPI {

    @Autowired
    private CacheManager cacheManager;
    private Cache<String, User> userCache;

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("userCache").expire(Duration.ofSeconds(100)).cacheType(CacheType.BOTH) // two level cache 两级缓存(内存+远程redis)
                .localLimit(50).syncLocal(true) // invalidate local cache in all jvm process after update 更新后使所有jvm进程中的本地缓存无效
                .build();
        userCache = cacheManager.getOrCreateCache(qc);
    }

    // 模拟：从数据库加载用户
    private User loadUserFromDataBase(String userId) {
        System.out.println("从数据库加载用户：" + userId);
        return new User(userId, "李四" + userId, getRandomNum());
    }

    public void putUser(User user) {
        userCache.put(user.getUserId(), user);
    }

    public void putUserIfAbsent(String userId) {
        // userCache.putIfAbsent(userId, loadUserFromDataBase(userId));  //putIfAbsent is not supported by MultiLevelCache
    }

    public User getUser(String userId) {
        return userCache.get(userId);
    }

    public void deleteUser(String userId) {
        userCache.remove(userId);
    }

    // 若存在，则不更新缓存时间
    public void computeIfAbsent(String userId) {
        userCache.computeIfAbsent(userId, (key) -> loadUserFromDataBase(userId));
    }

    // 生成0～100的随机数
    private int getRandomNum() {
        return (int) (Math.random() * 100);
    }
}
