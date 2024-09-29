package com.zja.hanbian.封装.异常;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 15:42
 */
public class 运行时异常 extends RuntimeException {

    public 运行时异常() {
        super();
    }

    public 运行时异常(String message) {
        super(message);
    }

    public 运行时异常(String message, Throwable cause) {
        super(message, cause);
    }
}
