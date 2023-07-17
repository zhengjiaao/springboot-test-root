/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 13:53
 * @Since:
 */
package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2023/07/17 13:53
 */
@Data
@ApiModel("RoleUpdateRequest")
public class RoleUpdateRequest implements Serializable {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String remarks;
}