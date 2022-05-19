/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:35
 * @Since:
 */
package com.zja.tangram.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回数据格式 结果
 */
@Data
@ApiModel("统一返回数据格式")
public class ResponseResult<T> implements Serializable {

    public static final String RESULT_MSG_SUCCESS = "请求成功";
    public static final String RESULT_MSG_FAIL = "业务逻辑错误,请求失败";
    public static final String RESULT_MSG_ERROR = "系统内部错误,请求失败";
    public static final int RESULT_CODE_SUCCESS = 1000;
    public static final int RESULT_CODE_FAIL = 2000;
    public static final int RESULT_CODE_ERROR = 3000;

    // 业务状态：成功
    public static final String RESULT_STATUS_SUCCESS = "success";

    // 业务状态：失败 因为业务逻辑错误导致操作失败，如邮箱已存在，年龄不满足条件等。
    public static final String RESULT_STATUS_FAIL = "fail";

    // 业务状态：错误 因为一些不可预计的、系统级的错误导致的操作失败，如数据库断电，服务器内存溢出等。
    public static final String RESULT_STATUS_ERROR = "error";


    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private Integer code;
    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private T data;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String status;
    /**
     * 消息
     */
    @ApiModelProperty("消息")
    private String message;

    public ResponseResult() {

    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this(code, data);
        this.message = msg;
    }

    public ResponseResult(Integer code) {
        this.code = code;
    }
}
