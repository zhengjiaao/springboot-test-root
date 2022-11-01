package com.zja.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-23 11:26
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：用户展示类
 */
@Data
@ApiModel("用户展示类")
public class UserVO implements Serializable {

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户登录名不能为空")
    @Size(min = 2, max = 10, message = "用户名称必须是2-10个字符")
    private String name;

    @ApiModelProperty("用户密码")
    @NotNull  //password不赋值时,显示不能为null, @NotNull对所有基本类型使用
    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$", message = "密码长度必须为6-16,仅支持英文和数字")
    private String password;

    @ApiModelProperty("用户年龄")
    @NotNull
    @Max(value = 25)
    @Min(value = 16)
    private Integer age;

    public UserVO(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public UserVO(String name, String password, Integer age) {
        this.name = name;
        this.password = password;
        this.age = age;
    }
}
