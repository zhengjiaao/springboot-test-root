package com.zja.sensitive.word.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2024-10-10 13:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    @DesensitizedString(type = DesensitizedType.USER_ID)
    private String userId;
    @DesensitizedString(type = DesensitizedType.CHINESE_NAME)
    private String name;
    @DesensitizedString(type = DesensitizedType.CLEAR_TO_NULL)
    // @Desensitized(type = DesensitizedType.CLEAR_TO_EMPTY)
    private String sex;
    @DesensitizedString(type = DesensitizedType.ID_CARD)
    // @DesensitizedString(type = DesensitizedType.ID_CARD, idCardNumFront = 1, idCardNumBack = 2)
    private String idCard;
    @DesensitizedString(type = DesensitizedType.FIXED_PHONE)
    private String fixedPhone;
    @DesensitizedString(type = DesensitizedType.MOBILE_PHONE)
    private String mobilePhone;
    @DesensitizedString(type = DesensitizedType.EMAIL)
    private String email;
    @DesensitizedString(type = DesensitizedType.ADDRESS)
    // @DesensitizedString(type = DesensitizedType.ADDRESS, addressSensitiveSize = 8)
    private String address;
    @DesensitizedString(type = DesensitizedType.PASSWORD)
    private String password;
    @DesensitizedString(type = DesensitizedType.CAR_LICENSE)
    private String carLicense;
    @DesensitizedString(type = DesensitizedType.BANK_CARD)
    private String bankCard;
    @DesensitizedString(type = DesensitizedType.IPV4)
    private String ipv4;
    @DesensitizedString(type = DesensitizedType.IPV6)
    private String ipv6;
    @DesensitizedString(type = DesensitizedType.FIRST_MASK)
    private String firstMask;
    @DesensitizedString(type = DesensitizedType.HIDE)
    // @DesensitizedString(type = DesensitizedType.HIDE, hideStartInclude = 2, hideEndExclude = 4)
    private String hideStartEnd;
    @DesensitizedString(type = DesensitizedType.WORD)
    // @DesensitizedString(type = DesensitizedType.WORD, wordBlackPath = "word_blacklist.txt", wordWhitePath = "word_whitelist.txt", wordFilterType = "*")
    private String wordContent;
}
