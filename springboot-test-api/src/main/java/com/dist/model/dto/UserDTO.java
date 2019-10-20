package com.dist.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date: 2019/1/4 17:31
 * @Author: Mr.Zheng
 * @Description:
 */
@ApiModel(value = "用户登陆验证(带设备号or无设备号)")
@Data
public class UserDTO implements Serializable {

    @ApiModelProperty(value = "用户登code名")
    private String userCode;
    @ApiModelProperty(value = "用户登录名")
    private String loginName;
    @ApiModelProperty(value = "密码")
    private String loginPassword;
    @ApiModelProperty(value = "设备号")
    private String deviceCode;
    @ApiModelProperty(value = "系统code")
    private String systemCode;


}
