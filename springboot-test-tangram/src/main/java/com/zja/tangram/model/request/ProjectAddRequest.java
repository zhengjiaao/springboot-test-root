/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:17
 * @Since:
 */
package com.zja.tangram.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 项目添加VO
 */
@Data
public class ProjectAddRequest implements Serializable {
    @ApiModelProperty("流程id")
    @NotBlank
    private String processId;
    @ApiModelProperty("名称")
    @NotBlank
    private String name;
    @ApiModelProperty("所属行政区名称")
    private String regionName;
    @ApiModelProperty("描述")
    private String remark;
}
