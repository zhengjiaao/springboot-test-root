package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;

/**
 * 几何对象 更新参数
 * @author: zhengja
 * @since: 2024/07/15 14:31
 */
@Data
@ApiModel("GeometryUpdateRequest 更新 几何对象信息")
public class GeometryUpdateRequest implements Serializable {
    @ApiModelProperty("几何对象名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("几何对象")
    private Geometry geometry;
}