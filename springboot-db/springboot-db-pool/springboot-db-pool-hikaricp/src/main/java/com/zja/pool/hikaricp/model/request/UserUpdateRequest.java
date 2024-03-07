/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-07 17:30
 * @Since:
 */
package com.zja.pool.hikaricp.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2023/08/07 17:30
 */
@Data
@ApiModel("UserUpdateRequest")
public class UserUpdateRequest implements Serializable {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("年龄")
    private int age;
}