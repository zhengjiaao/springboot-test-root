package com.zja.obfuscated.allatori.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:32
 */
@Getter
@Setter
@ApiModel("User 分页搜索参数")
public class UserPageSearchRequest extends BasePageRequest {
    @ApiModelProperty("")
    private String name;

}