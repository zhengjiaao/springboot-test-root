package com.dist.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/12 9:37
 */
@Data
@NoArgsConstructor  //自动生成无参数构造函数
//@AllArgsConstructor //自动生成全参数构造函数
@ApiModel(value = "校验dto")
public class ValidatorDto {

    @ApiModelProperty(value = "用户名不能为空")
    @NotBlank(message="用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "年龄不能为空")
    @NotBlank(message="年龄不能为空")
    @Pattern(regexp="^[0-9]{1,2}$",message="年龄不正确")
    private String age;

    @ApiModelProperty(value = "必须为false")
    @AssertFalse(message = "必须为false")
    private Boolean isFalse;
    /**
     * 如果是空，则不校验，如果不为空，则校验
     */
    @ApiModelProperty(value = "出生日期")
    @Pattern(regexp="^[0-9]{4}-[0-9]{2}-[0-9]{2}$",message="出生日期格式不正确")
    private String birthday;
}
