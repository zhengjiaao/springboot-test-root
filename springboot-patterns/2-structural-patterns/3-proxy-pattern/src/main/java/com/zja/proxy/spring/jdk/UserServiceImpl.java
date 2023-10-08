/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:14
 * @Since:
 */
package com.zja.proxy.spring.jdk;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:14
 */
// 目标类
class UserServiceImpl implements UserService {
    @Override
    public void addUser(String username) {
        System.out.println("Adding user: " + username);
    }
}