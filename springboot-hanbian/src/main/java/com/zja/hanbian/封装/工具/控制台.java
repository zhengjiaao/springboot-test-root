package com.zja.hanbian.封装.工具;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 9:03
 */
public class 控制台 {

    private 控制台() {

    }

    public static void 输出(Object object) {
        System.out.println(object);
    }

    public static void 错误输出(Object object) {
        System.err.println(object);
    }
}
