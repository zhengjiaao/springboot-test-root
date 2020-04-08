package com.dist.controller;

import com.dist.util.KeyStoreRSA;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-04-02 17:50
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：推荐使用 星数4
 */
@RestController
@RequestMapping("rest/rsa")
@Api(tags = {"RSA-KeyStoreRSAController"},description = "加解密")
public class KeyStoreRSAController {

    @GetMapping(value = "v1/encrypt")
    @ApiOperation(value = "公钥加密",httpMethod = "GET")
    public Object encrypt(@ApiParam("明文") @RequestParam(name = "srcData") String srcData) throws Exception {
        String strPublicKey = KeyStoreRSA.getStrPublicKey("D:\\mykeystore.keystore", "my_alias", "storepass123");
        return KeyStoreRSA.encryptByPublicKey(strPublicKey,srcData);
    }

    @GetMapping(value = "v1/decrypt")
    @ApiOperation(value = "私钥解密",httpMethod = "GET")
    public Object decrypt(@ApiParam("密文") @RequestParam(name = "data") String data) throws Exception {
        String strPrivateKey = KeyStoreRSA.getStrPrivateKey("D:\\mykeystore.keystore", "my_alias", "storepass123", "keypass123");
        return KeyStoreRSA.descryptByPrivateKey(strPrivateKey,data);
    }

    @GetMapping(value = "v1/publickey")
    @ApiOperation(value = "获取公钥",httpMethod = "GET")
    public Object getPublickey() throws Exception {
        return KeyStoreRSA.getStrPublicKey("D:\\mykeystore.keystore","my_alias","storepass123");
    }

    @GetMapping(value = "v1/privatekey")
    @ApiOperation(value = "获取私钥",httpMethod = "GET")
    public Object getPrivateKey() throws Exception {
        return KeyStoreRSA.getStrPrivateKey("D:\\mykeystore.keystore","my_alias","storepass123","keypass123");
    }

}
