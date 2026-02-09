package com.zja.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页请求（通用的）
 *
 * @author: zhengja
 * @since: 2024/09/27 9:31
 */
@Setter
@Getter
@ApiModel("BasePageRequest")
public class BasePageRequest implements Serializable {
    @ApiModelProperty("页码 从第1页开始")
    private Integer page = 1;
    @ApiModelProperty("每页数量 默认 10")
    private Integer size = 10;

    public Integer getPage() {
        return page - 1; // 注：jpa page 从 0 开始
    }
}