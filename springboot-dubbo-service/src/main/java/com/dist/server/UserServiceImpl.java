package com.dist.server;

import com.alibaba.dubbo.config.annotation.Service;
import com.dist.dao.UserDao;
import com.dist.interfaces.UserService;
import com.dist.model.dto.UserVo;
import com.dist.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:18
 * @Author: Mr.Zheng
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserEntity addUserEntity(UserEntity userEntity) {
        return userDao.save(userEntity);
    }

    @Override
    public List<UserEntity> getUserList() {
        return userDao.findAll();
    }

    @Override
    public UserEntity getUserById(Long Id) {
        Optional<UserEntity> optional = userDao.findById(Id);

        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public UserEntity updateUserEntityById(UserEntity userEntity) {
        return userDao.save(userEntity);
    }

    @Override
    public void daleteUserEntityById(Long Id) {
        userDao.deleteById(Id);
    }

    @Override
    public List<UserEntity> getCurrentUserList() {
        Sort sort =new Sort(Sort.Direction.DESC,"createTime");
        return userDao.findAll(sort);
    }

    @Override
    public Page<UserEntity> getPageUserList() {
        Sort sort =new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(0,5,sort);
        return userDao.findAll(pageable);
    }

    @Override
    public UserEntity getUserEntityByName(String name){
        return userDao.findByName(name);
    }

    @Override
    public List<UserVo> findUserVo(){
        //List<UserVo> userVo = userDao.findUserVo();
        return null;
    }


}
