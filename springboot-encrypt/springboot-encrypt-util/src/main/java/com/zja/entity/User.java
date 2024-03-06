package com.zja.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2019/6/17 16:49
 */
@ApiModel(value = "修改用户信息")
@Data
public class User implements Serializable{
    @ApiModelProperty(value = "用户id", required = true)
    private Integer id;
    @ApiModelProperty(value = "用户名", required = true)
    private String loginname;
    @ApiModelProperty(value = "用户密码", required = true)
    private String loginpwd;
}
