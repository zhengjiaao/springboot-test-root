package com.zja.hanbian.封装.注解.属性;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Author: zhengja
 * @Date: 2024-11-11 11:33
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface 属性值获取 {
}
