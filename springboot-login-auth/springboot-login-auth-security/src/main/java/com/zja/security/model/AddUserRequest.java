package com.zja.security.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 新增用户请求体
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Data
@ApiModel("新增用户请求体")
public class AddUserRequest implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度2-20个字符")
    @ApiModelProperty(value = "用户名", example = "zhangsan", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度6-30个字符")
    @ApiModelProperty(value = "密码", example = "123456", required = true)
    private String password;

    @ApiModelProperty(value = "角色列表", example = "[\"USER\"]")
    private List<String> roles;
}
