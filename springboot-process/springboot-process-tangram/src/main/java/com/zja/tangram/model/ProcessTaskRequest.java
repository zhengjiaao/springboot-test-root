/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-20 16:12
 * @Since:
 */
package com.zja.tangram.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("流程推进 参数")
public class ProcessTaskRequest implements Serializable {
    @ApiModelProperty("任务实例Id")
    @NotNull
    private String taskId;
    private boolean a = false;
    /**
     * @see ActionTypeEnum#getKey()
     */
    @ApiModelProperty("任务动作")
    @NotNull
    private String op;

//    @ApiModelProperty("变量映射键值对")
//    private Map<String, Object> variables;

    @ApiModelProperty("变量映射键值对")
    private JSONObject variables;
}
