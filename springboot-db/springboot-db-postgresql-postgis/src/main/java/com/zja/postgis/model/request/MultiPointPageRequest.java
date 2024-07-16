package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 点集合 分页参数
 * @author: zhengja
 * @since: 2024/07/15 15:09
 */
@Getter
@Setter
@ApiModel("MultiPoint 分页参数")
public class MultiPointPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}