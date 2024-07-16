package com.zja.postgis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.io.Serializable;

/**
 * Point 数据传输
 * @author: zhengja
 * @since: 2024/07/15 13:52
 */
@Data
@ApiModel("PointDTO")
public class PointDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点")
    private Point point;
}