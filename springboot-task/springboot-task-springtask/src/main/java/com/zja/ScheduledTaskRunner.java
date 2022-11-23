package com.zja;

import com.zja.dao.TaskTimeDao;
import com.zja.entity.TaskTimeEntity;
import com.zja.service.ScheduledTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 定时任务 项目服务启动完成后，任务自动启动
 */
//@Component
//@Order(value = 1) //注解@Order的执行优先级是按value值从小到大顺序
public class ScheduledTaskRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskRunner.class);

    @Autowired
    private TaskTimeDao taskTimeDao;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    /**
     * 程序启动完毕后,需要自启的任务
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<TaskTimeEntity> timeEntityList = taskTimeDao.findAll();
        LOGGER.info("项目启动完毕, 开启需要自启的任务：" + timeEntityList.size());
        if (!ObjectUtils.isEmpty(timeEntityList)) {
            scheduledTaskService.initAllTask(timeEntityList);
        }
    }
}
