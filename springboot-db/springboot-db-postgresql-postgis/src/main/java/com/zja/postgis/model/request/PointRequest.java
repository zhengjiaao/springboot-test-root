package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

/**
 *  请求参数
 * @author: zhengja
 * @since: 2024/07/15 13:52
 */
@Data
@ApiModel("PointRequest 新增 或 更新 点信息")
public class PointRequest implements Serializable {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点")
    private Point point;
}