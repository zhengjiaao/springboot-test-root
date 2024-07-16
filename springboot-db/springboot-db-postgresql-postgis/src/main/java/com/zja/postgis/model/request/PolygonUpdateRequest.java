package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Polygon;

import java.io.Serializable;

/**
 * 多边形 更新参数
 * @author: zhengja
 * @since: 2024/07/15 13:53
 */
@Data
@ApiModel("PolygonUpdateRequest 更新 多边形信息")
public class PolygonUpdateRequest implements Serializable {
    @ApiModelProperty("多边形名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("多边形(面)")
    private Polygon polygon;
}