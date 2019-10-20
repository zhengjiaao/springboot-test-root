package com.dist.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记某个对象属性转换到目标对象属性的时候，需要将该对象的字符串转换成日期再复制与目标对象 默认为yyyy-MM-dd
 * 
 * @author ShenYuTing
 * @version V1.0, 2013-8-12
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert2Date {
    String format() default "yyyy-MM-dd";
}
