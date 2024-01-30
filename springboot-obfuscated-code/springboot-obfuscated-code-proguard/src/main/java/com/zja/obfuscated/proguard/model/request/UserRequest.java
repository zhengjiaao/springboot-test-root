package com.zja.obfuscated.proguard.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:31
 */
@Data
@ApiModel("UserRequest")
public class UserRequest implements Serializable {
    @ApiModelProperty("")
    private String name;

}