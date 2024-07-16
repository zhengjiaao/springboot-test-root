package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 线 分页参数
 * @author: zhengja
 * @since: 2024/07/15 13:48
 */
@Getter
@Setter
@ApiModel("LineString 分页参数")
public class LineStringPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}