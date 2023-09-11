package com.zja.dto.arcgis;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 14:30
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：字段
 */
@Data
@ApiModel("字段信息")
public class FieldsDTO implements Serializable {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("别名")
    private String alias;
    @ApiModelProperty("长度")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer length;
}
