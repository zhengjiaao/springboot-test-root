package com.zja.juc.threadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2024/03/27 17:07
 */
public class ThreadLocalExample3 {

    public static void main(String[] args) throws InterruptedException {
        ThreadCache.put("key1", "value1");

        Object o = ThreadCache.get("key1");
        System.out.println(o);

        Thread thread1 = new Thread(() -> {
            // 设置线程本地变量的值
            ThreadCache.put("key2", "value2");

            // 访问线程本地变量
            Object o2 = ThreadCache.get("key2");
            System.out.println(o2);

            // 访问主线程变量
            Object o3 = ThreadCache.get("key1");
            System.out.println(o3);
        });

        thread1.start();

        Thread.sleep(1000 * 3);
    }

    /**
     * 线程级别的缓存
     */
    public static class ThreadCache {
        private static final ThreadLocal<Map<String, Object>> cache = new ThreadLocal<Map<String, Object>>();

        public static void put(String key, Object value) {
            if (!cacheIsNull()) {
                cache.get().put(key, value);
            } else {
                Map<String, Object> vmap = new HashMap<>();
                vmap.put(key, value);
                cache.set(vmap);
            }
        }

        public static Object get(String key) {
            Map<String, Object> map = cache.get();
            if (cacheIsNull())
                return null;
            if (map.containsKey(key)) {
                return map.get(key);
            }
            return null;
        }

        public static void clear() {
            cache.remove();
        }

        private static boolean cacheIsNull() {
            return cache.get() == null;
        }
    }
}
