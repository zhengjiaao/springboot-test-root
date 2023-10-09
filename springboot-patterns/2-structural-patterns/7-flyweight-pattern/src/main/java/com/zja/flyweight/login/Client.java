/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:23
 * @Since:
 */
package com.zja.flyweight.login;

/**
 * @author: zhengja
 * @since: 2023/10/09 10:23
 */
// 客户端（Client）
public class Client {
    public static void main(String[] args) {
        User user1 = UserFactory.getUser("John");
        user1.login();

        User user2 = UserFactory.getUser("Jane");
        user2.login();

        User user3 = UserFactory.getUser("John");
        user3.login();

        user1.logout();
        user2.logout();
        user3.logout();
    }
}