package com.dist.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-04-03 13:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc: 基础加密组件
 */
public class BASE64 {

    /**
     * BASE64 基础解密
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64 基础加密
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key).replace("\r", "").replace("\n", "");
    }
}
