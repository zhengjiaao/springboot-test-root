/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-06-07 14:35
 * @Since:
 */
package com.zja.java8.tools;

import org.assertj.core.util.DateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtilTests {

    @Test
    public void test() {
        System.out.println("昨天:" + DateUtil.yesterday());
        System.out.println("当前年:" + DateUtil.yearOf(new Date()));
        System.out.println("当前月:" + DateUtil.monthOf(new Date()));
    }
}
