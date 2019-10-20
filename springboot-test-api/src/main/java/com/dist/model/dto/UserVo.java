package com.dist.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/3 10:01
 */
@ApiModel(value = "用户VO")
@Data
public class UserVo implements Serializable {
    @ApiModelProperty(value = "用户登code名")
    private String userCode;
    @ApiModelProperty(value = "用户登录名")
    private String loginName;
}
