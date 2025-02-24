package com.zja.redis.redisson.service;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式 Map：跨服务共享的键值存储。
 * <p>
 * 数据一致性：分布式集合（如 RMap）需考虑最终一致性，重要数据建议配合数据库使用
 *
 * @Author: zhengja
 * @Date: 2025-02-24 10:24
 */
@Service
public class MapService {

    @Autowired
    private RedissonClient redisson;

    public void operateDistributedMap() {
        RMap<String, String> map = redisson.getMap("distributedMap");
        map.put("key1", "value1");
        String value = map.get("key1");
        System.out.println("Value from map: " + value); // 输出 value1
    }
}