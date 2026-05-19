package com.zja.security.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改密码请求体
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Data
@ApiModel("修改密码请求体")
public class ChangePasswordRequest implements Serializable {

    @NotBlank(message = "原密码不能为空")
    @ApiModelProperty(value = "原密码", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 30, message = "新密码长度6-30个字符")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
}
