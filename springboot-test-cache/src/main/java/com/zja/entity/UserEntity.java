package com.zja.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/15 10:34
 */
@Data
public class UserEntity implements Serializable{
    private Integer id;
    private String name;
    private Integer age;
}
