/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 10:58
 * @Since:
 */
package com.zja.caches;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author: zhengja
 * @since: 2023/11/07 10:58
 */
public class CacheUnitTest {

    @Test
    public void testCache() {
        // 创建缓存
        Cache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(100) //最大缓存数量
                .expireAfterWrite(1, TimeUnit.MINUTES) //写入后的过期时间 1分钟后过期
                .build();

        // 向缓存中放入键值对
        cache.put("key1", 1);
        cache.put("key2", 2);
        cache.put("key3", 3);

        // 从缓存中获取值
        Integer value1 = cache.getIfPresent("key1");
        Integer value2 = cache.getIfPresent("key2");
        Integer value3 = cache.getIfPresent("key3");
        Integer value4 = cache.getIfPresent("key4");

        // 验证缓存中的值
        assertEquals(1, value1);
        assertEquals(2, value2);
        assertEquals(3, value3);
        assertNull(value4);

        // 移除缓存中的键值对
        cache.invalidate("key3");

        // 从缓存中获取被移除的值
        Integer removedValue = cache.getIfPresent("key3");

        // 验证被移除的值为null
        assertNull(removedValue);
    }

    //缓存策略的示例
    public void CacheStrategy() {
        //1.最大大小策略：设置缓存的最大大小，当缓存中的键值对数量超过最大大小时，旧的键值对将被淘汰。这可以通过 maximumSize() 方法来设置。
        Cache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)  // 设置缓存的最大大小为1000
                .build();

        //2.过期时间策略：设置缓存中的键值对在一定时间后过期。使用 expireAfterWrite() 方法设置写入后的过期时间，或使用 expireAfterAccess() 方法设置访问后的过期时间。
        Cache<String, Integer> cache2 = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 写入后的过期时间为10分钟
                .build();

        //3.定时刷新策略：设置缓存中的键值对定期刷新，以确保数据始终保持最新。使用 refreshAfterWrite() 方法设置写入后的刷新时间。
        Cache<String, Integer> cache3 = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.HOURS)  // 写入后的刷新时间为1小时
                .build();

        //4.缓存移除监听器：可以添加缓存移除监听器，以便在缓存中的键值对被移除时执行自定义逻辑。可以使用 removalListener() 方法添加监听器。
        Cache<String, Integer> cache4 = CacheBuilder.newBuilder()
                .removalListener(notification -> {
                    // 缓存移除时的自定义逻辑
                    System.out.println("Key: " + notification.getKey() + " was removed from cache.");
                }).build();

    }
}
