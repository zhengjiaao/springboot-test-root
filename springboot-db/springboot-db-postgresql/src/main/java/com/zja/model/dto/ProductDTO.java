package com.zja.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product 数据传输
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Data
@ApiModel("ProductDTO")
public class ProductDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品价格")
    private Double price;

    @ApiModelProperty("产品描述")
    private String description;

    @ApiModelProperty("产品分类")
    private String category;

    @ApiModelProperty("产品状态(0-禁用,1-启用)")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后一次修改时间")
    private LocalDateTime lastModifiedDate;
}