package com.zja.rest.annotation;

import java.lang.annotation.*;

/**
 * @author dingchw
 * @date 2019/4/1.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistRequest {
    String name() default "";
    String value();
}
