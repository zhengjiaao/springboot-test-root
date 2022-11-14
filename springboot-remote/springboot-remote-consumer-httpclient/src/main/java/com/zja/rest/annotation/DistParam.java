package com.zja.rest.annotation;

/**
 * @author dingchw
 * @date 2019/4/1.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistParam {
    String value();
}
