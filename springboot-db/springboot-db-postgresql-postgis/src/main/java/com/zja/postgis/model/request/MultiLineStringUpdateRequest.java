package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;

import java.io.Serializable;

/**
 * 线集合 更新参数
 * @author: zhengja
 * @since: 2024/07/15 15:17
 */
@Data
@ApiModel("MultiLineStringUpdateRequest 更新 线集合信息")
public class MultiLineStringUpdateRequest implements Serializable {
    @ApiModelProperty("线集合名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点集合")
    private MultiLineString multiLineString;
}