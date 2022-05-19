/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:14
 * @Since:
 */
package com.zja.tangram;

import com.zja.config.OpenfeignConfig;
import com.zja.tangram.model.ResponseResult;
import com.zja.tangram.model.request.ProjectAddRequest;
import com.zja.tangram.model.vo.ProjectVO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * 七巧板 项目服务
 * 参考：{@link OpenfeignConfig#projectService()}
 */
public interface ProjectService {

    @ApiOperation("创建项目")
    @RequestLine("POST v1/projects")
    @Headers({"dist-token:{token}", "Content-Type: application/json", "Accept: application/json"})
    ResponseResult<ProjectVO> createProject(@Param(value = "token") String token, ProjectAddRequest projectAddRequest);

    //未测试通过，DELETE 传 body未成功
    @ApiOperation("删除项目")
    @RequestLine("DELETE v1/projects")
    @Headers({"dist-token:{token}", "Content-Type: application/json", "Accept: application/json"})
    ResponseResult<Boolean> deleteProjects(@Param(value = "token") String token, List<String> projectIds);
}
