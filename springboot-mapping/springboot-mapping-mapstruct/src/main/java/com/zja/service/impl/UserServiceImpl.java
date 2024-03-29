/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 15:30
 * @Since:
 */
package com.zja.service.impl;

import com.zja.entity.User;
import com.zja.mapper.UserMapper;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserRequest;
import com.zja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDTO save(UserRequest request) {
        User user = userMapper.map(request);
        System.out.println(user);
        return userMapper.map(user);
    }

    @Override
    public List<UserDTO> userDTOList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("李四", "111", "男"));
        userList.add(new User("李四2", "222", "男"));
        userList.add(new User("李四3", "333", "男"));
        userList.add(new User("李四4", "444", "男"));

        return userMapper.mapList(userList);
    }

}
