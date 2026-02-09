package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 产品 请求参数
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Data
@ApiModel("ProductRequest 新增 或 更新 产品信息")
public class ProductRequest implements Serializable {

    @ApiModelProperty(value = "产品名称", required = true)
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    @ApiModelProperty(value = "产品编码", required = true)
    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    @ApiModelProperty("产品价格")
    private Double price;

    @ApiModelProperty("产品描述")
    private String description;

    @ApiModelProperty("产品分类")
    private String category;

    @ApiModelProperty("产品状态(0-禁用,1-启用)")
    private Integer status;
}