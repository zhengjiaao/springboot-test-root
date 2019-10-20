package com.dist.util.exception;

/**
 * 参数非法，如密码错误/密码复杂度过低
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public class IllegalParameterException extends Exception {

    private static final String ILLEGAL_PARAMETER_EXCEPTION = "参数非法";

    public IllegalParameterException() {
        super(ILLEGAL_PARAMETER_EXCEPTION);
    }

    public IllegalParameterException(String s) {
        super(s);
    }
}
