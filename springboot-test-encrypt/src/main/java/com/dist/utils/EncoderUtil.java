package com.dist.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author yangmin
 * @date 2018/6/29.
 */
public class EncoderUtil {
    /**
     * url编码
     *
     * @param s
     *            待编码的字符串
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String s) throws UnsupportedEncodingException {
        return urlEncode(s, SecurityDefineUtil.Charset);
    }

    /**
     * url编码
     *
     * @param s
     *            待编码的字符串
     * @param charsetName
     *            编码使用的字符集
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String s, String charsetName) throws UnsupportedEncodingException {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return URLEncoder.encode(s, charsetName);
    }

    /**
     * base64编码
     *
     * @param s
     *            待编码的字符串
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String base64Encode(String s) throws UnsupportedEncodingException {
        return base64Encode(s, SecurityDefineUtil.Charset);
    }

    /**
     * base64编码
     *
     * @param s
     *            待编码的字符串
     * @param charsetName
     *            编码使用的字符集
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String base64Encode(String s, String charsetName) throws UnsupportedEncodingException {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return new String(new Base64().encode(s.getBytes(charsetName)));
    }

    /**
     * 将字符串编码成ldap可识别的字符串
     *
     * @param s
     *            待编码的字符串
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static String ldapEncode(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return ldapEncode(s, SecurityDefineUtil.Charset);
    }

    /**
     * 将字符串编码成ldap可识别的字符串
     *
     * @param s
     *            待编码的字符串
     * @param charsetName
     *            编码使用的字符集
     * @return 编码后的字符串
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static String ldapEncode(String s, String charsetName)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (s == null || s.isEmpty()) {
            return "";
        }
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] digest = md.digest(s.getBytes(charsetName));

        Base64 encoder = new Base64();
        return "{SHA}" + new String(encoder.encode(digest));
    }

    public static String aes256(String content) {
        return aes256Base64(content);
    }

    public static String aes256hex(String content) {
        byte[] encryptData = aes256(SecurityDefineUtil.AESKey.getBytes(), content.getBytes());
        return ParseUtil.byte2hex(encryptData);
    }
    public static String aes256Base64(String content) {
        byte[] encryptData = aes256(SecurityDefineUtil.AESKey.getBytes(), content.getBytes());
        Base64 encoder = new Base64();
        return new String(encoder.encode(encryptData));
    }
    public static byte[] aes256(byte[] contentArray) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SecurityDefineUtil.AESKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
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
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(contentArray);
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static String md5(String content) {
        try {
            return ParseUtil.byte2hex(md5(content.getBytes(SecurityDefineUtil.Charset)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static byte[] md5(byte[] contentArray) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(contentArray);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static String sha(String content) {
        try {
            return ParseUtil.byte2hex(sha(content.getBytes(SecurityDefineUtil.Charset)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static byte[] sha(byte[] contentArray) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            return md.digest(contentArray);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * 公钥加密
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static String rsaEncryptByPublicKey(byte[] data, byte[] key) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey generatePublic = kf.generatePublic(x509EncodedKeySpec);
        Cipher ci = Cipher.getInstance("RSA");
        ci.init(Cipher.ENCRYPT_MODE, generatePublic);
        return Base64.encodeBase64String(ci.doFinal(data));
    }
}
