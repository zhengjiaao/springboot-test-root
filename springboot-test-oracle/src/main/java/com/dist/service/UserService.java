package com.dist.service;

import com.dist.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @program: springbootdemo
 * @Date: 2018/12/28 10:13
 * @Author: Mr.Zheng
 * @Description:
 */
public interface UserService {

    UserEntity addUserEntity(UserEntity userEntity);
    List<UserEntity> getUserList();
    Optional<UserEntity> getUserById(Long Id);
    UserEntity updateUserEntityById(UserEntity userEntity);
    List<UserEntity> getCurrentUserList();
    Page<UserEntity> getPageUserList();
    void daleteUserEntityById(Long Id);


}
