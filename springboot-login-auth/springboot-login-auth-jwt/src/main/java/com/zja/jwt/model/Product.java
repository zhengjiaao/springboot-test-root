package com.zja.jwt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体
 *
 * @Author: zhengja
 * @Date: 2025-07-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("产品")
public class Product implements Serializable {

    @ApiModelProperty("产品ID")
    private Long id;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("分类")
    private String category;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("状态: ON_SALE-在售, OFF_SHELF-下架")
    private String status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
