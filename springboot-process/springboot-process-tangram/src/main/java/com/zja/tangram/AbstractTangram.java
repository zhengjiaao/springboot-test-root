/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-23 17:24
 * @Since:
 */
package com.zja.tangram;

import com.zja.tangram.model.ProcessTaskRequest;
import com.zja.tangram.model.ResponseResult;
import com.zja.tangram.model.TaskRuModelVO;
import com.zja.tangram.model.request.ProjectAddRequest;
import com.zja.tangram.model.vo.ProjectVO;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Tangram 通用操作 抽象类
 */
public abstract class AbstractTangram implements TangramService {

    //项目
    protected ProjectService projectService;
    //流程 任务
    protected ProcessService processService;
    //流程id
    protected String processId = null;

    /**
     * 创建项目
     * @param token
     * @param projectAddRequest
     * @return
     */
    protected ResponseResult<ProjectVO> createProject(String token, ProjectAddRequest projectAddRequest) {
        if (StringUtils.isEmpty(processId)) {
            throw new RuntimeException("processId not is null!");
        }
        projectAddRequest.setProcessId(processId);
        return projectService.createProject(token, projectAddRequest);
    }

    /**
     * 删除项目-逻辑删除
     * @param token
     * @param projectIds
     * @return
     */
    protected ResponseResult<Boolean> deleteProjects(String token, List<String> projectIds) {
        return projectService.deleteProjects(token, projectIds);
    }

    /**
     * 流程推进
     * @param token
     * @param request
     * @return
     */
    protected ResponseResult<Boolean> processTask(String token, ProcessTaskRequest request) {
        return processService.processTask(token, request);
    }

    /**
     * 运行时任务元数据列表
     * @param token
     * @param procInstId
     * @return
     */
    protected ResponseResult<List<TaskRuModelVO>> processRuntimeTaskMetasDataList(String token, String procInstId) {
        return processService.processRuntimeTaskMetasDataList(token, procInstId);
    }
}
