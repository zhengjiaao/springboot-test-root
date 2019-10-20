package com.dist.base.dto;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by Administrator on 2018/12/13.
 * 这个用于记录初始化的时候生成的 公私钥秘钥对信息
 */
public class RSAEncryptKeyPairDTO implements Serializable {

//    私钥
    private RSAPrivateKey privateKey;


//    公钥（公钥传给前端）
    private RSAPublicKey publicKey;


    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public RSAEncryptKeyPairDTO() {
    }

    public RSAEncryptKeyPairDTO(RSAPrivateKey privateKey,
                                RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
}
