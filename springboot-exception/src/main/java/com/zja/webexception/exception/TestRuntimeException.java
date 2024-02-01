package com.zja.webexception.exception;

/**
 * 测试：继承 RuntimeException 抛出异常，不需要手动抛出异常
 *
 * @author: zhengja
 * @since: 2024/02/01 9:57
 */
public class TestRuntimeException extends RuntimeException {

    private static final String EXCEPTION_MSG = "测试：继承 RuntimeException 抛出异常";

    public TestRuntimeException() {
        super(EXCEPTION_MSG);
    }

    public TestRuntimeException(String msg) {
        super(msg);
    }
}
