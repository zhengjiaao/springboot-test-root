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

import java.util.ArrayList;
import java.util.List;

/**
 * 运行时获取的任务数据，包含任务信息，分配信息以及可执行操作
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRuModelVOBack extends TaskModelVO {
    @ApiModelProperty("可执行的动作列表")
    protected List<FlowActionVO> actions = new ArrayList<>();

    @ApiModelProperty("抄送")
    protected boolean carbonCopy = false;

    @ApiModelProperty("抄送")
    protected List<CarbonCopyUserVO> carbonCopyUsers = new ArrayList<>();

    @ApiModelProperty("抄送")
    protected List<String> carbonCopyRoles = new ArrayList<>();
}
