package com.zja.mvc.filter.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("UserDTO")
public class UserDTO implements Serializable {
    @ApiModelProperty("唯一标识ID")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;
}