package com.zja.apache.codec;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 摘要
 *
 * @Author: zhengja
 * @Date: 2024-12-11 17:15
 */
public class DigestUtilsExample {
    public static void main(String[] args) {
        String originalText = "Hello, World! 你好，世界！";

        // MD5 摘要
        String md5Digest = DigestUtils.md5Hex(originalText);
        System.out.println("MD5 Digest: " + md5Digest);

        // SHA-256 摘要
        String sha256Digest = DigestUtils.sha256Hex(originalText);
        System.out.println("SHA-256 Digest: " + sha256Digest);
    }
}