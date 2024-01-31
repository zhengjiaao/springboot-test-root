package com.zja.obfuscated.allatori.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:30
 */
@Data
@ApiModel("UserDTO")
public class UserDTO implements Serializable {
    @ApiModelProperty("")
    private String id;

}