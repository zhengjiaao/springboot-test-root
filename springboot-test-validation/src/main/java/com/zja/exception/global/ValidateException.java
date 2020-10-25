package com.zja.exception.global;

import org.springframework.http.HttpStatus;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-23 14:50
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class ValidateException extends RuntimeException {
    HttpStatus status ;

    public ValidateException(String message){
        super(message);
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ValidateException(String message, HttpStatus status){
        this(message);
        this.status = status;
    }
}
