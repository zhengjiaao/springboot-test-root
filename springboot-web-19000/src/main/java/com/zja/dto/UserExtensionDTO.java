package com.zja.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zhengja
 * @Date: 2024-07-01 14:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "用户扩展信息")
public class UserExtensionDTO extends UserDTO {

    @ApiModelProperty(value = "用户地址")
    private String address;
}
