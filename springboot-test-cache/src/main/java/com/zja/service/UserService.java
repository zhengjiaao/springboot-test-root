package com.zja.service;

import com.zja.entity.UserEntity;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/15 10:36
 */
public interface UserService {

    /**
     * 测试 ehcache 保存缓存
     * @param id
     * @return
     */
    UserEntity getUserById(Integer id);

    /**
     * 测试 ehcache 清楚缓存
     * @param userEntity
     */
    void saveUser(UserEntity userEntity);


    /**
     * 以下为模拟数据库操作
     * @param typeId
     * @return
     */
    String save(String typeId);

    String update(String typeId);

    String delete(String typeId);

    String select(String typeId);

    /**
     * 复杂的缓存
     * @param name
     * @return
     */
    UserEntity selectByName(String name);
}
