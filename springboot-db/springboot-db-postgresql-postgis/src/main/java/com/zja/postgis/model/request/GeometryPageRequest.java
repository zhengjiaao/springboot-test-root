package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 几何对象 分页参数
 * @author: zhengja
 * @since: 2024/07/15 14:31
 */
@Getter
@Setter
@ApiModel("Geometry 分页参数")
public class GeometryPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}