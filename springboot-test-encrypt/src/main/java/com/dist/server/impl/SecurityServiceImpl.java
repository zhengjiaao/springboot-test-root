package com.dist.server.impl;

import com.dist.base.dto.RSAEncryptKeyPairDTO;
import com.dist.security.IEncrypt;
import com.dist.server.SecurityService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/12/13.
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    //获取容器中的秘钥对
    @Autowired
    @Qualifier(value = "RSAEncryptKeyPair")
    private RSAEncryptKeyPairDTO RSAEncryptKeyPair;

    //使用这个获取到系统的加密
    @Autowired
    @Qualifier(value = "systemEncrypt")
    IEncrypt encrypt;

    /**
     * 获取公钥
     * @return
     */
    @Override
    public String publicKey() {

        //64进制范围字符（有特殊字符，tomcat会忽略）
        return Base64.encodeBase64String(RSAEncryptKeyPair.getPublicKey().
                getEncoded());
        //16进制范围进行数据显示
        /*return Hex.encodeHexString(RSAEncryptKeyPair.getPublicKey().
                getEncoded());*/
    }

    /**
     * 获取私钥
     * @return
     */
    @Override
    public String getPrivateKey() {

        //64进制范围字符（有特殊字符，tomcat会忽略）
        return Base64.encodeBase64String(RSAEncryptKeyPair.getPrivateKey().
                getEncoded());
        //16进制范围进行数据显示
        /*return Hex.encodeHexString(RSAEncryptKeyPair.getPublicKey().
                getEncoded());*/
    }

    /**
     * 解密
     * @param data
     * @return
     */
    @Override
    public String decryptByPrivateKey(String data) {
        try {
            return encrypt.decrypt(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密
     * @param data
     * @return
     */
    @Override
    public String encryptByPublicKey(String data) {
        try {
            return encrypt.encrypt(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
