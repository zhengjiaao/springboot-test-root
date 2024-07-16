package com.zja.postgis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import java.io.Serializable;

/**
 * Geometry 数据传输
 * @author: zhengja
 * @since: 2024/07/15 14:31
 */
@Data
@ApiModel("GeometryDTO")
public class GeometryDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("几何对象")
    private Geometry geometry;
}