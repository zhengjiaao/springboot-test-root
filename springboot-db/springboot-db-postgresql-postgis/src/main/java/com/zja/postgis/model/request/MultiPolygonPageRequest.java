package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 多边形集合 分页参数
 * @author: zhengja
 * @since: 2024/07/15 15:19
 */
@Getter
@Setter
@ApiModel("MultiPolygon 分页参数")
public class MultiPolygonPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}