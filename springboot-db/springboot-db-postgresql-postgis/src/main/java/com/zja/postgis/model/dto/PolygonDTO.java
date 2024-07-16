package com.zja.postgis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Polygon 数据传输
 * @author: zhengja
 * @since: 2024/07/15 13:53
 */
@Data
@ApiModel("PolygonDTO")
public class PolygonDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("多边形(面)")
    private Polygon polygon;
}