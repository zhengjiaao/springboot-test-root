package com.zja.mvc.response.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:23
 */
@Getter
@Setter
@ApiModel("User 分页搜索参数")
public class UserPageSearchRequest extends BasePageRequest {
    @ApiModelProperty("唯一标识ID")
    private String id;

    @ApiModelProperty("名称")
    private String name;
}