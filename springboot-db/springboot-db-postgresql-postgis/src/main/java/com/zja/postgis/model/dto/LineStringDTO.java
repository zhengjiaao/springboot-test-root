package com.zja.postgis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

/**
 * LineString 数据传输
 * @author: zhengja
 * @since: 2024/07/15 13:44
 */
@Data
@ApiModel("LineStringDTO")
public class LineStringDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("线")
    private LineString lineString;
}