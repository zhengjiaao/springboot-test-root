package com.zja.mvc.filter.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:21
 */
@Data
@ApiModel("UserUpdateRequest")
public class UserUpdateRequest implements Serializable {
    @ApiModelProperty("唯一标识ID")
    private String id;

    @ApiModelProperty("名称")
    private String name;
}