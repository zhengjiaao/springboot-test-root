package com.zja.apache.lang3;

import org.apache.commons.lang3.RegExUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-16 13:21
 */
public class RegExUtilsTest {

    @Test
    public void _test1() {
        // 移除所有匹配
        System.out.println(RegExUtils.removeAll("abc", "b")); // ac
        System.out.println(RegExUtils.removeAll("abc", "^a")); // bc
        System.out.println(RegExUtils.removeAll("abc", "^a|c")); // b
    }

    @Test
    public void _test2() {
        // 替换所有匹配
        System.out.println(RegExUtils.replaceAll("abc", "b", "d")); // adc
        System.out.println(RegExUtils.replaceAll("abc", "^a", "d")); // dbc
        System.out.println(RegExUtils.replaceAll("abc", "^a|c", "d")); // adb
    }

    @Test
    public void _test3() {
        // 替换所有匹配
        System.out.println(RegExUtils.replacePattern("abc", "b", "d")); // adc
        System.out.println(RegExUtils.replacePattern("abc", "^a", "d")); // dbc
        System.out.println(RegExUtils.replacePattern("abc", "^a|c", "d")); // dbd
    }

    @Test
    public void _test4() {
        // 替换第一个匹配
        System.out.println(RegExUtils.replaceFirst("abc", "b", "d")); // adc
        System.out.println(RegExUtils.replaceFirst("abc", "^a", "d")); // dbc
        System.out.println(RegExUtils.replaceFirst("abc", "^a|c", "d")); // dbc
    }
}
