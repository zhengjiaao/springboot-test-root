/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:50
 * @Since:
 */
package com.zja.builder.user;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:50
 */
public class Client {

    public static void main(String[] args) {
        User user = User.builder().name("李四").password("123").age(18).build();
        System.out.println(user); //User(name=李四, password=123, age=18)
    }
}
