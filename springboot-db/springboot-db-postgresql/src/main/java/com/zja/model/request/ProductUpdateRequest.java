package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品 更新请求参数
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Data
@ApiModel("ProductUpdateRequest 更新 产品信息")
public class ProductUpdateRequest implements Serializable {

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品价格")
    private Double price;

    @ApiModelProperty("产品描述")
    private String description;

    @ApiModelProperty("产品分类")
    private String category;

    @ApiModelProperty("产品状态(0-禁用,1-启用)")
    private Integer status;
}