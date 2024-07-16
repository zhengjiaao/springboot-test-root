package com.zja.postgis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;

import java.io.Serializable;

/**
 * MultiPoint 数据传输
 * @author: zhengja
 * @since: 2024/07/15 15:09
 */
@Data
@ApiModel("MultiPointDTO")
public class MultiPointDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点集合")
    private MultiPoint multiPoint;
}