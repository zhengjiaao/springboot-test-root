/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-05 14:42
 * @Since:
 */
package com.zja.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

/**
 * SM4 自定义工具类
 *
 * <p>
 * 依赖：org.bouncycastle.bcprov-jdk15to18
 * </p>
 *
 * @author: zhengja
 * @since: 2023/09/05 14:42
 */
public class MySm4Util {
    private static final String ALGORITHM = "SM4";
    private static final String TRANSFORMATION = "SM4/ECB/PKCS5Padding";
    private static final String KEY = "S5idpYLksJ02pGseBtIC5g==";

    /**
     * SM4算法目前只支持128位（即密钥16字节）
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    static {
        // 防止内存中出现多次BouncyCastleProvider的实例
        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            //避免错误：Cannot find any provider supporting SM4/ECB/PKCS5Padding
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 加密
     *
     * @param plaintext
     * @return
     * @throws EncryptionException
     */
    public static String encrypt(String plaintext) throws EncryptionException {
        return encrypt(plaintext, KEY);
    }

    /**
     * 加密
     *
     * @param plaintext
     * @param key
     * @return
     * @throws EncryptionException
     */
    public static String encrypt(String plaintext, String key) throws EncryptionException {
        validateInput(plaintext);

        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new EncryptionException("Error occurred during encryption.", e);
        }
    }

    /**
     * 解密
     *
     * @param ciphertext
     * @return
     * @throws DecryptionException
     */
    public static String decrypt(String ciphertext) throws DecryptionException {
        return decrypt(ciphertext, KEY);
    }

    /**
     * 解密
     *
     * @param ciphertext
     * @param key
     * @return
     * @throws DecryptionException
     */
    public static String decrypt(String ciphertext, String key) throws DecryptionException {
        validateInput(ciphertext);

        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException("Error occurred during decryption.", e);
        }
    }

    private static void validateInput(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }
    }

    private static Cipher getCipher(int mode, String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        cipher.init(mode, keySpec);
        return cipher;
    }

    public static String generateKey() throws NoSuchAlgorithmException {
        SecretKey secretKey = generateSecretKey();
        // 获取生成的密钥的字节数组形式
        byte[] keyBytes = secretKey.getEncoded();
        // 可以将字节数组进行Base64编码等操作，以便存储和传输密钥
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        // 创建一个 SM4 密钥生成器实例
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        // 初始化密钥生成器为指定的密钥长度（128 bits）
        keyGenerator.init(DEFAULT_KEY_SIZE);
        // 生成密钥
        return keyGenerator.generateKey();
    }

    public static class EncryptionException extends Exception {
        public EncryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DecryptionException extends Exception {
        public DecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] args) throws EncryptionException, DecryptionException, NoSuchAlgorithmException {

        String text = "这是一段明文语句.";
        System.out.println("加密前：" + text);

        String encrypt = MySm4Util.encrypt(text);
        System.out.println("加密后：" + encrypt);

        String decrypt = MySm4Util.decrypt(encrypt);
        System.out.println("解密后：" + decrypt);

        //自定义秘钥 key

        String key = MySm4Util.generateKey();
        System.out.println("动态生成秘钥key：" + key);

        String encrypt2 = MySm4Util.encrypt(text, key);
        System.out.println("加密后：" + encrypt2);

        String decrypt2 = MySm4Util.decrypt(encrypt2, key);
        System.out.println("解密后：" + decrypt2);
    }
}
