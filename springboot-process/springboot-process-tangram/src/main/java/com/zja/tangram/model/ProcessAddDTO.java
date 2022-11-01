/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:39
 * @Since:
 */
package com.zja.tangram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 创建流程 VO
 */
@Data
public class ProcessAddDTO {
    @ApiModelProperty("名称")
    @NotBlank
    private String name;
    @JsonIgnore
    @ApiModelProperty("唯一key")
    private String key;
    @NotBlank
    @ApiModelProperty("业务id")
    private String businessId;
    @ApiModelProperty("描述")
    private String remark;
}
