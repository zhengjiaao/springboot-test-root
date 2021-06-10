package com.zja.config;

import com.zja.codec.FastjsonCodec;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 13:20
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Configuration
public class RedissonConfig {

    //Redisson 原生(支持异步)客户端
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.159.128:6379");
//                .setAddress("redis://localhost:6379");

        //默认JsonJackson json序列化方式  x1Ap\x00\x00\x00\x01
        //Codec codec = new JsonJacksonCodec();

        //自定义序列化方式 "a": 1
        Codec codec = new FastjsonCodec();
        config.setCodec(codec);

        //默认 connects to 127.0.0.1:6379
//        return Redisson.create();
        return Redisson.create(config);
    }

    //Redisson 反应式客户端
    @Bean
    public RedissonReactiveClient redissonReactiveClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.159.128:6379");
//                .setAddress("redis://localhost:6379");

        return Redisson.createReactive(config);
    }

    //Redisson RxJava2 客户端
    @Bean
    public RedissonRxClient redissonRxClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.159.128:6379");
//                .setAddress("redis://localhost:6379");

        return Redisson.createRx(config);
    }


}
