package com.dist.utils;


import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 字符串解码
 * @author yangmin
 * @date 2018/6/29.
 */
public class DecoderUtil {

    /**
     * url解码
     *
     * @param s
     *            待解码的字符串
     * @return 解码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String s)
            throws UnsupportedEncodingException {
        return urlDecode(s, SecurityDefineUtil.Charset);
    }

    /**
     * url解码
     *
     * @param s
     *            待解码的字符串
     * @param charsetName
     *            用于解码的字符集
     * @return 解码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String s, String charsetName)
            throws UnsupportedEncodingException {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return URLDecoder.decode(s, charsetName);
    }

    /**
     * base64解码
     *
     * @param s
     *            待解码的字符串
     * @return 解码后的字符串
     * @throws IOException
     */
    public static String base64Decode(String s) throws IOException {
        return base64Decode(s, SecurityDefineUtil.Charset);
    }

    /**
     * base64解码
     *
     * @param s
     *            待解码的字符串
     * @param charsetName
     *            用于解码的字符集
     * @return 解码后的字符串
     * @throws IOException
     */
    public static String base64Decode(String s, String charsetName)
            throws IOException {
        if (s == null || s.isEmpty()) {
            return "";
        }
        Base64 decoder = new Base64();
        byte[] bytes = decoder.decode(s.getBytes());
        return new String(bytes, charsetName);
    }

    public static String aes256(String content) {
        if(content==null) {
            return null;
        }
        byte[] bytes=null;
        try {
            bytes=ParseUtil.hex2byte(content);
        }
        catch (Exception e)
        {
            Base64 decoder = new Base64();
            bytes = decoder.decode(content.getBytes());
        }
        byte[] decryptData = aes256(SecurityDefineUtil.AESKey.getBytes(),
                bytes);
        return new String(decryptData);
    }

    public static byte[] aes256(byte[] contentArray) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SecurityDefineUtil.AESKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(contentArray);
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static byte[] aes256(byte[] keyArray, byte[] contentArray) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyArray, "AES");
        try {
            Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(contentArray);
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static String rsaDecryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }
}
