package com.dist.dao;

import com.dist.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:03
 * @Author: Mr.Zheng
 * @Description:
 */
public interface UserDao extends JpaRepository<UserEntity,Long>,CrudRepository<UserEntity, Long> {

    UserEntity findByName(String name);
}
