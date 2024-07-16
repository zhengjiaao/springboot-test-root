package com.zja.postgis.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *  分页参数
 * @author: zhengja
 * @since: 2024/07/15 13:52
 */
@Getter
@Setter
@ApiModel("Point 点分页参数")
public class PointPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}