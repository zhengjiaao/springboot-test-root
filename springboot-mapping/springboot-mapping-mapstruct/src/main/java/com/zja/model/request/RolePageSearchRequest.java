/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 13:31
 * @Since:
 */
package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhengja
 * @since: 2023/07/17 13:31
 */
@Getter
@Setter
@ApiModel("Role 分页搜索参数")
public class RolePageSearchRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

}