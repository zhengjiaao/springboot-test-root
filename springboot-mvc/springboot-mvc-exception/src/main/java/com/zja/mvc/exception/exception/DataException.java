package com.zja.mvc.exception.exception;

/**
 * 数据异常
 *
 * @author: zhengja
 * @since: 2024/02/01 9:56
 */
public class DataException extends RuntimeException {

    private static final String UN_RECORDED_EXCEPTION = "当前记录不存在.";

    public DataException() {
        super(UN_RECORDED_EXCEPTION);
    }

    public DataException(String msg) {
        super(msg);
    }
}
