package com.zja.sensitive.word.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-10 13:19
 */
@Service
public class UserService {

    public UserVO getUser() {
        //  以下为虚构数据
        return UserVO.builder()
                .userId("1k244j4230012")
                .name("李四")
                .password("123456")
                .address("上海市-浦东新区-张江镇-华夏中路-25890弄")
                .email("123456@qq.com")
                .fixedPhone("021-23456789")
                .mobilePhone("15923453317")
                .sex("男")
                .carLicense("沪B69999")
                .ipv4("192.168.2.110")
                .ipv6("fe80::c725:53ec:f036:c802%7")
                .bankCard("6230520360008692770")
                .idCard("412829199602036235")
                .firstMask("666666")
                .hideStartEnd("123456789")
                .wordContent("敏感词：这是一个傻子，真傻呀。")
                .build();
    }

    @Desensitization
    public UserVO getUserInfo() {
        return getUser();
    }

    @Desensitization
    public List<UserVO> getUserList() {
        return Collections.singletonList(getUser());
    }

    @Desensitization
    public PageData getUserPage() {
        return PageData.of(Collections.singletonList(getUser()), 1, 1, 1);
    }

}
