package com.zja.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文档数据类
 *
 * @author: zhengja
 * @since: 2019/6/19 10:00
 */
@ApiModel(value = "文档数据")
@Data
public class ShopDTO {

    @ApiModelProperty(value = "文档id")
    private Integer id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;

    public ShopDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShopDTO(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
