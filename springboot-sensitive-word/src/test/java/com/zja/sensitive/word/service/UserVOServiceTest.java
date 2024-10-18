package com.zja.sensitive.word.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-10 13:46
 */
@SpringBootTest
public class UserVOServiceTest {

    @Autowired
    UserService service;

    @Test
    public void test() {
        UserVO userVO = service.getUser();
        UserVO userVO2 = service.getUserInfo();
        List<UserVO> userVOList = service.getUserList();
        PageData userPage = service.getUserPage();

        System.out.println(userVO); // 单个对象，原始数据
        System.out.println(userVO2); // 单个对象，处理后数据
        System.out.println(userVOList); // 列表，处理后数据
        System.out.println(userPage); // 分页结果集，处理后数据
    }
}
