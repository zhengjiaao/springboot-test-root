package com.zja;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-05-31 14:10
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTests {

    @Autowired
    RedisTemplate redisTemplate;


}
