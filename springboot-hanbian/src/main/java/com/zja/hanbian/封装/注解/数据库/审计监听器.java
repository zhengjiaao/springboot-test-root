package com.zja.hanbian.封装.注解.数据库;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: zhengja
 * @Date: 2024-11-11 13:41
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface 审计监听器 {
}
