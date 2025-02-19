package com.zja.sensitive.word.hutool.service;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2025-02-19 11:20
 */
public class DesensitizedUtilTest {

    // 测试 hutool DesensitizedUtil 脱敏工具类


    @Test
    public void testDesensitizedUtil() {
        String userId = DesensitizedUtil.desensitized("12345678901", DesensitizedUtil.DesensitizedType.USER_ID);
        // String userId = StrUtil.desensitized("12345678901", DesensitizedUtil.DesensitizedType.USER_ID);

        String chineseName = DesensitizedUtil.desensitized("张三丰", DesensitizedUtil.DesensitizedType.CHINESE_NAME);

        String idCardNum = DesensitizedUtil.desensitized("610123199001011234", DesensitizedUtil.DesensitizedType.ID_CARD);

        String fixedPhone = DesensitizedUtil.desensitized("010-12345678", DesensitizedUtil.DesensitizedType.FIXED_PHONE);

        String mobilePhone = DesensitizedUtil.desensitized("13812345678", DesensitizedUtil.DesensitizedType.MOBILE_PHONE);

        String email = DesensitizedUtil.desensitized("zhengja@163.com", DesensitizedUtil.DesensitizedType.EMAIL);

        String password = DesensitizedUtil.desensitized("123456", DesensitizedUtil.DesensitizedType.PASSWORD);

        String carLicense = DesensitizedUtil.desensitized("粤A12345", DesensitizedUtil.DesensitizedType.CAR_LICENSE);

        String bankCard = DesensitizedUtil.desensitized("6228480400000000000", DesensitizedUtil.DesensitizedType.BANK_CARD);

        String ipv4 = DesensitizedUtil.desensitized("192.168.0.1", DesensitizedUtil.DesensitizedType.IPV4);

        String ipv6 = DesensitizedUtil.desensitized("2001:0db8:85a3:0000:0000:8a2e:0370:7334", DesensitizedUtil.DesensitizedType.IPV6);

        String firstMask = DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.FIRST_MASK);

        String hideStartEnd = StrUtil.hide("1234567890", 2, 4);

        System.out.println(userId);
        System.out.println(chineseName);
        System.out.println(idCardNum);
        System.out.println(fixedPhone);
        System.out.println(mobilePhone);
        System.out.println(email);
        System.out.println(password);
        System.out.println(carLicense);
        System.out.println(bankCard);
        System.out.println(ipv4);
        System.out.println(ipv6);
        System.out.println(firstMask);
        System.out.println(hideStartEnd);
    }
}
