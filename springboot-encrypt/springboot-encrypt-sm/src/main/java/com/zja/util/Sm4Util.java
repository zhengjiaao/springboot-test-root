/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 16:00
 * @Since:
 */
package com.zja.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * 对称加密：SM4
 *
 * <p>
 * 依赖：org.bouncycastle.bcprov-jdk15to18
 * 依赖：cn.hutool.hutool-crypto
 * </p>
 *
 * @author: zhengja
 * @since: 2023/08/08 16:00
 */
public class Sm4Util {

    /**
     * Base64 秘钥字符串
     */
    private static final String SECRET_KEY_ENCODED = "S5idpYLksJ02pGseBtIC5g==";

    public static SymmetricCrypto sm4() {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] key = decoder.decode(SECRET_KEY_ENCODED);
        return SmUtil.sm4(key);
    }

    /**
     * 加密
     *
     * @param data 示例：test中文
     * @return 示例：a1b322373df654cf7fbfe04cc6cd41e5
     */
    public static String encryptHex(String data) {
        return sm4().encryptHex(data, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 解密
     *
     * @param data
     * @return
     */
    public static String decryptStr(String data) {
        return sm4().decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }


    public static void main(String[] args) {
        String content = "test中文";

        SymmetricCrypto sm4 = SmUtil.sm4();
        //生成的秘钥,转成二进制,可以用Base64对秘钥转换为可见字符串存储
        SecretKey secretKey = sm4.getSecretKey();
        byte[] keys = secretKey.getEncoded();
        Base64.Encoder encoder = Base64.getEncoder();
        String key = encoder.encodeToString(keys);
        System.out.println(key);

        String encryptHex = sm4.encryptHex(content, CharsetUtil.CHARSET_UTF_8);
        System.out.println(encryptHex);

        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
    }
}
