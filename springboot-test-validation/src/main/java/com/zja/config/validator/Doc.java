package com.zja.config.validator;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 16:44
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：此类仅作为注释说明文档
 */
public class Doc {

/*
常用注解说明:
    @AssertFalse
    @AssertTrue 检验boolean类型的值
    @DecimalMax@DecimalMin 限定被标注的属性的值的大小
    @Digits(intege=,fraction=) 限定被标注的属性的整数位数和小数位数
    @Future  检验给定的日期是否比现在晚
    @Past    校验给定的日期是否比现在早
    @Max     检查被标注的属性的值是否小于等于给定的值
    @Min     检查被标注的属性的值是否大于等于给定的值
    @NotNull 检验被标注的值不为空
    @Null    检验被标注的值为空
    @Pattern(regex= ,flag= )  检查该字符串是否能够在match指定的情况下被regex定义的正则表达式匹配
    @Size(min= ,max= )        检查被标注元素的长度
    @Valid 递归的对关联的对象进行校验
*/

/*
@NotNull,@NotEmpty,@NotBlank 三个注解区别:
    @NotEmpty 用在集合类上面
    @NotBlank 用在String上面
    @NotNull   用在基本类型上
当@NotEmpty用在Integer类型上时将会出现上面的错误，换成@NotNull问题解决
*/

/*
分组校验:
    默认组: Default.class
    作用: 不同模块可能对于同一字段合法性校验也会不同
    注意: 组必须是接口,只校验组所标记的属性字段
    例: UserGroup, 用户(User)在登录时只对登录名和密码关心,而用户在注册时对所有信息都关心
*/

/*
自定义注解校验:
    例: DateValidator
*/

}
