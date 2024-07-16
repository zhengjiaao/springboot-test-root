package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 线集合 分页参数
 * @author: zhengja
 * @since: 2024/07/15 15:18
 */
@Getter
@Setter
@ApiModel("MultiLineString 分页参数")
public class MultiLineStringPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}