/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-13 14:51
 * @Since:
 */
package com.zja.service;

import com.zja.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/13 14:51
 */
@Service
public class UserService {
    public User getUserById(Long id) {
        // 根据id从数据库或其他数据源获取用户信息
        // 省略具体实现
        return new User(id, "John Doe");
    }

    public User createUser(User user) {
        // 创建用户并返回
        // 省略具体实现
        return user;
    }

    public List<User> searchUsers(String query){
        //搜索用户
        List<User> userList = new ArrayList<>();
        userList.add(new User(001L, "李四"));

        return userList;
    }
}