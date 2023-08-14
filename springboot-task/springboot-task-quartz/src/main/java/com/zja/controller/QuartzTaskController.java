package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Quartz 动态控制任务(暂停，启动，修改执行时间)
 * 真实环境使用请配合数据库动态设置
 */
@Api(tags = {"Quartz 动态控制任务"})
@RestController
@RequestMapping("/quartz")
public class QuartzTaskController {

    @Autowired
    @Qualifier("sch")
    private Scheduler scheduler;

    @ApiOperation(value = "任务关闭")
    @GetMapping(value = "/task/close")
    public String pause(@ApiParam(value = "任务名称", defaultValue = "job1") @RequestParam String jobName) throws Exception {
        JobKey key = new JobKey(jobName);
        scheduler.pauseJob(key);
        return "task close.";
    }

    @ApiOperation(value = "任务启动")
    @GetMapping(value = "/task/start")
    public String start(@ApiParam(value = "任务名称", defaultValue = "job1") @RequestParam String jobName) throws Exception {
        System.out.println(scheduler);
        JobKey key = new JobKey(jobName);
        scheduler.resumeJob(key);
        return "task start.";
    }

    @ApiOperation(value = "更新任务执行时间")
    @GetMapping(value = "/task/update")
    public String trigger(@ApiParam(value = "任务名称", defaultValue = "job1") @RequestParam String jobName,
                          @ApiParam(value = "cron 表达式", defaultValue = "0/10 * * * * ?") @RequestParam String cron) throws Exception {
        // 获取任务
        JobKey jobKey = new JobKey(jobName);
        // 获取 jobDetail
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        // 生成 trigger
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        // 删除任务，不删除会报错。报任务已存在
        scheduler.deleteJob(jobKey);
        // 启动任务
        Date date = scheduler.scheduleJob(jobDetail, trigger);

        return "task 启动时间更新为：" + date;
    }

}
