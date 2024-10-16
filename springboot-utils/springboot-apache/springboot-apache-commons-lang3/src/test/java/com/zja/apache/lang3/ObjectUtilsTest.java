package com.zja.apache.lang3;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: zhengja
 * @Date: 2024-10-16 10:10
 */
public class ObjectUtilsTest {

    @Test
    public void _test1() {
        // 判断是否为空
        System.out.println(ObjectUtils.isEmpty(null)); // true
        System.out.println(ObjectUtils.isEmpty("")); // true
        System.out.println(ObjectUtils.isEmpty(" ")); // false
        System.out.println(ObjectUtils.isEmpty(new Object[]{})); // true
        System.out.println(ObjectUtils.isEmpty(new Object[]{"aaa", "bbb"})); // false
        System.out.println(ObjectUtils.isEmpty(new int[]{}));  // true
        System.out.println(ObjectUtils.isEmpty(new int[]{1})); // false

        System.out.println("-----------------------");

        // 判断是否非空
        System.out.println(ObjectUtils.isNotEmpty(null)); // false
        System.out.println(ObjectUtils.isNotEmpty(""));  // false
        System.out.println(ObjectUtils.isNotEmpty(" "));  // true
        System.out.println(ObjectUtils.isNotEmpty(new Object[]{}));  // false
        System.out.println(ObjectUtils.isNotEmpty(new Object[]{"aaa", "bbb"}));  // true
        System.out.println(ObjectUtils.isNotEmpty(new int[]{}));  // false
    }

    @Test
    public void _test2() {
        // 返回一个默认值
        System.out.println(ObjectUtils.defaultIfNull(null, "default"));
        System.out.println(ObjectUtils.defaultIfNull("not null", "default"));
        System.out.println(Optional.ofNullable(ObjectUtils.defaultIfNull(null, null)).isPresent());
        System.out.println(ObjectUtils.defaultIfNull(null, ""));
        System.out.println(ObjectUtils.defaultIfNull(null, 1));
        System.out.println(ObjectUtils.defaultIfNull(null, new Object()));
        System.out.println(Arrays.toString(ObjectUtils.defaultIfNull(null, new Object[]{"aaa", "bbb"})));
        System.out.println(Arrays.toString(ObjectUtils.defaultIfNull(null, new Object[]{})));
    }

    @Test
    public void _test3() {
        // 比较两个对象的值是否相等
        System.out.println(ObjectUtils.notEqual(null, null)); // false
        System.out.println(ObjectUtils.notEqual(new Object(), null)); // true
        System.out.println(ObjectUtils.notEqual("aaa", null)); // true
        System.out.println(ObjectUtils.notEqual(new Object(), new Object())); // true
        System.out.println(ObjectUtils.notEqual("aaa", "bbb")); // true
        System.out.println(ObjectUtils.notEqual(new Object[]{"aaa", "bbb"}, new Object[]{"aaa", "bbb"})); // true
    }

    // firstNonNull 返回第一个非null的值，如果没有非null值，则返回null。
    @Test
    public void _test4() {
        // 返回第一个非null的值，如果没有非null值，则返回null。
        System.out.println(ObjectUtils.firstNonNull("value1", "value2", "value3")); // value1
        System.out.println(ObjectUtils.firstNonNull("value1", null, "value3")); // value1
        System.out.println(ObjectUtils.firstNonNull(null, "value2", "value3")); // value2
        System.out.println(ObjectUtils.firstNonNull(null, null, "value3")); // value3
        System.out.println(ObjectUtils.firstNonNull(null, "value3")); // value3

        System.out.println(Optional.ofNullable(ObjectUtils.firstNonNull(null, null, null)).isPresent()); // Optional.empty false
    }

    @Test
    public void _test5() {
        // 计算对象的哈希值
        System.out.println(ObjectUtils.hashCodeHex(null)); // 0
        System.out.println(ObjectUtils.hashCodeHex(new Object())); // 5b12b668
        System.out.println(ObjectUtils.hashCodeHex("aaa")); // 17841
        System.out.println(ObjectUtils.hashCodeHex(new Object[]{"aaa", "bbb"})); // 1165b38
        System.out.println(ObjectUtils.hashCodeHex(new Object[]{})); // 4c12331b
    }

    @Test
    public void _test6() {

        // 比较两个对象的值，返回一个整数，表示第一个对象的值是否小于、等于或大于第二个对象的值。
        System.out.println(ObjectUtils.compare(null, null)); // 0
        System.out.println(ObjectUtils.compare(null, "")); // -1
        System.out.println(ObjectUtils.compare("", "")); // 0
        System.out.println(ObjectUtils.compare("", "aaa")); // -3
        System.out.println(ObjectUtils.compare("aaa", "bbb")); // -1
        System.out.println(ObjectUtils.compare("aaa", "aaa")); // 0

        System.out.println("-------------------------------");

        // 返回对象的最大值
        System.out.println(Optional.ofNullable(ObjectUtils.max(null, null)).isPresent()); // false
        System.out.println(ObjectUtils.max(null, "")); // --> ""
        System.out.println(ObjectUtils.max("", "")); // --> ""
        System.out.println(ObjectUtils.max("", "aaa")); // --> aaa
        System.out.println(ObjectUtils.max("aaa", "bbb")); // --> bbb
        System.out.println(ObjectUtils.max("aaa", "aaa")); // --> aaa

        System.out.println("-------------------------------");

        // 返回对象的最小值
        System.out.println(Optional.ofNullable(ObjectUtils.min(null, null)).isPresent()); // false
        System.out.println(ObjectUtils.min(null, "")); // --> ""
        System.out.println(ObjectUtils.min("", "")); // --> ""
        System.out.println(ObjectUtils.min("", "aaa")); // --> ""
        System.out.println(ObjectUtils.min("aaa", "bbb")); // --> aaa
        System.out.println(ObjectUtils.min("aaa", "aaa")); // --> aaa
    }

    @Test
    public void _test8() {
        // 返回对象的字符串表示形式
        System.out.println(ObjectUtils.identityToString(null)); // --> null
        System.out.println(ObjectUtils.identityToString("")); // --> java.lang.String@5b12b668
        System.out.println(ObjectUtils.identityToString("aaa")); // --> java.lang.String@5b12b632
        System.out.println(ObjectUtils.identityToString(new Object())); // --> java.lang.Object@5b12b625
        System.out.println(ObjectUtils.identityToString(new Object[]{"aaa", "bbb"}));
        System.out.println(ObjectUtils.identityToString(new Object[]{}));
    }

}
