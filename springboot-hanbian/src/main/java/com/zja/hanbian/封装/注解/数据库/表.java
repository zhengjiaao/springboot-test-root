package com.zja.hanbian.封装.注解.数据库;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应：@Table
 *
 * @Author: zhengja
 * @Date: 2024-11-11 10:26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface 表 {
    String 名称() default "";
}
