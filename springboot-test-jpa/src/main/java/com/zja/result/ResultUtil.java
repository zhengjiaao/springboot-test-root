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
public abstract class ResultUtil {

    // 业务状态：成功
    public static final String RESULT_STATUS_SUCCESS = "success";

    // 业务状态：错误 因为一些不可预计的、系统级的错误导致的操作失败，如数据库断电，服务器内存溢出等。
    public static final String RESULT_STATUS_ERROR = "error";

    // 业务状态：失败 因为业务逻辑错误导致操作失败，如邮箱已存在，年龄不满足条件等。
    public static final String RESULT_STATUS_FAIL = "fail";


    //返回数据
    public static ResultVO instance(Object data, String status) {
        return new ResultVO<>(data, status);
    }

    /**
     * @param resultCode 自定义状态码和提示消息
     * @param data       返回数据
     */
    public static ResultVO instance(ResultCode resultCode, Object data, String status) {
        return new ResultVO<>(resultCode, data, status);
    }

    /**
     * @param code   自定义状态码
     * @param msg    自定义提示信息
     * @param data   返回数据
     * @param status 自定义业务状态
     */
    public static ResultVO instance(int code, String msg, Object data, String status) {
        return new ResultVO<>(code, msg, data, status);
    }

    /**
     * 请求成功
     *
     * @return 返回空json数据
     */
    public static ResultVO success() {
        return instance(new JSONObject(), RESULT_STATUS_SUCCESS);
    }

    /**
     * 请求成功
     *
     * @param o 返回数据
     * @return
     */
    public static ResultVO success(Object o) {
        return instance(o, RESULT_STATUS_SUCCESS);
    }

    /**
     * 请求成功
     *
     * @param msg 提示信息
     */
    public static ResultVO success(String msg) {
        return instance(ResultCode.SUCCESS.getCode(), msg, new JSONObject(), RESULT_STATUS_SUCCESS);
    }


    /**
     * 请求成功
     *
     * @param msg 提示信息
     * @param o   返回数据
     */
    public static ResultVO success(String msg, Object o) {
        return instance(ResultCode.SUCCESS.getCode(), msg, o, RESULT_STATUS_SUCCESS);
    }

    /**
     * 系统错误，请求失败
     */
    public static ResultVO error() {
        return instance(ResultCode.ERROR, new JSONObject(), RESULT_STATUS_ERROR);
    }

    /**
     * 系统错误，请求失败
     *
     * @param msg 提示信息
     */
    public static ResultVO error(String msg) {
        return instance(ResultCode.ERROR.getCode(), msg, new JSONObject(), RESULT_STATUS_ERROR);
    }

    /**
     * 系统错误，请求失败
     *
     * @param code 错误码
     * @param msg  提示信息
     */
    public static ResultVO error(Integer code, String msg) {
        return instance(code, msg, new JSONObject(), RESULT_STATUS_ERROR);
    }

    /**
     * 系统错误，请求失败
     *
     * @param msg 提示信息
     * @param o   返回数据
     */
    public static ResultVO error(String msg, Object o) {
        return instance(ResultCode.ERROR.getCode(), msg, o, RESULT_STATUS_ERROR);
    }

    /**
     * 业务逻辑错误，请求失败
     */
    public static ResultVO fail() {
        return instance(ResultCode.FAILED, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 业务逻辑错误，请求失败
     *
     * @param msg 提示信息
     */
    public static ResultVO fail(String msg) {
        return instance(ResultCode.FAILED.getCode(), msg, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 业务逻辑错误，请求失败
     *
     * @param code 错误码
     * @param msg  提示信息
     */
    public static ResultVO fail(Integer code, String msg) {
        return instance(code, msg, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 业务逻辑错误，请求失败
     *
     * @param msg 提示信息
     * @param o   返回数据
     */
    public static ResultVO fail(String msg, Object o) {
        return instance(ResultCode.FAILED.getCode(), msg, o, RESULT_STATUS_FAIL);
    }

    /**
     * 参数错误
     */
    public static ResultVO paramError() {
        return instance(ResultCode.VALIDATE_FAILED, new JSONObject(), RESULT_STATUS_FAIL);
    }

    /**
     * 参数错误
     *
     * @param data
     */
    public static ResultVO paramError(Object data) {
        return instance(ResultCode.VALIDATE_FAILED, data, RESULT_STATUS_FAIL);
    }

    /**
     * 参数错误
     *
     * @param msg
     * @param o
     */
    public static ResultVO paramError(String msg, Object o) {
        return instance(ResultCode.VALIDATE_FAILED.getCode(), msg, o, RESULT_STATUS_FAIL);
    }

}
