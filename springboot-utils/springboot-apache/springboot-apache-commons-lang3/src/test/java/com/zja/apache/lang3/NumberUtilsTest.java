package com.zja.apache.lang3;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * NumberUtils： 提供了处理数字类型的实用方法，如将字符串转换为数字、数值比较、范围检查等。
 *
 * @Author: zhengja
 * @Date: 2024-10-16 9:36
 */
public class NumberUtilsTest {

    @Test
    public void _test1() {
        // 返回一组整数中的最大值、最小值。
        int max = NumberUtils.max(5, 8, 2, 10);
        int min = NumberUtils.min(5, 8, 2, 10);
        System.out.println("max = " + max);
        System.out.println("min = " + min);

        // 比较两个数字的大小
        int compare = NumberUtils.compare(5, 8);
        System.out.println("compare = " + compare);

        // 判断字符串是否为数字
        boolean isDigits = NumberUtils.isDigits("123");
        System.out.println("isDigits = " + isDigits);

        // 判断字符串是否可以解析为数字
        boolean isParsable = NumberUtils.isParsable("123");
        System.out.println("isParsable = " + isParsable);

        // 将字符串转换为数字
        double aDouble = NumberUtils.toDouble("123"); // 123.0
        System.out.println("aDouble = " + aDouble);
        int anInt = NumberUtils.toInt("123");
        System.out.println("anInt = " + anInt);

        // 创建BigDecimal对象
        BigDecimal bigDecimal = NumberUtils.createBigDecimal("123"); // 123
        System.out.println("bigDecimal = " + bigDecimal);

        Number number = NumberUtils.createNumber("123");
        System.out.println("number = " + number);
    }
}
