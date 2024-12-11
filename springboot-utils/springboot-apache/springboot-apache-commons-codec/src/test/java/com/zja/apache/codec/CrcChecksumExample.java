package com.zja.apache.codec;

import org.apache.commons.codec.binary.BinaryCodec;

/**
 * Binary Encoding/Decoding
 * Binary 编码将二进制数据转换为二进制字符串，常用于特定的二进制数据处理场景。
 *
 * @Author: zhengja
 * @Date: 2024-12-11 17:13
 */
public class CrcChecksumExample {
    public static void main(String[] args) {
        try {
            BinaryCodec codec = new BinaryCodec();

            String originalText = "Hello";
            byte[] bytes = originalText.getBytes();

            // Binary Encoding
            byte[] binaryEncoded = codec.encode(bytes);
            System.out.println("Binary Encoded: " + new String(binaryEncoded));

            // Binary Decoding
            byte[] binaryDecodedBytes = codec.decode(binaryEncoded);
            String binaryDecoded = new String(binaryDecodedBytes);
            System.out.println("Binary Decoded: " + binaryDecoded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
