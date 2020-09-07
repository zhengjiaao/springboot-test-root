package com.dist.util.exception;

/**
 * 记录不存在
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public class UnRecordedException extends Exception {

    private static String UN_RECORDED_EXCEPTION = "当前记录不存在";

    public UnRecordedException() {
        super(UN_RECORDED_EXCEPTION);
    }

    public UnRecordedException(String msg) {
        super(msg);
    }
}
