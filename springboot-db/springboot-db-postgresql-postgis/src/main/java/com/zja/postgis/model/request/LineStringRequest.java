package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.LineString;

import java.io.Serializable;

/**
 * 线 请求参数
 * @author: zhengja
 * @since: 2024/07/15 13:43
 */
@Data
@ApiModel("LineStringRequest 新增 或 更新 线信息")
public class LineStringRequest implements Serializable {

    @ApiModelProperty("线名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("线")
    private LineString lineString;
}