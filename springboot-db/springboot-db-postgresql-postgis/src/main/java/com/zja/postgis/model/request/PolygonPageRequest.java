package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 多边形 分页参数
 * @author: zhengja
 * @since: 2024/07/15 13:53
 */
@Getter
@Setter
@ApiModel("Polygon 分页参数")
public class PolygonPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}