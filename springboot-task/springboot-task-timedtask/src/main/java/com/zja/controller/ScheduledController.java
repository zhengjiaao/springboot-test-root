package com.zja.controller;

import com.zja.entity.TaskTimeEntity;
import com.zja.service.ScheduledTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**Scheduled定时任务 功能有: 初始化任务列表、所有任务列表、启动任务、重启任务、停止任务
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 11:36
 */
@Api(tags = {"ScheduledController"},description = "Scheduled定时任务")
@RestController
@RequestMapping("rest/scheduled")
public class ScheduledController {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @ApiOperation(value = "初始化任务列表",notes = "只初始化一次",httpMethod = "GET")
    @RequestMapping(value = "v2/save/taskList",method = RequestMethod.GET)
    public List<TaskTimeEntity> saveList() {
        List<TaskTimeEntity> timeEntityList = new ArrayList<>();

        TaskTimeEntity timeEntity = new TaskTimeEntity();
        timeEntity.setCreateTime(new Date());
        timeEntity.setStartFlag(true);
        timeEntity.setInitStartFlag(1);
        timeEntity.setTaskCron("0/5 * * * * ?");
        timeEntity.setTaskDesc("定时任务01");
        timeEntity.setTaskKey("scheduledTask01");

        TaskTimeEntity timeEntity2 = new TaskTimeEntity();
        timeEntity2.setCreateTime(new Date());
        timeEntity2.setStartFlag(false);
        timeEntity2.setInitStartFlag(0);
        timeEntity2.setTaskCron("0/10 * * * * ?");
        timeEntity2.setTaskDesc("定时任务02");
        timeEntity2.setTaskKey("scheduledTask02");

        TaskTimeEntity timeEntity3 = new TaskTimeEntity();
        timeEntity3.setCreateTime(new Date());
        timeEntity3.setStartFlag(true);
        timeEntity3.setInitStartFlag(1);
        timeEntity3.setTaskCron("0/10 * * * * ?");
        timeEntity3.setTaskDesc("定时任务03");
        timeEntity3.setTaskKey("scheduledTask03");

        timeEntityList.add(timeEntity);
        timeEntityList.add(timeEntity2);
        timeEntityList.add(timeEntity3);

        return scheduledTaskService.saveAllTask(timeEntityList);
    }

    @ApiOperation(value = "所有任务列表",httpMethod = "GET")
    @RequestMapping(value = "/taskList",method = RequestMethod.GET)
    public List<TaskTimeEntity> taskList() {
        return scheduledTaskService.getAllTask();
    }

    @ApiOperation(value = "根据任务key => 启动任务",httpMethod = "GET")
    @RequestMapping(value = "start",method = RequestMethod.GET)
    public Object start(@ApiParam(value = "唯一值taskKey") @RequestParam("taskKey") String taskKey) {
        return scheduledTaskService.start(taskKey);
    }

    @ApiOperation(value = "根据任务key => 停止任务",httpMethod = "GET")
    @RequestMapping(value = "stop",method = RequestMethod.GET)
    public Object stop(@RequestParam("taskKey") String taskKey) {
        return scheduledTaskService.stop(taskKey);
    }

    @ApiOperation(value = "根据任务key => 重启任务",httpMethod = "GET")
    @RequestMapping(value = "restart",method = RequestMethod.GET)
    public Object restart(@RequestParam("taskKey") String taskKey) {
        return scheduledTaskService.restart(taskKey);
    }
}
