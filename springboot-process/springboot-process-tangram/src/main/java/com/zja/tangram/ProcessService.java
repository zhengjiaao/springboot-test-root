/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:27
 * @Since:
 */
package com.zja.tangram;

import com.zja.config.OpenfeignConfig;
import com.zja.tangram.model.ProcessTaskRequest;
import com.zja.tangram.model.ResponseResult;
import com.zja.tangram.model.TaskRuModelVO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * 七巧板 流程服务
 */
public interface ProcessService {

    @ApiOperation(value = "流程环节推进", notes = "要先获取当前可执行taskId")
    @RequestLine("POST v1/process/task")
    @Headers({"dist-token:{token}", "Content-Type: application/json", "Accept: application/json"})
    ResponseResult<Boolean> processTask(@Param(value = "token") String token, ProcessTaskRequest request);

    @ApiOperation(value = "获取流程实例当前环节元数据(运行时)", notes = "获取可执行的下一个环节列表")
    @RequestLine("GET v1/process/{procInstId}/task/meta/runtime")
    @Headers({"dist-token:{token}"})
    ResponseResult<List<TaskRuModelVO>> processRuntimeTaskMetasDataList(@Param(value = "token") String token,
                                                                        @ApiParam("流程实例Id") @Param(value = "procInstId") String procInstId);
}
