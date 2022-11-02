package com.zja.controller;

import com.zja.util.Sm2Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sm2")
@Api(tags = {"SM2Controller"}, description = "前端加密，后端解密，保证系统安全")
public class SM2Controller {

    @ApiOperation(value = "获取非对称加密公钥", notes = "获取非对称加密公钥，通过前端加密之后到达后端对数据进行解密")
    @GetMapping("publicKey")
    public String getPublicKey() {
        return Sm2Util.getPublicKey();
    }

    @ApiOperation(value = "获取非对称解密私钥", notes = "获取非对称解密私钥，通过前端加密之后到达后端对数据进行解密")
    @GetMapping("privatekey")
    public String getPrivateKey() {
        return Sm2Util.getPrivateKey();
    }

    @ApiOperation(value = "服务端通过公钥加密", notes = "通过后台进行明文数据的加密")
    @GetMapping("encrypt")
    public String encrypt(
            @ApiParam(value = "明文密文") @RequestParam String plainText) throws Exception {
        return Sm2Util.encrypt(plainText);
    }

    @ApiOperation(value = "服务端通过私钥解密", notes = "通过传过来的加密数据然后通过私钥进行数据解密")
    @GetMapping("decrypt")
    public String decryptByPrivateKey(
            @ApiParam(value = "加密后的数据") @RequestParam String cipherText) throws Exception {
        return Sm2Util.decrypt(cipherText);
    }

}
