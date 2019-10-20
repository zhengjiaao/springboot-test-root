package com.dist.rest.annotation;

import java.lang.annotation.*;

/**
 * 仿照SpringMVC Rest风格的@Controller编写
 * @author dingchw
 * @date 2019/4/1.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistRest {
    String name() default "";
    String url();
}
