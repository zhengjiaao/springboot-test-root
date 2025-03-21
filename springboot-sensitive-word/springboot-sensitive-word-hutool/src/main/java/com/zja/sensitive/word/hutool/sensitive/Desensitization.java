package com.zja.sensitive.word.hutool.sensitive;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-10 11:30
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitization {
    boolean enable() default true; // true默认启用脱敏，false禁用则不脱敏
}
