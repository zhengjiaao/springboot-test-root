package com.zja.controller;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.zja.SpringbootTestApplication;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-28 14:01
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
//@Rollback //事务自动回滚，不自动回滚@Rollback(false)
//@Transactional //启用事务，默认 @Rollback(true)
//@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootTestApplication.class)
public class BaseTests {

    public static Logger log = LoggerFactory.getLogger(BaseTests.class);

    public Stopwatch stopwatch; //测试类的计时工具

    @Before
    public void beforeTheMethod() {
        stopwatch = Stopwatch.createStarted();
    }

    @After
    public void afterTheMethod() {
        log.info("总耗时：{}", stopwatch);
    }


    /**
     * 输出任何对象
     * @param obj
     * @param <T>
     *     将任何对象转换为json字符串，并打印
     */
    protected <T> void printlnAnyObj(T obj) {
        printlnAnyObj(null, obj);
    }

    /**
     * 输出任何对象
     * @param key 输出对象的key 区分多个对象的输出结果
     * @param obj
     * @param <T>
     *     将任何对象转换为json字符串，并打印
     */
    protected <T> void printlnAnyObj(String key, T obj) {

        if (isEmpty(obj)) {
            System.out.println(key + " obj is null！");
            return;
        }

        if (StringUtils.isNotEmpty(key)) {
            System.out.print(key + "：");
        }

        //将任何对象都转换为json字符串输出
        String jsonString = "";
        if (obj instanceof CharSequence) {
            CharSequence sequence = (CharSequence) obj;
            jsonString = sequence.toString();
        } else if (obj instanceof Map) {
            jsonString = JSON.toJSONString((Map) obj, true);
        } else if (obj instanceof Iterable) {
            jsonString = JSON.toJSONString((Iterable) obj, true);
        } else if (obj instanceof Iterator) {
            jsonString = JSON.toJSONString((Iterator) obj, true);
        } else if (ArrayUtil.isArray(obj)) {
            jsonString = JSON.toJSONString(obj, true);
        } else if (obj instanceof Object) {
            jsonString = JSON.toJSONString(obj, true);
        }else {
            System.out.println(obj.toString());
        }
        System.out.println(jsonString);
    }

    /**
     * 判断是否为null
     * @param obj
     * @return
     */
    protected static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return StrUtil.isEmpty((CharSequence) obj);
        } else if (obj instanceof Map) {
            return MapUtil.isEmpty((Map) obj);
        } else if (obj instanceof Iterable) {
            return IterUtil.isEmpty((Iterable) obj);
        } else if (obj instanceof Iterator) {
            return IterUtil.isEmpty((Iterator) obj);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.isEmpty(obj);
        }

        return false;
    }
}
