/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:22
 * @Since:
 */
package com.zja.flyweight.login;

/**
 * @author: zhengja
 * @since: 2023/10/09 10:22
 */
// 具体享元类（Concrete Flyweight）
class UserImpl implements User {
    private String username;

    public UserImpl(String username) {
        this.username = username;
    }

    @Override
    public void login() {
        System.out.println(username + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println(username + " logged out.");
    }
}