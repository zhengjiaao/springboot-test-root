package com.zja.webexception.exception;

import com.zja.webexception.model.response.ResponseCode;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * 业务处理异常
 *
 * @author: zhengja
 * @since: 2024/02/01 9:55
 */
@Getter
@ToString
public class BusinessException extends RuntimeException {
    // 异常代码
    private final int code;
    // 异常提示信息
    private final String msg;

    // 默认异常抛出提示信息
    public BusinessException() {
        this(ResponseCode.FAILED);
    }

    // 自定义业务异常抛出提示信息
    public BusinessException(ResponseCode failed) {
        this.code = failed.getCode();
        this.msg = failed.getMsg();
    }

}
