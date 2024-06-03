package com.zja.cache.jetcache.service;

import com.alicp.jetcache.anno.*;
import com.zja.cache.jetcache.model.User;
import com.zja.cache.jetcache.model.UserRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 注意：
 * 为了使用诸如 之类的参数名称key="#userId"，您的 javac 编译器目标必须是 1.8 及以上版本，并且应该-parameters设置 。
 * 否则，请使用索引来访问参数，例如key="args[0]"
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-05-31 9:54
 */
public interface UserService {

    // expire = 3600表示该元素设置后3600秒过期，JetCache会自动根据所有参数生成缓存键
    // @Cached(expire = 3600, cacheType = CacheType.REMOTE)
    // User getUserById(String userId);

    // 使用属性通过SpELkey脚本指定缓存键
    @Cached(name = "userCache-", key = "#userId", expire = 100)
    User getUserById(String userId);

    @CacheUpdate(name = "userCache-", key = "#user.userId", value = "#user")
    void updateUser(User user);

    @CacheInvalidate(name = "userCache-111", key = "#userId")
    void deleteUser(String userId);

    // 自动刷新
    @Cached(expire = 60, cacheType = CacheType.REMOTE) // 一旦缓存项在创建后60秒没有被访问，它将会被自动移除。
    @CacheRefresh(refresh = 30, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
    // 缓存项每30秒自动刷新，除非最近60秒内没有被访问过。
    @CachePenetrationProtect
    // 注解表示在多线程环境下，缓存会被同步加载。在多线程环境中，如果缓存未命中，加载新数据到缓存是同步的，以防止穿透性缓存击穿问题。
    BigDecimal summaryOfToday(String categoryId);


    // 更多复杂示例 todo 未测试

    @Cached(name = "userCache-All", expire = 100)
    List<User> getAll();

    @Cached(name = "userCache-Map", expire = 100)
    Map<String, User> getMap();

    @Cached(name = "userCache-UserRequest", expire = 100)
    User getUser(UserRequest request);

    @Cached(name = "userCache-Request-List", expire = 100)
    User getUserById(List<String> ids);

    @CacheUpdate(name = "userCache-Request-Map", key = "#user.id", value = "#user")
    User getUserById(Map<String, Object> map);

}
