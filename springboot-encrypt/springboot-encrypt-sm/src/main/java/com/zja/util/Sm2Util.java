/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 14:36
 * @Since:
 */
package com.zja.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 非对称加密和签名：SM2
 */
public class Sm2Util {

    //秘钥大小
    private static final int KEY_SIZE = 1024;
    //后续放到常量类中去
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";

    private static KeyPair keyPair;

    private static Map<String, String> keyPairMap;
    private static org.bouncycastle.jce.provider.BouncyCastleProvider bouncyCastleProvider = null;

    /**
     * BouncyCastleProvider内的方法都为静态方法，GC不会回收
     */
    public static synchronized org.bouncycastle.jce.provider.BouncyCastleProvider getInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }

    //生成密钥，并存放密钥对
    static {
        keyPair = SecureUtil.generateKeyPair("SM2", KEY_SIZE);
        //将公钥和私钥存放，登录时会不断请求获取公钥
        storeRsa();
    }

    /**
     * 将密钥存入缓存
     * 建议：把密钥对放到redis的缓存中，避免在分布式场景中，出现拿着server1的公钥去server2解密的问题
     */
    private static void storeRsa() {
        keyPairMap = new HashMap<>();
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        keyPairMap.put(PUBLIC_KEY, publicKeyStr);

        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
        keyPairMap.put(PRIVATE_KEY, privateKeyStr);
    }

    /**
     * 公钥加密
     * @param plainText 明文
     * @return 密文
     */
    public static String encrypt(String plainText) {
        SM2 sm2 = SmUtil.sm2(getPrivateKey(), getPublicKey());
        return sm2.encryptBase64(plainText, KeyType.PublicKey);
    }

    /**
     * 使用私钥解密
     *
     * @param cipherText 公钥加密的数据
     * @return 私钥解密出来的数据
     */
    public static String decrypt(String cipherText) {
        SM2 sm2 = SmUtil.sm2(getPrivateKey(), getPublicKey());
        return sm2.decryptStr(cipherText, KeyType.PrivateKey);
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        return keyPairMap.get(PUBLIC_KEY);
    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey() {
        return keyPairMap.get(PRIVATE_KEY);
    }

    /**
     * 示例：公钥加密，私钥解密
     */
    public static void main(String[] args) throws Exception {
        System.out.println(Sm2Util.getPrivateKey());
        System.out.println(Sm2Util.getPublicKey());

        String encrypt = Sm2Util.encrypt("明文666");
        System.out.println(encrypt);
        System.out.println(Sm2Util.decrypt(encrypt));
    }
}
