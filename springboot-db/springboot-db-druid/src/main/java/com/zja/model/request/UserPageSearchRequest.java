/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-07 17:30
 * @Since:
 */
package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhengja
 * @since: 2023/08/07 17:30
 */
@Getter
@Setter
@ApiModel("User 分页搜索参数")
public class UserPageSearchRequest extends BasePageRequest {
    @ApiModelProperty("用户名")
    private String username;
}