/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:20
 * @Since:
 */
package com.zja.proxy.spring.cglib;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:20
 */
public class Client {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.addUser("John");

        //输出结果:
        //Adding user: John
    }
}
