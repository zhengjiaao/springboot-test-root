package com.dist.response;

import com.alibaba.fastjson.JSONObject;

/**
 * 统一response响应工具
 * @author yinxp@dist.com.cn
 * @date 2018/12/10
 */
public abstract class ResponseUtil {

    public static ResponseData instance(String status, Integer code, String msg, Object o) {
        ResponseData result = new ResponseData();
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
    public static ResponseData success() {
        return instance(ResponseData.RESULT_STATUS_SUCCESS, ResponseData.RESULT_CODE_SUCCESS, ResponseData.RESULT_MSG_SUCCESS,new JSONObject());
    }

    /**
     * 请求成功
     * @param msg  提示信息
     * @return
     */
    public static ResponseData success(String msg) {
        return instance(ResponseData.RESULT_STATUS_SUCCESS, ResponseData.RESULT_CODE_SUCCESS,msg,new JSONObject());
    }

    /**
     * 请求成功
     * @param o  返回数据
     * @return
     */
    public static ResponseData success(Object o) {
        return instance(ResponseData.RESULT_STATUS_SUCCESS, ResponseData.RESULT_CODE_SUCCESS, ResponseData.RESULT_MSG_SUCCESS,o);
    }

    /**
     * 请求成功
     * @param msg  提示信息
     * @param o     返回数据
     * @return
     */
    public static ResponseData success(String msg, Object o) {
        return instance(ResponseData.RESULT_STATUS_SUCCESS, ResponseData.RESULT_CODE_SUCCESS,msg,o);
    }

    /**
     * 系统错误，请求失败
     * @return
     */
    public static ResponseData error() {
        return instance( ResponseData.RESULT_STATUS_ERROR, ResponseData.RESULT_CODE_ERROR, ResponseData.RESULT_MSG_ERROR,new JSONObject());
    }

    /**
     * 系统错误，请求失败
     * @param msg  提示信息
     * @return
     */
    public static ResponseData error(String msg) {
        return instance( ResponseData.RESULT_STATUS_ERROR, ResponseData.RESULT_CODE_ERROR, msg,new JSONObject());
    }

    /**
     * 系统错误，请求失败
     * @param msg 提示信息
     * @param o  返回数据
     * @return
     */
    public static ResponseData error(String msg, Object o) {
        return instance(ResponseData.RESULT_STATUS_ERROR, ResponseData.RESULT_CODE_ERROR,msg,o);
    }

    /**
     * 业务逻辑错误，请求失败
     * @return
     */
    public static ResponseData fail() {
        return instance( ResponseData.RESULT_STATUS_FAIL, ResponseData.RESULT_CODE_FAIL, ResponseData.RESULT_MSG_FAIL,new JSONObject());
    }

    /**
     * 业务逻辑错误，请求失败
     * @param msg   提示信息
     * @return
     */
    public static ResponseData fail(String msg) {
        return instance( ResponseData.RESULT_STATUS_FAIL, ResponseData.RESULT_CODE_FAIL, msg,new JSONObject());
    }

    /**
     * 业务逻辑错误，请求失败
     * @param msg  提示信息
     * @param o     返回数据
     * @return
     */
    public static ResponseData fail(String msg, Object o) {
        return instance(ResponseData.RESULT_STATUS_FAIL, ResponseData.RESULT_CODE_FAIL,msg,o);
    }


}
