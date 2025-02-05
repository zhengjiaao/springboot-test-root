package com.zja.mvc.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.validation.constraints.Size;

/**
 * 分页请求（通用的）
 *
 * @author: zhengja
 * @since: 2025/02/05 13:24
 */
@Setter
@ApiModel("BasePageRequest")
public class BasePageRequest implements Serializable {
    @ApiModelProperty("页码 从第1页开始")
    @Size(min = 1, max = 100000000)
    private Integer page = 1;
    @Getter
    @ApiModelProperty("每页数量 默认 10")
    @Size(min = 1, max = 100000000)
    private Integer size = 10;

    public Integer getPage() {
        return page - 1; // 注：jpa page 从 0 开始
    }
}