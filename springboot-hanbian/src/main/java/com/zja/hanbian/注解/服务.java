package com.zja.hanbian.注解;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-19 17:28
 */
// @Service
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface 服务 {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
