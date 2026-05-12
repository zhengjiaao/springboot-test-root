package com.zja.jwt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 认证响应体
 *
 * @Author: zhengja
 * @Date: 2025-07-11 16:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("认证响应体")
public class AuthResponse implements Serializable {

    @ApiModelProperty("访问令牌")
    private String accessToken;

    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("角色列表")
    private List<String> roles;
}
