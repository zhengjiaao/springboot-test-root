/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:19
 * @Since:
 */
package com.zja.proxy.spring.cglib;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:19
 */
// 目标类
class UserService {
    public void addUser(String username) {
        System.out.println("Adding user: " + username);
    }
}
