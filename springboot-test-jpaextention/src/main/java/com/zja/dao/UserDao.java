package com.zja.dao;

import com.zja.entity.UserEntity;
import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:03
 * @Author: Mr.Zheng
 * @Description:
 */
public interface UserDao extends GenericJpaRepository<UserEntity,Long> {

    @TemplateQuery
    List<UserEntity> getAlls();

    @Override
    List<UserEntity> findAll();

    @Query(nativeQuery = true, value = "select name from t_user t")
    List<String> getNames();
}
