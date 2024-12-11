package com.zja.apache.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * @Author: zhengja
 * @Date: 2024-12-11 17:23
 */
public class Base64Example {
    public static void main(String[] args) {
        // Base64 编码/解码
        String originalText = "Hello, World!你好，世界";
        String base64Encoded = Base64.encodeBase64String(originalText.getBytes());
        System.out.println("Base64 Encoded: " + base64Encoded);
        String base64Decoded = new String(Base64.decodeBase64(base64Encoded));
        System.out.println("Base64 Decoded: " + base64Decoded);
    }
}
