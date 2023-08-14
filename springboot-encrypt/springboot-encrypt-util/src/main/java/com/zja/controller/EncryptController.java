package com.zja.controller;

import com.zja.entity.User;
import com.zja.security.IEncrypt;
import com.zja.service.AOPEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**测试加解密，数据入库加解密
 * @author zhengja@dist.com.cn
 * @data 2019/6/17 13:45
 */
@Api(tags = {"EncryptController"})
@RequestMapping(value = "rest/encrypt")
@RestController
public class EncryptController {

    /**
     * 数据入库加解密
     */
    @Autowired
    @Qualifier("dbEncrypt")
    IEncrypt encrypt;

    /**
     * 系统加解密
     */
    @Autowired
    @Qualifier("systemEncrypt")
    IEncrypt iEncrypt;

    @Autowired
    AOPEncrypt aopEncrypt;

    /**
     * 加密
     */
    @ApiOperation(value = "加密操作",notes = "输入明文进行加密")
    @RequestMapping(value = "v1/encrypt",method = {RequestMethod.GET})
    public String encrypt(@ApiParam(value = "待加密明文") @RequestParam String clearText) throws Exception {
        System.out.println("明文："+clearText);
        String encrypt = iEncrypt.encrypt(clearText);
        System.out.println("系统加密："+encrypt);
        String decrypt = iEncrypt.decrypt(encrypt);
        System.out.println("系统解密："+decrypt);
        String encryptTest = encryptTest(decrypt);
        System.out.println("入库解密："+encryptTest);
        return encryptTest;
    }

    private String encryptTest(String decrypt) throws Exception {
        String encrypt = this.encrypt.encrypt(decrypt);
        System.out.println("出库加密："+encrypt);
        return this.encrypt.decrypt(encrypt);
    }

    @ApiOperation(value = "AOP加密字符串操作",notes = "后端加密，前端解密")
    @RequestMapping(value = "v2/encrypt",method = {RequestMethod.GET})
    public StringBuffer aopEncrypt(@ApiParam(value = "待加密明文") @RequestParam String clearText) throws Exception {
        return aopEncrypt.getUserPwd(clearText);
    }

    @ApiOperation(value = "AOP加密对象属性操作",notes = "后端加密，前端解密")
    @RequestMapping(value = "v3/encrypt",method = {RequestMethod.POST})
    public User aopEncryptEntity(@ApiParam(value = "待加密明文") @RequestBody User user) throws Exception {
        return aopEncrypt.updateUser(user);
    }
}
