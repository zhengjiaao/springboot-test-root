/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:45
 * @Since:
 */
package com.zja.tangram.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 任务运行时任务可选操作
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class FlowActionVO implements Serializable {
    @ApiModelProperty("flow名称")
    private String name;
    @ApiModelProperty("action名称")
    private String action;
    @ApiModelProperty("动作类型")
    private ActionTypeEnum actionType;
    @ApiModelProperty("参数")
    private Map<String, Object> parameter;
    @ApiModelProperty("网关")
    private List<ProcessTaskGatewayVO> gateways;
    @ApiModelProperty("下一个任务的指派信息")
    private AssigneeExpressionVO nextAssignee;
    @ApiModelProperty("目标任务定义id")
    private String targetDefId;
}
