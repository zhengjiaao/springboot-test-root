package com.zja.mvc.exception.exception;

/**
 * 测试：继承 Exception 抛出异常，需要手动抛出异常
 *
 * @author: zhengja
 * @since: 2024/02/01 9:57
 */
public class TestException extends Exception {
    private static final String EXCEPTION_MSG = "测试：继承 Exception 抛出异常";

    public TestException() {
        super(EXCEPTION_MSG);
    }

    public TestException(String msg) {
        super(msg);
    }
}
