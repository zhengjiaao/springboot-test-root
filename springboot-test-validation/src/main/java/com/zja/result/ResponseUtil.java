package com.zja.result;

import com.alibaba.fastjson.JSONObject;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 15:56
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public abstract class ResponseUtil {

    // 业务状态：成功
    public static final String RESULT_STATUS_SUCCESS = "success";

    // 业务状态：错误 因为一些不可预计的、系统级的错误导致的操作失败，如数据库断电，服务器内存溢出等。
    public static final String RESULT_STATUS_ERROR = "error";

    // 业务状态：失败 因为业务逻辑错误导致操作失败，如邮箱已存在，年龄不满足条件等。
    public static final String RESULT_STATUS_FAIL = "fail";


    //返回数据
    public static ResponseVO instance(Object data, String status) {
        return new ResponseVO<>(data, status);
    }

    /**
     * @param responseCode 自定义状态码和提示消息
     * @param data       返回数据
     */
    public static ResponseVO instance(ResponseCode responseCode, Object data, String status) {
        return new ResponseVO<>(responseCode, data, status);
    }

    /**
     * @param code   自定义状态码
     * @param msg    自定义提示信息
     * @param data   返回数据
     * @param status 自定义业务状态
     */
    public static ResponseVO instance(int code, String msg, Object data, String status) {
        return new ResponseVO<>(code, msg, data, status);
    }

    /**
     * 请求成功
     *
     * @return 返回空json数据
     */
    public static ResponseVO success() {
        return instance(new JSONObject(), RESULT_STATUS_SUCCESS);
    }

    /**
     * 请求成功
     *
     * @param o 返回数据
     * @return
     */
    public static ResponseVO success(Object o) {
        return instance(o, RESULT_STATUS_SUCCESS);
    }

    /**
     * 请求成功
     *
     * @param msg 提示信息
     */
    public static ResponseVO success(String msg) {
        return instance(ResponseCode.SUCCESS.getCode(), msg, new JSONObject(), RESULT_STATUS_SUCCESS);
    }


    /**
     * 请求成功
     *
     * @param msg 提示信息
     * @param o   返回数据
     */
    public static ResponseVO success(String msg, Object o) {
        return instance(ResponseCode.SUCCESS.getCode(), msg, o, RESULT_STATUS_SUCCESS);
    }

    /**
     * 系统错误，请求失败
     */
    public static ResponseVO error() {
        return instance(ResponseCode.ERROR, new JSONObject(), RESULT_STATUS_ERROR);
    }

    /**
     * 系统错误，请求失败
     *
     * @param msg 提示信息
     */
    public static ResponseVO error(String msg) {
        return instance(ResponseCode.ERROR.getCode(), msg, new JSONObject(), RESULT_STATUS_ERROR);
    }

    /**
     * 系统错误，请求失败
     *
     * @param code 错误码
     * @param msg  提示信息
     */
    public static ResponseVO error(Integer code, String msg) {
        return instance(code, msg, new JSONObject(), RESULT_STATUS_ERROR);
    }

    /**
     * 系统错误，请求失败
     *
     * @param msg 提示信息
     * @param o   返回数据
     */
    public static ResponseVO error(String msg, Object o) {
        return instance(ResponseCode.ERROR.getCode(), msg, o, RESULT_STATUS_ERROR);
    }

    /**
     * 业务逻辑错误，请求失败
     */
    public static ResponseVO fail() {
        return instance(ResponseCode.FAILED, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 业务逻辑错误，请求失败
     *
     * @param msg 提示信息
     */
    public static ResponseVO fail(String msg) {
        return instance(ResponseCode.FAILED.getCode(), msg, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 业务逻辑错误，请求失败
     *
     * @param code 错误码
     * @param msg  提示信息
     */
    public static ResponseVO fail(Integer code, String msg) {
        return instance(code, msg, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 业务逻辑错误，请求失败
     *
     * @param msg 提示信息
     * @param o   返回数据
     */
    public static ResponseVO fail(String msg, Object o) {
        return instance(ResponseCode.FAILED.getCode(), msg, o, RESULT_STATUS_FAIL);
    }

    /**
     * 参数错误
     */
    public static ResponseVO paramError() {
        return instance(ResponseCode.VALIDATE_FAILED, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 参数错误
     *
     * @param data
     */
    public static ResponseVO paramError(Object data) {
        return instance(ResponseCode.VALIDATE_FAILED, data, RESULT_STATUS_FAIL);
    }

    /**
     * 参数错误
     *
     * @param msg
     * @param o
     */
    public static ResponseVO paramError(String msg, Object o) {
        return instance(ResponseCode.VALIDATE_FAILED.getCode(), msg, o, RESULT_STATUS_FAIL);
    }

}
