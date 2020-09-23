package com.zja.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 14:16
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义统一响应体
 * 显示层对象，通常是Web向模板渲染引擎层传输的对象
 */
@Getter
@Setter
@ApiModel(value = "自定义统一响应体")
public class ResponseVO<T> implements Serializable {

    /**
     * 状态码，比如1000代表响应成功
     */
    @ApiModelProperty(value = "状态码")
    private int code;
    /**
     * 响应信息，用来说明响应情况
     */
    @ApiModelProperty(value = "响应信息")
    private String msg;
    /**
     * 响应的具体数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;
    /**
     * 业务状态：成功、失败、错误
     */
    @ApiModelProperty(value = "响应状态")
    private String status;

    public ResponseVO(T data, String status) {
        this(ResponseCode.SUCCESS, data, status);
    }

    public ResponseVO(ResponseCode responseCode, T data, String status) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = data;
        this.status = status;
    }

    public ResponseVO(int code, String msg, T data, String status) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.status = status;
    }
}
