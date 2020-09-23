package com.zja.encryptionDecrypt.utils;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/13 11:15
 */
public class DESUtils {
    /**
     * 生成随机密钥
     *
     * @param size
     *            位数
     * @return
     */
    public static String generateRandomKey(int size) {
        StringBuilder key = new StringBuilder();
        String chars = "0123456789ABCDEF";
        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * (chars.length() - 1));
            key.append(chars.charAt(index));
        }
        return key.toString();
    }

    /**
     * DES加密
     *
     * @param key
     *            密钥信息
     * @param content
     *            待加密信息
     * @return
     * @throws Exception
     */
    public static byte[] encodeDES(byte[] key, byte[] content) throws Exception {
        // 不是8的倍数的，补足
        if (key.length % 8 != 0) {
            int groups = key.length / 8 + (key.length % 8 != 0 ? 1 : 0);
            byte[] temp = new byte[groups * 8];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }

        // 不是8的倍数的，补足
        byte[] srcBytes = content;
        if (srcBytes.length % 8 != 0) {
            int groups = content.length / 8 + (content.length % 8 != 0 ? 1 : 0);
            srcBytes = new byte[groups * 8];
            Arrays.fill(srcBytes, (byte) 0);
            System.arraycopy(content, 0, srcBytes, 0, content.length);
        }

        IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv, sr);
        byte[] tgtBytes = cipher.doFinal(srcBytes);
        return tgtBytes;
    }

    /**
     * DES解密
     *
     * @param key
     *            密钥信息
     * @param content
     *            待加密信息
     * @return
     * @throws Exception
     */
    public static byte[] decodeDES(byte[] key, byte[] content) throws Exception {
        // 不是8的倍数的，补足
        if (key.length % 8 != 0) {
            int groups = key.length / 8 + (key.length % 8 != 0 ? 1 : 0);
            byte[] temp = new byte[groups * 8];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        // 不是8的倍数的，补足
        byte[] srcBytes = content;
        if (srcBytes.length % 8 != 0) {
            int groups = content.length / 8 + (content.length % 8 != 0 ? 1 : 0);
            srcBytes = new byte[groups * 8];
            Arrays.fill(srcBytes, (byte) 0);
            System.arraycopy(content, 0, srcBytes, 0, content.length);
        }

        IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv, sr);
        byte[] tgtBytes = cipher.doFinal(content);
        return tgtBytes;
    }

    @Test
    public void desTest() throws Exception{
        //获取随机密钥
        String key = generateRandomKey(16); //长度
        System.out.println("随机密钥："+key);
        String str = "DREAMING.XIN";
        //des加密
        byte[] encodeDES = encodeDES(key.getBytes(), str.getBytes());
        System.out.println("加密结果："+encodeDES);

        //des解密
        byte[] decodeDES = decodeDES(key.getBytes(), encodeDES);
        System.out.println("减密结果： "+new String(decodeDES));
    }
    /*
    执行结果：

　　随机密钥：AB09C55631425D67
　　加密结果：[B@19fc4e
　　减密结果： DREAMING.XIN
     */
}
