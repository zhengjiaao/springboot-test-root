package com.zja.apache.codec;

import org.apache.commons.codec.net.URLCodec;

/**
 * CRC 校验
 * CRC 校验用于计算数据的校验值，常用于数据完整性检查。
 *
 * @Author: zhengja
 * @Date: 2024-12-11 17:11
 */
public class UrlCodecExample {
    public static void main(String[] args) {
        try {
            URLCodec codec = new URLCodec();

            String originalUrl = "https://example.com/search?q=hello world&lang=en";
            String encodedUrl = codec.encode(originalUrl);
            System.out.println("URL Encoded: " + encodedUrl);

            String decodedUrl = codec.decode(encodedUrl);
            System.out.println("URL Decoded: " + decodedUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
