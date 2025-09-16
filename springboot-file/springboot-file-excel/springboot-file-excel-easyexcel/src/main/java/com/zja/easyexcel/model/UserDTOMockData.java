package com.zja.easyexcel.model;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-09-15 20:44
 */
public class UserDTOMockData {

    // 模拟数据-单个
    public static UserDTO getUserDTO() {
        return UserDTO.builder()
                .userId("001")
                .userName("张三")
                .userPhone("13800138001")
                .userBirthday("1990-01-01")
                .userAddress("北京市朝阳区xxx街")
                .userEmail("zhangsan@example.com")
                .userSex("男")
                .userStatus("正常")
                .build();
    }

    // 模拟数据-列表
    public static List<UserDTO> getUserDTOList() {
        return Arrays.asList(
                UserDTO.builder()
                        .userId("001")
                        .userName("张三")
                        .userPhone("13800138001")
                        .userBirthday("1990-01-01")
                        .userAddress("北京市朝阳区xxx街道")
                        .userEmail("zhangsan@example.com")
                        .userSex("男")
                        .userStatus("正常")
                        .build(),
                UserDTO.builder()
                        .userId("002")
                        .userName("李四")
                        .userPhone("13800138002")
                        .userBirthday("1992-05-15")
                        .userAddress("上海市浦东新区xxx路")
                        .userEmail("lisi@example.com")
                        .userSex("女")
                        .userStatus("异常")
                        .build(),
                UserDTO.builder()
                        .userId("003")
                        .userName("王五")
                        .userPhone("13800138003")
                        .userBirthday("1988-12-20")
                        .userAddress("广州市天河区xxx大道")
                        .userEmail("wangwu@example.com")
                        .userSex("男")
                        .userStatus("正常")
                        .build(),
                UserDTO.builder()
                        .userId("004")
                        .userName("赵六")
                        .userPhone("13800138004")
                        .userBirthday("1995-08-10")
                        .userAddress("深圳市南山区xxx社区")
                        .userEmail("zhaoliu@example.com")
                        .userSex("女")
                        .userStatus("异常")
                        .build(),
                UserDTO.builder()
                        .userId("005")
                        .userName("钱七")
                        .userPhone("13800138005")
                        .userBirthday("1993-03-25")
                        .userAddress("杭州市西湖区xxx园区")
                        .userEmail("qianqi@example.com")
                        .userSex("男")
                        .userStatus("正常")
                        .build()
        );
    }

}
