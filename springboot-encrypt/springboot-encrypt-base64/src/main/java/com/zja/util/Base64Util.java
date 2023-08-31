/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-31 16:06
 * @Since:
 */
package com.zja.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 编码工具类
 * Java 8 提供的 Base64 效率最高
 *
 * @author: zhengja
 * @since: 2023/08/31 16:06
 */
public class Base64Util {

    /**
     * Base64 编码
     */
    public static String encoder(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 解码
     */
    public static String decoder(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(text), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String encoder = Base64Util.encoder("这是一句话。");
        System.out.println(encoder);
        String decoder = Base64Util.decoder(encoder);
        System.out.println(decoder);
    }
}
