package com.zja.controller;

import com.zja.task.SpringTaskAsyncExample;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务多线程运行
 */
@Api(tags = {"SpringTaskAsyncController"},description = "Spring Task")
@RestController
@RequestMapping(value = "/springtask")
public class SpringTaskAsyncController {

    @Autowired
    private SpringTaskAsyncExample springTaskAsyncExample;

    @ApiOperation(value = "开启所有定时任务-支持多线程",httpMethod = "GET")
    @RequestMapping(value = "taskExecutor",method = RequestMethod.GET)
    public void alarmTask() throws Exception {
        springTaskAsyncExample.run();
        springTaskAsyncExample.run1();
        springTaskAsyncExample.run2();
        springTaskAsyncExample.run3();
    }

    @ApiOperation(value = "开启单个定时任务-多线程",httpMethod = "GET")
    @RequestMapping(value = "taskExecutor/run",method = RequestMethod.GET)
    public void alarmTask1() throws Exception {
        springTaskAsyncExample.run();
    }

    @ApiOperation(value = "定时任务1-多线程",httpMethod = "GET")
    @RequestMapping(value = "taskExecutor/run1",method = RequestMethod.GET)
    public void alarmTask2() throws Exception {
        springTaskAsyncExample.run1();
    }

    @ApiOperation(value = "定时任务2-多线程",httpMethod = "GET")
    @RequestMapping(value = "taskExecutor/run2",method = RequestMethod.GET)
    public void alarmTask3() throws Exception {
        springTaskAsyncExample.run2();
    }

    @ApiOperation(value = "定时任务3-多线程",httpMethod = "GET")
    @RequestMapping(value = "taskExecutor/run3",method = RequestMethod.GET)
    public void alarmTask4() throws Exception {
        springTaskAsyncExample.run3();
    }
}
