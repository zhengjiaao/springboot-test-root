package com.dist.util.exception;

/**
 * 记录已存在
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public class ExistedRecordedException extends Exception {

    private static String EXISTED_RECORDED_EXCEPTION = "当前记录已存在";

    public ExistedRecordedException() {
        super(EXISTED_RECORDED_EXCEPTION);
    }

    public ExistedRecordedException(String msg) {
        super(msg);
    }

}
