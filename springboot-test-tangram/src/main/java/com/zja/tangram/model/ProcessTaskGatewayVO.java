/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:50
 * @Since:
 */
package com.zja.tangram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 处理任务的网关
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessTaskGatewayVO extends FlowExpressionVO {
    private String name;
    private String description;
    private AssigneeExpressionVO nextAssignee;
    private String targetDefId;
}
