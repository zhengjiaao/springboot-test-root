package com.zja.hanbian.封装.工具;

import com.zja.hanbian.封装.异常.运行时异常;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 16:15
 */
public class 异常 {

    private 异常() {

    }

    public static void 抛出运行时异常(String message) {
        throw new 运行时异常(message);
    }

    public static void 抛出运行时异常(String message, Throwable cause) {
        throw new 运行时异常(message, cause);
    }

}
