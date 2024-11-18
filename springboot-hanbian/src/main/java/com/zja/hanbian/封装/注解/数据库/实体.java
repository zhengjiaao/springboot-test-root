package com.zja.hanbian.封装.注解.数据库;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-11-11 10:21
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface 实体 {
    String 名称() default "";
}
