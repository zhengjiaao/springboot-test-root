package com.zja.bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-05-31 15:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Redis的map数据结构来存储数据，可以大大减少JVM内存开销，而且不需要每次重启都要往集合中存储数据
 */
public class RedissonBloomFilterTests {

    public static void main(String[] args) {
        redissonBloomFilter();
//        googleBloomFilter();
    }

    //分布式服务下的布隆过滤器（Redis实现）
    public static void redissonBloomFilter() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("user");
        // 初始化布隆过滤器，预计统计元素数量为55000000，期望误差率为0.03
        bloomFilter.tryInit(55000000L, 0.03);
        bloomFilter.add("Tom");
        bloomFilter.add("Jack");
        System.out.println(bloomFilter.count());   //2
        System.out.println(bloomFilter.contains("Tom"));  //true
        System.out.println(bloomFilter.contains("Linda"));  //false
    }

    //单机版 googleBloomFilter
    public static void googleBloomFilter() {
        //后边两个参数：预计包含的数据量，和允许的误差值，默认是0.03
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 100000, 0.01);
        for (int i = 0; i < 100000; i++) {
            bloomFilter.put(i);
        }
        System.out.println(bloomFilter.mightContain(1)); //TRUE
        System.out.println(bloomFilter.mightContain(2)); //TRUE
        System.out.println(bloomFilter.mightContain(3)); //TRUE
        System.out.println(bloomFilter.mightContain(100001)); //FALSE
        //bloomFilter.writeTo();
    }
}
