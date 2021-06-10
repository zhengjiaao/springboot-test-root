package com.zja.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-05-31 14:09
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void set() {
        redisUtil.set("mykey","value1");
    }

    @Test
    public void expire() {
        System.out.println(redisUtil.expire("mykey",60));
    }

    @Test
    public void get() {
        redisUtil.get("mykey");
    }

    @Test
    public void del() {
        redisUtil.del("mykey");
    }

}
