package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户 请求参数
 *
 * @author: zhengja
 * @since: 2025/03/05 14:01
 */
@Data
@ApiModel("UserRequest 新增 或 更新 用户信息")
public class UserRequest implements Serializable {

    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户地址")
    private String address;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户年龄")
    private Integer age;
    @ApiModelProperty("创建时间")
    private Date createTime;

}