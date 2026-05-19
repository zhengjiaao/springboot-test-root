package com.zja.security.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求体
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Data
@ApiModel("登录请求体")
public class LoginRequest implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", example = "admin", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", example = "admin123", required = true)
    private String password;

    @ApiModelProperty(value = "记住我", example = "false")
    private Boolean rememberMe = false;
}
