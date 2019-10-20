package com.dist.service.Impl;


import com.dist.dao.UserDao;
import com.dist.entity.UserEntity;
import com.dist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:18
 * @Author: Mr.Zheng
 * @Description:
 */
@Service
@Slf4j
// 声明式事务：解决close session 关闭问题
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserEntity addUserEntity(UserEntity userEntity) {
        return userDao.save(userEntity);
    }

    @Override
    public List<UserEntity> getUserList() {
        return this.userDao.findAll();
    }


    @Override
    public Optional<UserEntity> getUserById(Long Id) {
        return this.userDao.findById(Id);
    }

    @Override
    public UserEntity updateUserEntityById(UserEntity userEntity) {
        return userDao.save(userEntity);
    }

    @Override
    public void daleteUserEntityById(Long Id) {
        Optional<UserEntity> userById = getUserById(Id);
        if (userById != null){
            this.userDao.deleteById(Id);
        }
    }

    @Override
    public List<UserEntity> getCurrentUserList() {
        Sort sort =new Sort(Sort.Direction.DESC,"createTime");
        return userDao.findAll(sort);
    }

    /**
     * 分页查询
     * @return
     */
    @Override
    public Page<UserEntity> getPageUserList() {
        /*Sort sort =new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable =new PageRequest(0,5,sort);
        return userDao.findAll(pageable);*/
        return null;
    }
}
