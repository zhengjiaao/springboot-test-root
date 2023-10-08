/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:14
 * @Since:
 */
package com.zja.proxy.spring.jdk;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:14
 */
public class Client {
    public static void main(String[] args) {
      /*  ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.addUser("John");*/

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.addUser("John");

        //输出结果：
        //Before method execution
        //Adding user: John
        //After method execution
    }
}
