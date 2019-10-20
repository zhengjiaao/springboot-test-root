package com.dist.util.exception;

/**
 * 逻辑异常，如一个用户只能是一个域的管理员
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public class LogicException extends Exception{

    private static String LOGIC_EXCEPTION = "业务逻辑异常";

    public LogicException() {
        super(LOGIC_EXCEPTION);
    }

    public LogicException(String msg) {
        super(msg);
    }
}
