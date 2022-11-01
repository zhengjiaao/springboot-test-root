package com.zja.annotation;

import java.lang.annotation.*;

/**
 * 通过这个注解可以对返回的数据中的指定的字段数据进行rsa公钥加密
 * @author xupp
 * @date 2018/12/26
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target({ElementType.METHOD})
public @interface DREncrypt {

}
