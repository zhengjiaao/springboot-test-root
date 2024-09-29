package com.zja.hanbian.封装.注解;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-19 17:38
 */
@CrossOrigin
@RestController
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface 请求控制器 {

}
