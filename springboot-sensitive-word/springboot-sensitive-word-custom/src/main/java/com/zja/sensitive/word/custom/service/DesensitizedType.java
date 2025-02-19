package com.zja.sensitive.word.custom.service;

import cn.hutool.core.util.DesensitizedUtil;

/**
 * 脱敏类型枚举
 *
 * <p>
 * 参考 {@link DesensitizedUtil.DesensitizedType}
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-10-10 13:11
 */
public enum DesensitizedType {
    // 词库脱敏（需指定词库路径） => 脱敏词库（例如：傻-->*、傻子-->**）
    WORD,
    // 隐藏(需指定隐藏范围，默认隐藏全部) => *
    HIDE,
    // 用户id => 0
    USER_ID,
    // 中文名 => 李*
    CHINESE_NAME,
    // 身份证号 => 1*************88
    ID_CARD,
    // 座机号 => 021-******89
    FIXED_PHONE,
    // 手机号 => 159****0000
    MOBILE_PHONE,
    // 地址 => 上海市浦东新区********
    ADDRESS,
    // 电子邮件 => 1******@qq.com
    EMAIL,
    // 密码 => *******
    PASSWORD,
    // 中国大陆车牌，包含普通车辆、新能源车辆 => 沪A******
    CAR_LICENSE,
    // 银行卡 => 6226**********0000
    BANK_CARD,
    // ipv4 => 192.*.*.*
    IPV4,
    // ipv6 => fe80:*:*:*:*:*:*:*
    IPV6,
    // 仅显示第一个字符 => L******
    FIRST_MASK,
    // 清空 => ""
    CLEAR_TO_NULL,
    // 清空 => null
    CLEAR_TO_EMPTY;
}
