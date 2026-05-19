package com.zja.security.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息 DTO
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户信息")
public class UserInfo implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("角色列表")
    private List<String> roles;

    @ApiModelProperty("是否启用")
    private boolean enabled;
}
