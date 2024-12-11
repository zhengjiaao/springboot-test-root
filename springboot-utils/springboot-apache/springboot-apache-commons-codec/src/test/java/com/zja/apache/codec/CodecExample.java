package com.zja.apache.codec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @Author: zhengja
 * @Date: 2024-12-11 16:28
 */
public class CodecExample {

    public static void main(String[] args) throws DecoderException {
        // Base64 编码/解码
        String originalText = "Hello, World!";
        String base64Encoded = Base64.encodeBase64String(originalText.getBytes());
        System.out.println("Base64 Encoded: " + base64Encoded);
        String base64Decoded = new String(Base64.decodeBase64(base64Encoded));
        System.out.println("Base64 Decoded: " + base64Decoded);

        // Hex 编码/解码
        String hexEncoded = Hex.encodeHexString(originalText.getBytes());
        System.out.println("Hex Encoded: " + hexEncoded);
        byte[] hexDecodedBytes = Hex.decodeHex(hexEncoded.toCharArray());
        String hexDecoded = new String(hexDecodedBytes);
        System.out.println("Hex Decoded: " + hexDecoded);

        // Soundex 编码
        Soundex soundex = new Soundex();
        String soundexEncoded = soundex.soundex(originalText);
        System.out.println("Soundex Encoded: " + soundexEncoded);

        // RefinedSoundex 编码
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        String refinedSoundexEncoded = refinedSoundex.soundex(originalText);
        System.out.println("Refined Soundex Encoded: " + refinedSoundexEncoded);

        // MD5 摘要
        String md5Digest = DigestUtils.md5Hex(originalText);
        System.out.println("MD5 Digest: " + md5Digest);

        // SHA-256 摘要
        String sha256Digest = DigestUtils.sha256Hex(originalText);
        System.out.println("SHA-256 Digest: " + sha256Digest);
    }

    @Test
    public void test_1() throws Exception {
        String str = "你好，世界；";
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
        String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
        System.out.println(encodedString); // 输出编码后的字符串
        byte[] decodedBytes = Base64.decodeBase64(encodedString);
        System.out.println(new String(decodedBytes, StandardCharsets.UTF_8)); // 输出解码后的字符串
    }
}
