package com.zja.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 18:23
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：测试级联校验
 */
@Getter
@Setter
@ToString
public class Child {
    @NotNull
    private String name;
}
