package com.dist.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**Quartz 动态控制任务(暂停，启动，修改执行时间)
 * 这里只是简单测试-可以完善修改成(配合数据库动态设置)
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 14:48
 */
@Api(tags = {"QuartzTaskController"},description = "Quartz 动态控制任务")
@RestController
@RequestMapping("rest/quartz")
public class QuartzTaskController {

    @Autowired
    @Qualifier("sch")
    private Scheduler scheduler;

    @ApiOperation(value = "动态关闭任务",httpMethod = "GET")
    @RequestMapping(value = "/pause",method = RequestMethod.GET)
    public Object pause(@ApiParam(value = "任务名称",defaultValue = "job1") @RequestParam String jobName) throws Exception {
        System.out.println( scheduler );
        JobKey key = new JobKey(jobName);
        scheduler.pauseJob(key);
        return "pause";
    }

    @ApiOperation(value = "动态启动任务",httpMethod = "GET")
    @RequestMapping(value = "/start",method = RequestMethod.GET)
    public Object start(@ApiParam(value = "任务名称",defaultValue = "job1") @RequestParam String jobName) throws Exception {
        System.out.println( scheduler );
        JobKey key = new JobKey(jobName);
        scheduler.resumeJob(key);
        return "start";
    }

    @ApiOperation(value = "动态修改任务执行的时间",httpMethod = "GET")
    @RequestMapping(value = "/trigger",method = RequestMethod.GET)
    public Object trigger(@ApiParam(value = "任务名称",defaultValue = "job1") @RequestParam String jobName,
                          @ApiParam(value = "cron 表达式",defaultValue = "0/10 * * * * ?") @RequestParam String cron) throws Exception {
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

        return "trigger - 任务启动时间："+date;
    }

}
