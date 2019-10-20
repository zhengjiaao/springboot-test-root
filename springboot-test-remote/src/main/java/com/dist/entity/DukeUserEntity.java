package com.dist.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:03
 * @Author: Mr.Zheng
 * @Description:
 */
@Data
public class DukeUserEntity implements Serializable {
    private Long id;
    private String guid;
    private String name;
    private String age;
    private Date createTime;
}
