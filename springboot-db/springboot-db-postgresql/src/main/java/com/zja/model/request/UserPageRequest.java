package com.zja.model.request;

import com.zja.model.base.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户 分页参数
 *
 * @author: zhengja
 * @since: 2025/03/05 14:01
 */
@Getter
@Setter
@ApiModel("User 分页参数")
public class UserPageRequest extends BasePageRequest {
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户年龄")
    private Integer age;
}