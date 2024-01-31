package com.zja.obfuscated.proguard.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:31
 */
@ApiModel("BasePageRequest")
public class BasePageRequest implements Serializable {
    @ApiModelProperty("页码 从第1页开始")
    private Integer page = 1;
    @ApiModelProperty("每页数量 默认 10")
    private Integer size = 10;

    public Integer getPage() {
        return page - 1; // jpa默认第一页是从0开始
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