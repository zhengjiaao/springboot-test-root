package com.zja.result;

import lombok.Getter;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 14:20
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Getter
public enum ResponseCode {

    //1000系列通用成功
    SUCCESS(1000, "请求成功"),

    //2000系列通用失败,因为业务逻辑错误导致操作失败，如邮箱已存在，年龄不满足条件等
    FAILED(2000, "业务逻辑错误,请求失败"),
    VALIDATE_FAILED(2001, "参数校验失败"),
    USER_NOT_EXIST(2002, "用户不存在"),
    USER_LOGIN_FAIL(2003, "用户名或密码错误"),
    USER_NOT_LOGIN(2004, "用户还未登录,请先登录"),
    NO_PERMISSION(2005, "权限不足,请联系管理员"),

    //3000系列通用错误 错误 因为一些不可预计的、系统级的错误导致的操作失败，如数据库断电，服务器内存溢出等
    ERROR(3000, "系统内部错误,请求失败"),
    SYS_RUN_ERROR(3001, "系统运行错误,请求失败");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
