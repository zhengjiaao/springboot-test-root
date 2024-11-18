package com.zja.hanbian.封装.注解.数据库;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-11-11 13:46
 */
@Repository
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface 存储库 {
}
