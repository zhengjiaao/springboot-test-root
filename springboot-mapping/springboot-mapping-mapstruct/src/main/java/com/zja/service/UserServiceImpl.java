/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 15:30
 * @Since:
 */
package com.zja.service;

import com.zja.mapper.UserMapper;
import com.zja.model.User;
import com.zja.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDTO getUserDTO() {
        User user = new User();
        user.setUsername("李四");
        user.setPassword("123456");
        user.setSex("男");

//        return UserMapper.INSTANCE.toDto(user);

        return userMapper.toDto(user);
    }

}
