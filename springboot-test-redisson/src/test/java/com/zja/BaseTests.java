package com.zja;

import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-05-31 14:43
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class BaseTests {

    @Resource
    protected RedissonClient redisson;

    @Resource
    protected RedissonReactiveClient redissonReactiveClient;

    @Resource
    protected RedissonRxClient redissonRxClient;




}
