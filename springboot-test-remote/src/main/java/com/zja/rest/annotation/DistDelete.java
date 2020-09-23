package com.zja.rest.annotation;

import java.lang.annotation.*;

/**
 * @author dingchw
 * @date 2019/4/1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistDelete{
    String name() default "";
    String value();
    Class resultClass() default Void.class;
}
