package com.zja.response;


import com.alibaba.fastjson.JSONObject;

/**
 * 统一返回工具类
 */
public abstract class ResultUtil {

    public static Result instance(String status, Integer code, String msg, Object o) {
        Result result = new Result();
        result.setStatus(status);
        result.setCode(code);
        result.setMessage(msg);
        result.setData(o);
        return result;
    }


    /**
     * 请求成功
     * @return
     */
    public static Result success() {
        return instance(Result.RESULT_STATUS_SUCCESS, Result.RESULT_CODE_SUCCESS, Result.RESULT_MSG_SUCCESS,new JSONObject());
    }
    public static Result success(String msg) {
        return instance(Result.RESULT_STATUS_SUCCESS, Result.RESULT_CODE_SUCCESS,msg,new JSONObject());
    }

    public static Result success(String msg, Object o) {
        return instance(Result.RESULT_STATUS_SUCCESS, Result.RESULT_CODE_SUCCESS,msg,o);
    }

    public static Result success(Object o) {
        return instance(Result.RESULT_STATUS_SUCCESS, Result.RESULT_CODE_SUCCESS, Result.RESULT_MSG_SUCCESS,o);
    }

    /**
     * 系统错误请求失败
     * @return
     */
    public static Result error() {
        return instance( Result.RESULT_STATUS_ERROR, Result.RESULT_CODE_ERROR, Result.RESULT_MSG_ERROR,new JSONObject());
    }

    public static Result error(String msg) {
        return instance( Result.RESULT_STATUS_ERROR, Result.RESULT_CODE_ERROR, msg,new JSONObject());
    }

    public static Result error(Integer code, String msg) {
        return instance(Result.RESULT_STATUS_ERROR,code,msg,new JSONObject());
    }

    public static Result error(String msg, Object o) {
        return instance(Result.RESULT_STATUS_ERROR, Result.RESULT_CODE_ERROR,msg,o);
    }

    /**
     * 业务逻辑错误请求失败
     * @return
     */
    public static Result fail() {
        return instance( Result.RESULT_STATUS_FAIL, Result.RESULT_CODE_FAIL, Result.RESULT_MSG_FAIL,new JSONObject());
    }

    public static Result fail(String msg) {
        return instance( Result.RESULT_STATUS_FAIL, Result.RESULT_CODE_FAIL, msg,new JSONObject());
    }

    public static Result fail(String msg, Object o) {
        return instance(Result.RESULT_STATUS_FAIL, Result.RESULT_CODE_FAIL,msg,o);
    }
}
