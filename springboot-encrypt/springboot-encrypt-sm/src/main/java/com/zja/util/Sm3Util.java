/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 16:03
 * @Since:
 */
package com.zja.util;

import cn.hutool.crypto.SmUtil;

/**
 * 摘要加密算法：SM3
 *
 * @author: zhengja
 * @since: 2023/08/08 16:03
 */
public class Sm3Util {

    /**
     * sm3加密
     *
     * @return 示例 136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be
     */
    public static String sm3(String data) {
        return SmUtil.sm3(data);
    }

    public static void main(String[] args) {
        String data = "test中文";
        String s = SmUtil.sm3(data);
        System.out.println(s);
        //614a2eb8e9b14be901faf46155c40caa09d49cc61a1382447d28b98254c2234c
    }
}
