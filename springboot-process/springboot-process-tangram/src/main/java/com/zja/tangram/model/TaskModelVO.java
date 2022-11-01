/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:44
 * @Since:
 */
package com.zja.tangram.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * flowable环节实例
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskModelVO implements Serializable {
    @ApiModelProperty("任务id")
    protected String id;
    @ApiModelProperty("任务名")
    protected String name;
    @ApiModelProperty("描述")
    protected String description;
    @ApiModelProperty("权重")
    protected int priority;
    @ApiModelProperty("拥有者")
    protected String owner;
    @ApiModelProperty("指派人")
    protected String assignee;
    @ApiModelProperty("流程实例id")
    protected String processInstanceId;
    @ApiModelProperty("execute id")
    protected String executionId;
    @ApiModelProperty("任务定义id")
    protected String taskDefinitionId;
    @ApiModelProperty("流程定义id")
    protected String processDefinitionId;
    protected String scopeId;
    protected String subScopeId;
    protected String scopeType;
    protected String scopeDefinitionId;
    @ApiModelProperty("委派状态")
    protected String delegationState;
    protected String sropagatedStageInstanceId;
    protected Date createTime;
    @ApiModelProperty("任务定义key")
    protected String taskDefinitionKey;
    protected Date dueDate;
    protected String category;
    @ApiModelProperty("父任务id")
    protected String parentTaskId;
    protected String tenantId;
    protected String formKey;
    @ApiModelProperty("任务本地变量")
    protected Map<String, Object> taskLocalVariables;
    @ApiModelProperty("流程变量")
    protected Map<String, Object> processVariables;
    protected Date claimTime;
    @ApiModelProperty("候选用户")
    protected List<String> candidateUsers;
    @ApiModelProperty("候选组")
    protected List<String> candidateGroups;
    @ApiModelProperty("历史任务id")
    protected String hiTaskId;
    @ApiModelProperty("历史任务名称")
    protected String hiTaskName;
}
