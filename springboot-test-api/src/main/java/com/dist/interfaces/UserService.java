package com.dist.interfaces;


import com.dist.model.dto.UserVo;
import com.dist.model.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @program: springbootdemo
 * @Date: 2018/12/28 10:13
 * @Author: Mr.Zheng
 * @Description:
 */
public interface UserService {

    UserEntity addUserEntity(UserEntity userEntity);
    UserEntity updateUserEntityById(UserEntity userEntity);
    List<UserEntity> getUserList();
    UserEntity getUserById(Long Id);
    List<UserEntity> getCurrentUserList();
    Page<UserEntity> getPageUserList();
    void daleteUserEntityById(Long Id);
    List<UserVo> findUserVo();
    UserEntity getUserEntityByName(String name);
}
