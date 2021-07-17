package com.zja;

import com.google.common.base.Stopwatch;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    /**
     * 计时工具类
     */
    public static Stopwatch stopwatch = Stopwatch.createUnstarted();

}
