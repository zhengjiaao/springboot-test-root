package com.zja.service;

import com.zja.entity.User;

/**
 * @author: zhengja
 * @since: 2019/6/17 16:36
 */
public interface AOPEncrypt {

    /**
     * 获取用户明文密码
     * @param usercode
     * @return String
     * @throws Exception
     */
    StringBuffer getUserPwd(String usercode) throws Exception ;

    /**
     * 修改用户信息
     * @param user
     * @return User
     * @throws Exception
     */
    User updateUser(User user) throws Exception ;
}
