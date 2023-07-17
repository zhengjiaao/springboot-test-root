/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 14:05
 * @Since:
 */
package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2023/07/17 14:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("UserRequest")
public class UserRequest implements Serializable {
    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("性别")
    private String sex;
}