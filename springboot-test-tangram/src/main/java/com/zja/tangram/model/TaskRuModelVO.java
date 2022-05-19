/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-20 17:52
 * @Since:
 */
package com.zja.tangram.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 运行时获取的任务数据，包含任务信息，分配信息以及可执行操作
 */
@Data
public class TaskRuModelVO implements Serializable {
    @ApiModelProperty("任务id")
    protected String id;
    @ApiModelProperty("任务名")
    protected String name;

    @ApiModelProperty("流程实例id")
    protected String processInstanceId;

    @ApiModelProperty("候选组")
    protected List<String> candidateGroups;

    @ApiModelProperty("可执行的动作列表")
    protected List<FlowActionVO> actions;

    @ApiModelProperty("抄送")
    protected boolean carbonCopy = false;

    @ApiModelProperty("抄送")
    protected List<CarbonCopyUserVO> carbonCopyUsers;

    @ApiModelProperty("抄送")
    protected List<String> carbonCopyRoles;
}
