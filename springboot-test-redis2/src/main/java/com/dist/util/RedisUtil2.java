package com.dist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据源2 redisTemplate2封装
 * @author zhengja@dist.com.cn
 * @data 2019/7/29 10:17
 */
@Component  // implements ApplicationContextAware
public class RedisUtil2 extends RedisService {

    /*@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisTemplate<String, Object> redisTemplate2 = (RedisTemplate<String, Object>)applicationContext.getBean("redisTemplate2");
        this.setRedisTemplate(redisTemplate2);
    }*/

    @Autowired
    @Override
    public void setRedisTemplate(@Qualifier("redisTemplate2") RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
    }
}
