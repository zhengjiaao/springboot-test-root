package com.zja.tangram.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 *任务流向的表达式
 */
@Accessors(chain = true)
@Data
public class FlowExpressionVO {

    protected String expression;
    @ApiModelProperty("流动作（通过、撤回、退回等）")
    protected ActionTypeEnum actionType;
    @ApiModelProperty("除动作之外的条件")
    protected String condition;
    @ApiModelProperty("需要传入的参数")
    protected Map<String, Object> parameter;

}
