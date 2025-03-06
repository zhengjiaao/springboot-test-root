package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户 更新参数
 *
 * @author: zhengja
 * @since: 2025/03/05 14:01
 */
@Data
@ApiModel("UserUpdateRequest 更新 用户信息")
public class UserUpdateRequest implements Serializable {
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户年龄")
    private Integer age;
}