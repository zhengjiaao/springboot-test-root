package com.zja.dto;

import com.zja.config.validator.annotation.DateValidator;
import com.zja.config.validator.groups.UserLoginGroup;
import com.zja.config.validator.groups.UserRegistGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 14:23
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：用户实体类
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {

    @NotNull(message = "用户id不能为空")
    private Long id;

    @NotBlank(message = "用户登录名不能为空", groups = {UserLoginGroup.class, UserRegistGroup.class})
    @Size(min = 3, max = 11, message = "账号长度必须是3-11个字符")
    private String loginName;

    //@NotBlank  //password赋值为""时,显示不能为空, @NotBlank仅对字符串使用
    @NotNull  //password不赋值时,显示不能为null, @NotNull对所有基本类型使用
    @Size(min = 6, max = 16, message = "密码长度必须是6-16个字符", groups = UserLoginGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$")
    private String password;

    @NotBlank(message = "用户邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    //不为空则检验日期格式
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;

    @Max(value = 10, message = "值是否小于等于给定的值10")
    @Min(value = 1, message = "值是否大于等于给定的值1")
    private Integer valueMax;

    //自定义注解格式
    @DateValidator(dateFormat = "yyyy-MM-dd", message = "自定义日期格式")
    private String birthday;

    @AssertTrue(message = "必须为true")
    private boolean isRegistered;

    // 级联校验
    @Valid
    @NotNull
    private Child child;

}
