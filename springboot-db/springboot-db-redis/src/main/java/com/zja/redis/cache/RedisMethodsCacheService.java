package com.zja.redis.cache;

/**
 * @author ZhengJa
 * @since: 2019/11/13
 */
public interface RedisMethodsCacheService {
    String getData();

    String saveData(String parameter);

    String getData(String parameter);

    String putData(String parameter);

    String deleteData(String parameter);

    String deleteAllData(String parameter);
}
