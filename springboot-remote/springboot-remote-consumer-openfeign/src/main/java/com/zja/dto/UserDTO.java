package com.zja.dto;

import feign.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "用户信息")
@Data
public class UserDTO implements Serializable{
    @NotNull
    @ApiModelProperty(value = "用户id")
    private String id;
    @NotNull
    @ApiModelProperty(value = "用户名")
    @Param("name")
    private String name;
    @ApiModelProperty(value = "时间")
    private Date date;
}
