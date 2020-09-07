package com.dist.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记转换成entity对象的注解<br />
 * 转换成的entity对象必须有id.<br />
 * 被注解的属性必须为String
 * 
 * @author shenyuting
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert2Entity {
    Class<?> entityClass();
}
