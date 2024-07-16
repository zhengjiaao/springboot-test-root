package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

/**
 * 点 更新参数
 * @author: zhengja
 * @since: 2024/07/15 13:52
 */
@Data
@ApiModel("PointUpdateRequest 更新 点信息")
public class PointUpdateRequest implements Serializable {
    @ApiModelProperty("点名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点")
    private Point point;
}