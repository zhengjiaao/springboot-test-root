/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 20:09
 * @Since:
 */
package com.zja.controller;

import com.alibaba.fastjson.JSONObject;
import com.zja.tangram.ProcessService;
import com.zja.tangram.ProjectService;
import com.zja.tangram.model.FlowActionVO;
import com.zja.tangram.model.ProcessTaskRequest;
import com.zja.tangram.model.ResponseResult;
import com.zja.tangram.model.TaskRuModelVO;
import com.zja.tangram.model.request.ProjectAddRequest;
import com.zja.tangram.model.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/rest/tangram")
public class TangramProcessClientController {

    @Autowired
    ProjectService projectService;

    @Autowired
    ProcessService processService;

    @Value("${tangram.gwzx.process_id}")
    public String tangramProcessId;

    @GetMapping("/create/project")
    public ResponseResult<ProjectVO> createProject(@RequestParam String token) {
        ProjectAddRequest projectAddRequest = new ProjectAddRequest();
        projectAddRequest.setProcessId(tangramProcessId);
        return projectService.createProject(token, projectAddRequest);
    }

    @DeleteMapping("/delete/project")
    public Object deleteProjects(@RequestParam String token, @RequestParam List<String> projectIds) {
        return projectService.deleteProjects(token, projectIds);
    }

    @GetMapping("/get/process/task")
    public Object processRuntimeTaskMetasDataList(@RequestParam String token, @RequestParam String procInsId) {
        return processService.processRuntimeTaskMetasDataList(token, procInsId);
    }


    @GetMapping("/process/task")
    public Object processTask(@RequestParam String token, @RequestParam String procInsId, @RequestParam String op) {
        ResponseResult<List<TaskRuModelVO>> responseResult = processService.processRuntimeTaskMetasDataList(token, procInsId);
        System.out.println(responseResult);
        if (ObjectUtils.isEmpty(responseResult)) {
            return false;
        }
        List<TaskRuModelVO> data = responseResult.getData();
        if (ObjectUtils.isEmpty(data)) {
            return false;
        }
        if (ObjectUtils.isEmpty(data.get(0))) {
            return "未获取的节点数据";
        }
        TaskRuModelVO taskRuModelVO = data.get(0);
        System.out.println("taskRuModelVO=" + taskRuModelVO);

        //获取流程可操作动作
        List<FlowActionVO> actions = taskRuModelVO.getActions();
        List<Map<String, Object>> param_cpl = new ArrayList<>();
        Map<String, Object> param = null;
        if (!CollectionUtils.isEmpty(actions)) {
            for (FlowActionVO action : actions) {
                JSONObject jsonObject = new JSONObject(action.getParameter());
                if (jsonObject == null || jsonObject.isEmpty() || "null".equals(jsonObject)) {
                    System.out.println("没有找到额外参数");
                } else {
                    System.out.println(action.getParameter());
                }
            }
            System.out.println();
            List<Map<String, Object>> params = actions.stream().map(e -> !e.getParameter().equals("{}") ? e.getParameter() : null).collect(Collectors.toList());
            System.out.println("params=" + params);
            if (!CollectionUtils.isEmpty(params)) {
                //过滤出额外参数
                param_cpl = params.stream().filter(e -> e.get("param_cpl").equals("true")).collect(Collectors.toList());
                if (!"{}".equals(param_cpl)) {
                    param = param_cpl.get(0);
                    System.out.println("param=" + param);
                }
            }
        }

        String taskId = taskRuModelVO.getId();
        log.info("推进参数 token:{},op:{},taskId:{},", token, op, taskId);

        ProcessTaskRequest request = new ProcessTaskRequest();
        request.setTaskId(taskId);
        request.setOp(op);
        request.setVariables(param != null ? new JSONObject(param) : new JSONObject());

        //流程推进
        ResponseResult<Boolean> objectResponseResult = processService.processTask(token, request);
        return objectResponseResult;
    }

    @GetMapping("/process/task/v2")
    public Object processTaskv2(@RequestParam String token, @RequestParam String procInsId, @RequestParam String op, @RequestParam(required = false) String parameter) {
        ResponseResult<List<TaskRuModelVO>> responseResult = processService.processRuntimeTaskMetasDataList(token, procInsId);
        System.out.println("responseResult=" + responseResult);
        if (ObjectUtils.isEmpty(responseResult.getData())) {
            return "任务已执行完毕！";
        }
        TaskRuModelVO taskRuModelVO = responseResult.getData().get(0);
        System.out.println("taskRuModelVO=" + taskRuModelVO);
        System.out.println("TaskId=" + taskRuModelVO.getId());

        ProcessTaskRequest request = new ProcessTaskRequest();
        request.setTaskId(taskRuModelVO.getId());
        request.setOp(op);
        Map<String, Object> params = new HashMap<>();
        //注意，额外参数必须是 param_ 前缀
        if (StringUtils.hasLength(parameter)) {
            params.put("param_close", parameter);
        }
        request.setVariables(!params.isEmpty() ? new JSONObject(params) : new JSONObject());

        //流程推进
        ResponseResult<Boolean> objectResponseResult = processService.processTask(token, request);
        return objectResponseResult;
    }

    /*@PostMapping("/query/doing")
    @ApiOperation("查询待办箱")
    public ResponseData listDoingBox(@RequestParam int pageIndex,
                                     @RequestParam int pageSize,
                                     @RequestParam(required = false) @ApiParam(value = "流程阶段") String stage) {
        return ResponseUtil.success(pcoService.listDoingBox(pageIndex, pageSize, stage));
    }

    @PostMapping("/query/done")
    @ApiOperation("查询已办箱")
    public ResponseData listDoneBox(@RequestParam int pageIndex,
                                    @RequestParam int pageSize,
                                    @RequestParam(required = false) @ApiParam(value = "流程阶段") String stage) {
        return ResponseUtil.success(pcoService.listDoneBox(pageIndex, pageSize, stage));
    }

    @PostMapping("/query/finish")
    @ApiOperation("查询办结箱")
    public ResponseData listFinishBox(@RequestParam int pageIndex,
                                      @RequestParam int pageSize) {
        return ResponseUtil.success(pcoService.listFinishBox(pageIndex, pageSize));
    }*/
}
