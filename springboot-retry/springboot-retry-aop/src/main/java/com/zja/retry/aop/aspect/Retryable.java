package com.zja.retry.aop.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: zhengja
 * @Date: 2024-10-29 13:11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Retryable {
    Backoff backoff() default @Backoff();
    int maxAttempts() default 3;
    Class<? extends Throwable>[] include() default {};
    Class<? extends Throwable>[] exclude() default {};
}