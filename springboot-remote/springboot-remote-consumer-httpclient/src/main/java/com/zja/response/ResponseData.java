package com.zja.response;

import java.io.Serializable;

/**
 * 统一的response响应结果
 */
public class ResponseData implements Serializable{

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


    private Integer code;
    private Object data;
    private String status;
    private String message;

    public ResponseData(){

    }
    public ResponseData(Integer code, Object data){
        this.code = code;
        this.data = data;
    }
    public ResponseData(Integer code, String msg, Object data){
        this(code,msg);
        this.message = msg;
    }
    public ResponseData(Integer code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public  String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
