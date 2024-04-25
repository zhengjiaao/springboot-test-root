package com.zja.mvc.filter.auth;

import java.lang.annotation.*;

/**
 * 取消认证(无需登录)
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnAuth {

}
