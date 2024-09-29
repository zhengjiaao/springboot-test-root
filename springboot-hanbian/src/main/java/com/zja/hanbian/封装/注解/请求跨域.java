package com.zja.hanbian.封装.注解;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 14:02
 */
@CrossOrigin
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface 请求跨域 {

}
