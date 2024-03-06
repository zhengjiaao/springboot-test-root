package com.zja.service;

import com.zja.entity.UserEntity;

import java.util.List;

/**存储过程测试接口
 * @author: zhengja
 * @since: 2019/6/24 14:23
 */
public interface ProcedureService {
    /**
     *获取所有用户信息
     * @return
     */
    List<UserEntity> getUsers();

    /**
     * 根据年龄获取用户信息
     * @param age
     * @return
     */
    List<UserEntity> getUsersByAge(String age);
}
