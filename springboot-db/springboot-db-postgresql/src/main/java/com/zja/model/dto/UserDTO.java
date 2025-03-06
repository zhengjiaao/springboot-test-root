package com.zja.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User 数据传输
 *
 * @author: zhengja
 * @since: 2025/03/05 14:01
 */
@Data
@ApiModel("UserDTO")
public class UserDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户地址")
    private String address;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户年龄")
    private Integer age;
    @ApiModelProperty("创建时间")
    private Date createTime;
}