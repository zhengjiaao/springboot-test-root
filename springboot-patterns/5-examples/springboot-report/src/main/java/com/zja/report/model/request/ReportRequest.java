package com.zja.report.model.request;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2024-11-05 10:00
 */
@Data
@ApiModel("ReportRequest 报告参数")
public class ReportRequest implements Serializable {

    @ApiModelProperty("模板编码：模版的唯一编码")
    @Range(message = "模版编码范围为 {min} 到 {max} 之间", min = 1, max = 100000)
    private Integer templateCode;

    @ApiModelProperty("业务唯一标识（可选的，例如：业务实体id、项目id等）")
    private String businessId;

    @ApiModelProperty("业务扩展参数（可选的，生成报告所需的数据）")
    private JSONObject businessJsonData;
}