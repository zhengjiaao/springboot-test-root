package com.zja.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/19 10:00
 */
//@Document(collection = "shops")
@Data
public class ShopDTO implements Serializable {
    //id 可以是 Integer 类型
    private String id;
    private String name;
    private Integer age;

    public ShopDTO(String id, String name,Integer age) {
        this.id = id;
        this.name = name;
        this.age=age;
    }
}
