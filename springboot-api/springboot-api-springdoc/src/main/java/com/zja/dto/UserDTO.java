package com.zja.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: zhengja
 * @since: 2019/6/27 16:35
 */
// @ApiModel(value = "用户信息")
@Schema(description = "用户信息")
@Data
public class UserDTO implements Serializable {

    // @ApiModelProperty(value = "用户id")
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String id;
    // @ApiModelProperty(value = "用户名")
    @Schema(description = "用户名")
    private String name;
    // @ApiModelProperty(value = "时间")
    @Schema(description = "时间")
    private Date date;
}
