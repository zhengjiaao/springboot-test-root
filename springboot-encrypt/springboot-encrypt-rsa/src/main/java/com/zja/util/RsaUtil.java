/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 11:21
 * @Since:
 */
package com.zja.util;

import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 加解密
 * 密钥类型：非对称密钥对(公钥+私钥)
 * 每次重启之后，都会生成一对新的[公私钥]
 */
public class RsaUtil {

    //秘钥大小
    private static final int KEY_SIZE = 2048;
    //后续放到常量类中去
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";

    private static KeyPair keyPair;

    private static Map<String, String> rsaMap;
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

    //生成RSA，并存放密钥对
    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", getInstance());
            SecureRandom random = new SecureRandom();
            generator.initialize(KEY_SIZE, random);
            keyPair = generator.generateKeyPair();
            //将公钥和私钥存放，登录时会不断请求获取公钥
            //建议放到redis的缓存中，避免在分布式场景中，出现拿着server1的公钥去server2解密的问题
            storeRsa();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将RSA存入缓存
     */
    private static void storeRsa() {
        rsaMap = new HashMap<>();
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        rsaMap.put(PUBLIC_KEY, publicKeyStr);

        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
        rsaMap.put(PRIVATE_KEY, privateKeyStr);
    }

    /**
     * 使用公钥加密
     *
     * @param plainText 明文内容
     * @return byte[]
     */
    public static byte[] encryptByPublicKey(String plainText) throws Exception {
        String encode = URLEncoder.encode(plainText, "utf-8");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        //获取公钥指数
        BigInteger e = rsaPublicKey.getPublicExponent();
        //获取公钥系数
        BigInteger n = rsaPublicKey.getModulus();
        //获取明文字节数组
        BigInteger m = new BigInteger(encode.getBytes());
        //进行明文加密
        BigInteger res = m.modPow(e, n);
        return res.toByteArray();
    }

    /**
     * 使用私钥解密
     *
     * @param cipherText 加密后的字节数组
     * @return 解密后的数据
     */
    public static String decryptByPrivateKey(byte[] cipherText) throws Exception {
        RSAPrivateKey prk = (RSAPrivateKey) keyPair.getPrivate();
        // 获取私钥参数-指数/系数
        BigInteger d = prk.getPrivateExponent();
        BigInteger n = prk.getModulus();
        // 读取密文
        BigInteger c = new BigInteger(cipherText);
        // 进行解密
        BigInteger m = c.modPow(d, n);
        // 解密结果-字节数组
        byte[] mt = m.toByteArray();
        //转成String,此时是乱码
        String en = new String(mt);
        //再进行编码,最后返回解密后得到的明文
        return URLDecoder.decode(en, "UTF-8");
    }

    /**
     * 公钥加密
     * @param plainText 明文
     * @return 密文
     */
    public static String encrypt(String plainText) throws Exception {
        byte[] bytes = encryptByPublicKey(plainText);
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 使用私钥解密
     *
     * @param cipherText 公钥加密的数据
     * @return 私钥解密出来的数据
     */
    public static String decrypt(String cipherText) throws Exception {
        return decryptByPrivateKey(Base64.decodeBase64(cipherText));
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        return rsaMap.get(PUBLIC_KEY);
    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey() {
        return rsaMap.get(PRIVATE_KEY);
    }

    /**
     * 示例：公钥加密，私钥解密
     */
    public static void main(String[] args) throws Exception {
        System.out.println(RsaUtil.getPrivateKey());
        System.out.println(RsaUtil.getPublicKey());
        byte[] usernames = RsaUtil.encryptByPublicKey("明文：123");
        System.out.println(RsaUtil.decryptByPrivateKey(usernames));

        String encrypt = RsaUtil.encrypt("明文666");
        System.out.println(encrypt);
        System.out.println(RsaUtil.decrypt(encrypt));
    }

}
