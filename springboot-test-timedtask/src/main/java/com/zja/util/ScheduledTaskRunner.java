package com.zja.util;

import com.zja.dao.TaskTimeDao;
import com.zja.service.ScheduledTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**实现项目启动时定时任务自启动，不再需要手动启动
 * 项目启动完毕后开启需要自启的任务
 * @see @Order注解的执行优先级是按value值从小到大顺序
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 13:14
 */
@Component
@Order(value = 1)
public class ScheduledTaskRunner implements ApplicationRunner{

    /**
     * 日志
     */
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

   /*     LOGGER.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 开始!");
        List<TaskTimeEntity> timeEntityList = taskTimeDao.findAll();
        if (timeEntityList != null && !timeEntityList.isEmpty()){
            scheduledTaskService.initAllTask(timeEntityList);
        }
        LOGGER.info(" >>>>>> 项目启动完毕, 开启定时任务总数： "+timeEntityList.size()+" => 需要自启的任务 结束！");
*/
    }
}
