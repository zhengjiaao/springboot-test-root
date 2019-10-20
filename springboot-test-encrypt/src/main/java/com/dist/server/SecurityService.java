package com.dist.server;

/**
 * @author xupp
 * @date 2018/12/18
 * @desc rsa数据加密服务
 * */
public interface SecurityService {
    /**
     * 获取公钥
     * @return String
     */
    String publicKey();

    /**
     * 获取私钥
     * @return
     */
    String getPrivateKey();

    /**
     * 通过私钥进行解密
     * @return String
     */
    String decryptByPrivateKey(String data);

    /**
     * 通过公钥以及数据进行数据加密
     * @return String
     */
    String encryptByPublicKey(String data);
}
