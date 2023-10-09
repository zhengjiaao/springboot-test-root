/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:23
 * @Since:
 */
package com.zja.flyweight.login;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2023/10/09 10:23
 */
// 享元工厂类（Flyweight Factory）
class UserFactory {
    private static final Map<String, User> userCache = new HashMap<>();

    public static User getUser(String username) {
        User user = userCache.get(username);

        if (user == null) {
            user = new UserImpl(username);
            userCache.put(username, user);
            System.out.println("Creating a new user: " + username);
        }

        return user;
    }
}