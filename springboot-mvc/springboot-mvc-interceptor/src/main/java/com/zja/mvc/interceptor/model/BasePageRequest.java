package com.zja.mvc.interceptor.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:21
 */
@ApiModel("BasePageRequest")
public class BasePageRequest implements Serializable {
    @ApiModelProperty("页码 从第1页开始")
    private Integer page = 1;
    @ApiModelProperty("每页数量 默认 10")
    private Integer size = 10;

    public Integer getPage() {
        // jpa page 从 0 开始
        return page - 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}