/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-19 13:29
 * @Since:
 */
package com.zja.juc.future.auxiliary;

import com.zja.juc.future.entity.User;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * 用户服务
 *
 * @author: zhengja
 * @since: 2023/07/19 13:29
 */
public class UserService {

    @SneakyThrows
    public User getUser() {
        //睡眠 1s
        Thread.sleep(1000);
        System.out.println("getUser()");
        return new User();
    }

    @SneakyThrows
    public User getUser(String userId) {
        //睡眠 1s
        Thread.sleep(1000);
        System.out.println("getUser(userId)");
        return new User();
    }

    @SneakyThrows
    public List<User> getUserList() {
        //睡眠 2s
        Thread.sleep(2000);
        System.out.println("getUserList()");
        return Arrays.asList(new User(), new User());
    }
}
