package com.zja.util.exception;

/**
 * 参数不合法
 */
public class IllegalParameterException extends RuntimeException{

    private static final String ILLEGAL_PARAMETER = "参数非法";

    public IllegalParameterException() {
        super(ILLEGAL_PARAMETER);
    }

    public IllegalParameterException(String s) {
        super(s);
    }
}
