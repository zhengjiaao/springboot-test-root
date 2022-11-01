package com.zja.response;

import java.io.Serializable;

/**
 * response响应结果
 */
public class Result<T> implements Serializable{

    public static final String RESULT_MSG_SUCCESS = "请求成功";
    public static final String RESULT_MSG_FAIL = "业务逻辑错误,请求失败";
    public static final String RESULT_MSG_ERROR = "系统内部错误,请求失败";
    public static final Integer RESULT_CODE_SUCCESS = 1000;
    public static final Integer RESULT_CODE_FAIL = 2000;
    public static final Integer RESULT_CODE_ERROR = 3000;

    // 业务状态：成功
    public static final String RESULT_STATUS_SUCCESS = "success";

   // 业务状态：失败 因为业务逻辑错误导致操作失败，如邮箱已存在，年龄不满足条件等。
    public static final String RESULT_STATUS_FAIL = "fail";

    // 业务状态：错误 因为一些不可预计的、系统级的错误导致的操作失败，如数据库断电，服务器内存溢出等。
    public static final String RESULT_STATUS_ERROR = "error";



    /**
     * 业务状态，若业务状态是成功/失败/错误，则采用ReturnValue.SUCCESS、FAILED、ERROR。若非这3种状态，请自行定义。
     */
    private String status;

    /**
     * 返回编码
     */
    private Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 提示信息
     */
    private String message;

    public Result() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
