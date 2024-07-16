package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.MultiPoint;

import java.io.Serializable;

/**
 * 点集合 更新参数
 * @author: zhengja
 * @since: 2024/07/15 15:09
 */
@Data
@ApiModel("MultiPointUpdateRequest 更新 点集合信息")
public class MultiPointUpdateRequest implements Serializable {
    @ApiModelProperty("点集合名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("点集合")
    private MultiPoint multiPoint;
}