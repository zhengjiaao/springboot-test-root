package com.zja.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zhengja
 * @Date: 2024-07-01 14:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户扩展信息")
public class UserExtensionDTO extends UserDTO {

    @Schema(description = "用户地址")
    private String address;
}
