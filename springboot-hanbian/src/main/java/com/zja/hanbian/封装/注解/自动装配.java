package com.zja.hanbian.封装.注解;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-19 17:32
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Autowired
public @interface 自动装配 {
}
