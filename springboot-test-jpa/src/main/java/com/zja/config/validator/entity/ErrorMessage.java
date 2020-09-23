package com.zja.config.validator.entity;

import lombok.Data;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-18 13:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：校验参数的错误信息
 */
@Data
public class ErrorMessage {

    //错误属性
    private String property;
    //属性错误信息
    private String message;

    public ErrorMessage(String property, String message) {
        this.property = property;
        this.message = message;
    }
}
