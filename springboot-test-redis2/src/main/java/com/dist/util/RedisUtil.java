package com.dist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 默认 redisTemplate封装
 * @author zhengja@dist.com.cn
 * @data 2019/7/29 10:17
 */
@Component
public class RedisUtil extends RedisService {

    /**
     * 使用注解方式
     * @param redisTemplate
     */
    @Autowired
    @Override
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
    }
}
