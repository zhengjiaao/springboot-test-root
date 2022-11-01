package com.zja.tangram.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 指派人的表达式
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeExpressionVO {
    @ApiModelProperty("表达式")
    private String expression;
    @ApiModelProperty("指派人变量Key")
    private String assigneeKey;
    @ApiModelProperty("候选人角色")
    private List<String> candidateRole;
    @ApiModelProperty("已经指派人")
    private String assignee;
}
