package com.dist.util;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 1、获取公钥和私钥
 * 2、加解密功能
 */
public class KeyStoreRSA {

    /**
     * 获取密钥库(KeyStore)
     * @param keyStorePath 密钥库存储路径
     * @param password 密钥密码
     */
    private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    /**
     * 由KeyStore获取私钥
     * @param keyStorePath 密钥库存储路径
     * @param alias 别名
     * @param storePass 密钥库密码
     * @param keyPass 私钥密码
     */
    private static PrivateKey getPrivateKey(String keyStorePath, String alias, String storePass, String keyPass) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PrivateKey key = (PrivateKey) ks.getKey(alias, keyPass.toCharArray());
        return key;
    }

    /**
     * 由KeyStore获取公钥
     * @param keyStorePath 密钥库存储路径
     * @param alias 别名
     * @param storePass 密钥库密码
     */
    private static PublicKey getPublicKey(String keyStorePath, String alias, String storePass) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PublicKey key = ks.getCertificate(alias).getPublicKey();
        return key;
    }

    /**
     * 从KeyStore中获取公钥，并经BASE64编码解密
     * @param keyStorePath 密钥库存储路径
     * @param alias 别名
     * @param storePass 密钥库密码
     */
    public static String getStrPublicKey(String keyStorePath, String alias, String storePass) throws Exception {
        PublicKey key = getPublicKey(keyStorePath, alias, storePass);
        String strKey = BASE64.encryptBASE64(key.getEncoded());
        return strKey;
    }

    /**
     * 从KeyStore中获取私钥，并经BASE64编码加密
     * @param keyStorePath 密钥库存储路径
     * @param alias 别名
     * @param storePass 密钥库密码
     * @param keyPass 私钥密码
     */
    public static String getStrPrivateKey(String keyStorePath, String alias, String storePass, String keyPass) throws Exception {
        PrivateKey key = getPrivateKey(keyStorePath, alias, storePass, keyPass);
        String strKey = BASE64.encryptBASE64(key.getEncoded());
        return strKey;
    }

    /**
     * 通过公钥加密数据
     * @param publicKey 公钥
     * @param srcData 明文数据
     */
    public static String encryptByPublicKey(String publicKey, String srcData) throws Exception {
        //BASE64解密
        byte[] pk = BASE64.decryptBASE64(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        //获取公钥
        PublicKey pubKey = kf.generatePublic(spec);
        //对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] doFinal = cipher.doFinal(srcData.getBytes());
        //再经BASE64加密
        return BASE64.encryptBASE64(doFinal);
    }

    /**
     * 通过私钥解密数据
     * @param privateKey 私钥
     * @param data 解密数据
     */
    public static String descryptByPrivateKey(String privateKey, String data) throws Exception {
        // BASE64转码解密私钥
        byte[] pk = BASE64.decryptBASE64(privateKey);
        // BASE64转码解密密文
        byte[] text = BASE64.decryptBASE64(data);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // 获取私钥
        PrivateKey prvKey = kf.generatePrivate(spec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prvKey);
        byte[] doFinal = cipher.doFinal(text);
        return new String(doFinal);
    }

}
