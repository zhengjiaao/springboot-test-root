package com.dist.util.exception;

/**
 * 业务异常,如计算失败
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public class BusinessException extends Exception{

    private static String BUSINESS_EXCEPTION = "业务异常，请稍后再试";

    public BusinessException() {
        super(BUSINESS_EXCEPTION);
    }

    public BusinessException(String msg) {
        super(msg);
    }
}
