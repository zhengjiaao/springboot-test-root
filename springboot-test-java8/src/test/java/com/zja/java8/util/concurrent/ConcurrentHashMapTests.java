package com.zja.java8.util.concurrent;

import com.google.common.base.Stopwatch;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-11 13:48
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 *
 * Java8 ConcurrentHashMap 源码真心不简单 引入了红黑树，最难的在于扩容，数据迁移操作不容易看懂
 */
public class ConcurrentHashMapTests {

    /**
     * 计时工具类
     */
    public static Stopwatch stopwatch = Stopwatch.createUnstarted();

    private final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    @Before
    public void beforeTest() {
        //一千万条数据
//        for (int i = 1; i <= 10000000; ++i) {
        //十万条数据
        for (int i = 1; i <= 100000; ++i) {
            concurrentHashMap.put("key" + i, i);
        }
        System.out.println(concurrentHashMap.size());
    }

    @Test
    public void test() {
        stopwatch.start();
        concurrentHashMap.put("aa",1);
        System.out.println(concurrentHashMap.get("key1000"));
        stopwatch.stop();
        System.out.println("耗时: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

}
