package com.zja.model.request;

import com.zja.model.base.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 产品 分页参数
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Getter
@Setter
@ApiModel("ProductPageRequest")
public class ProductPageRequest extends BasePageRequest {
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品分类")
    private String category;

    @ApiModelProperty("产品状态(0-禁用,1-启用)")
    private Integer status;
}