package com.zja.exception;

import com.zja.result.ResponseCode;
import lombok.Getter;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 15:02
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义异常-API异常
 */
@Getter
public class APIException extends RuntimeException {
    private int code;
    private String msg;

    public APIException() {
        this(ResponseCode.FAILED);
    }

    public APIException(ResponseCode failed) {
        this.code = failed.getCode();
        this.msg = failed.getMsg();
    }

}
