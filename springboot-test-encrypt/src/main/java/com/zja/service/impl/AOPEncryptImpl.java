package com.zja.service.impl;

import com.zja.annotation.DREncrypt;
import com.zja.entity.User;
import com.zja.service.AOPEncrypt;
import org.springframework.stereotype.Service;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/17 16:38
 */
@Service
public class AOPEncryptImpl implements AOPEncrypt {

    @Override
    @DREncrypt
    public StringBuffer getUserPwd(String usercode) throws Exception {
        StringBuffer stringBuffer = new StringBuffer(usercode);
        System.out.println("明文："+stringBuffer);
        return stringBuffer;
    }

    @Override
    @DREncrypt
    public User updateUser(User user) throws Exception {
        System.out.println("明文："+user.toString());
        return user;
    }
}
