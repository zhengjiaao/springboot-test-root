package com.zja.obfuscated.allatori.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:31
 */
@Data
@ApiModel("UserUpdateRequest")
public class UserUpdateRequest implements Serializable {
    @ApiModelProperty("")
    private String name;

}