/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-09 17:47
 * @Since:
 */
package com.zja.hutool.id;

import cn.hutool.core.util.RandomUtil;

/**
 * @author: zhengja
 * @since: 2023/08/09 17:47
 */
public class RandomUtilExample {
    public static void main(String[] args) {
        // 生成一个随机的整数
        int randomInt = RandomUtil.randomInt();
        System.out.println("随机整数：" + randomInt);

        // 生成一个指定范围内的随机整数
        int randomRangeInt = RandomUtil.randomInt(1, 100);
        System.out.println("1到100之间的随机整数：" + randomRangeInt);

        // 生成一个随机的长整数
        long randomLong = RandomUtil.randomLong();
        System.out.println("随机长整数：" + randomLong);

        // 生成一个指定长度的随机字符串
        String randomString = RandomUtil.randomString(10);
        System.out.println("10位随机字符串：" + randomString);

        // 生成一个指定长度和字符范围的随机字符串
        String randomStringWithRange = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz", 8);
        System.out.println("8位由小写字母组成的随机字符串：" + randomStringWithRange);

    }
}
