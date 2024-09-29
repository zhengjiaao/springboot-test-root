package com.zja.hanbian.封装.工具;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 10:45
 */
public class 比较 {
    public static boolean 相等(Object args1, Object args2) {
        if (args1 == null) {
            return args2 == null;
        }
        return args1.equals(args2);
    }
}
