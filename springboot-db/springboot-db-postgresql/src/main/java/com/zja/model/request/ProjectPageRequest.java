package com.zja.model.request;

import com.zja.model.base.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页请求参数 分页参数
 *
 * @author: zhengja
 * @since: 2024/09/27 9:34
 */
@Getter
@Setter
@ApiModel("Project 分页参数")
public class ProjectPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

}