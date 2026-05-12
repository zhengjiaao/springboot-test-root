package com.zja.jwt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求体 请求参数
 *
 * @author: zhengja
 * @since: 2025/07/11 16:45
 */
@Data
@ApiModel("登录请求体")
public class LoginRequest implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

}