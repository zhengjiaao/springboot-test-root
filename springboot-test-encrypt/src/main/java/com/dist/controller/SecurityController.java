package com.dist.controller;

import com.dist.server.SecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @company: Dist
 * @date：2018/12/19
 * @author: xupp
 * desc：安全服务接口
 */
@RestController
@RequestMapping(value = "rest/secrity/v1")
@Api(tags = {"SecurityController"}, description = "安全服务：前端加密，后端解密，保证系统安全")
public class SecurityController{

    /**
     * 获取公钥接口，返回给前端容器中的公钥
     * @return
     * @throws Exception
     */
    @Autowired
    private SecurityService secretService;

    @ApiOperation( value="获取非对称加密公钥",notes = "获取非对称加密公钥，通过前端加密之后到达后端对数据进行解密(建议只调用一次然后本地进行持久化)")
    @GetMapping("rsa/publicKey")
    public String getPublicKey(){
        return secretService.publicKey();
    }

    @ApiOperation( value="获取非对称解密私钥",notes = "获取非对称解密私钥，通过前端加密之后到达后端对数据进行解密")
    @GetMapping("rsa/privatekey")
    public String getPrivateKey(){
        return secretService.publicKey();
    }

    @ApiOperation( value="服务端通过公钥加密",notes = "通过后台进行明文数据的加密")
    @GetMapping("rsa/encrypt/{data}")
    public String encrypt(
            @ApiParam(value = "明文密文")
            @PathVariable @NotNull String data
    ) throws Exception {
        return secretService.encryptByPublicKey(data);
    }

    @ApiOperation( value="服务端通过私钥解密",notes = "通过传过来的加密数据然后通过私钥进行数据解密(测试)")
    @GetMapping("rsa/decrypt")
    public String decryptByPrivateKey(
        @ApiParam(value = "加密后的数据")
        @RequestParam(value = "data") String data
    ) throws Exception {
        return secretService.decryptByPrivateKey(data);
    }

}
