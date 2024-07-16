package com.zja.postgis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.MultiPolygon;

import java.io.Serializable;

/**
 * MultiPolygon 数据传输
 * @author: zhengja
 * @since: 2024/07/15 15:20
 */
@Data
@ApiModel("MultiPolygonDTO")
public class MultiPolygonDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点集合")
    private MultiPolygon multiPolygon;
}