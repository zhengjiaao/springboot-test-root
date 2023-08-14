package com.zja.controller;

import com.zja.entity.TaskTimeEntity;
import com.zja.service.ScheduledTaskService;
import com.zja.service.TaskTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/13 17:13
 */
@Api(tags = {"动态定时任务在数据库中的配置"})
@RestController
@RequestMapping(value = "rest/tasktime")
public class TaskTimeController {

    @Autowired
    private TaskTimeService taskTimeService;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @ApiOperation(value = "初始化任务列表",notes = "只初始化一次",httpMethod = "GET")
    @RequestMapping(value = "v1/save/taskList",method = RequestMethod.GET)
    public List<TaskTimeEntity> saveList() {
        List<TaskTimeEntity> timeEntityList = new ArrayList<>();

        TaskTimeEntity timeEntity = new TaskTimeEntity();
        timeEntity.setCreateTime(new Date());
        timeEntity.setStartFlag(true);
        timeEntity.setInitStartFlag(1);
        timeEntity.setTaskCron("0/5 * * * * ?");
        timeEntity.setTaskDesc("定时任务04");
        timeEntity.setTaskKey("scheduledTask04");

        TaskTimeEntity timeEntity2 = new TaskTimeEntity();
        timeEntity2.setCreateTime(new Date());
        timeEntity2.setStartFlag(false);
        timeEntity2.setInitStartFlag(0);
        timeEntity2.setTaskCron("0/10 * * * * ?");
        timeEntity2.setTaskDesc("定时任务05");
        timeEntity2.setTaskKey("scheduledTask05");

        TaskTimeEntity timeEntity3 = new TaskTimeEntity();
        timeEntity3.setCreateTime(new Date());
        timeEntity3.setStartFlag(true);
        timeEntity3.setInitStartFlag(1);
        timeEntity3.setTaskCron("0/10 * * * * ?");
        timeEntity3.setTaskDesc("定时任务06");
        timeEntity3.setTaskKey("scheduledTask06");

        timeEntityList.add(timeEntity);
        timeEntityList.add(timeEntity2);
        timeEntityList.add(timeEntity3);

        return scheduledTaskService.saveAllTask(timeEntityList);
    }

    @ApiOperation(value = "新增定时任务",httpMethod = "POST")
    @RequestMapping(value = "save/task",method = RequestMethod.POST)
    public Object saveTask(@ApiParam(value = "具体参考：Model") @RequestBody TaskTimeEntity timeEntity){
        return this.taskTimeService.saveTask(timeEntity);
    }

    @ApiOperation(value = "修改定时任务",httpMethod = "PUT")
    @RequestMapping(value = "update/task",method = RequestMethod.PUT)
    public Object updateTask(@ApiParam(value = "具体参考：Model") @RequestBody TaskTimeEntity timeEntity){
        return this.taskTimeService.updateTask(timeEntity);
    }

    @ApiOperation(value = "查看所有定时任务",httpMethod = "GET")
    @RequestMapping(value = "get/all/task",method = RequestMethod.GET)
    public Object getAllTask(){
        return this.taskTimeService.getAllTask();
    }

    @ApiOperation(value = "查看某个定时任务",httpMethod = "GET")
    @RequestMapping(value = "get/task",method = RequestMethod.GET)
    public Object getTask(@ApiParam(value = "任务唯一 key值") @RequestParam String taskKey){
        return this.taskTimeService.getTask(taskKey);
    }

    @ApiOperation(value = "删除定时任务",httpMethod = "DELETE")
    @RequestMapping(value = "delete/task",method = RequestMethod.DELETE)
    public Object deleteTask(@ApiParam(value = "任务唯一 key值") @RequestParam String taskKey){
        return this.taskTimeService.deleteTask(taskKey);
    }
}
