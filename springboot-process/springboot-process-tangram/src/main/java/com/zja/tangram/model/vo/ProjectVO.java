package com.zja.tangram.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectVO implements Serializable {
    @ApiModelProperty("行政区名称")
    private String regionName;
    @ApiModelProperty("请示文号")
    private String requestNumber;
    @ApiModelProperty("项目名称")
    private String name;
    @ApiModelProperty("申请单位")
    private String applyUnit;
    @ApiModelProperty("创建时间")
    private Long createTime;
    @ApiModelProperty("运行时任务名称")
    private String taskName;
    @ApiModelProperty("项目id")
    private String id;
    @ApiModelProperty("项目Number")
    private String number;
    @ApiModelProperty("业务id")
    private String businessId;
    @ApiModelProperty("业务名称")
    private String businessName;
    @ApiModelProperty("流程id")
    private String processId;
    @ApiModelProperty("流程名称")
    private String processName;
    @ApiModelProperty("流程版本id")
    private String processVersionId;
    @ApiModelProperty("流程实例id")
    private String processInsId;
    @ApiModelProperty("备注")
    private String remake;
    @ApiModelProperty("运行时任务id")
    private String taskId;
    @ApiModelProperty("历史任务名称")
    private String hiTaskName;
    @ApiModelProperty("历史任务id")
    private String hiTaskId;
    @ApiModelProperty("历史任务定义id")
    private String hiTaskDefkey;
    @ApiModelProperty("办结时间")
    private String finishTime;
    @ApiModelProperty("已读状态")
    private Integer readStatus;
}
