package com.dist.service;

import com.dist.entity.TaskTimeEntity;

import java.util.List;

/**定时任务接口
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 11:21
 */
public interface ScheduledTaskService {

    /**
     * 初始化任务列表
     * @param taskTimeEntityList
     * @return
     */
    List<TaskTimeEntity> saveAllTask(List<TaskTimeEntity> taskTimeEntityList);

    /**
     * 所有任务列表
     * @return
     */
    List<TaskTimeEntity> getAllTask();

    /**
     * 根据任务key 启动任务
     */
    Boolean start(String taskKey);

    /**
     * 根据任务key 停止任务
     */
    Boolean stop(String taskKey);

    /**
     * 根据任务key 重启任务
     */
    Boolean restart(String taskKey);


    /**
     * 程序启动时初始化  ==> 启动所有正常状态的任务
     */
    void initAllTask(List<TaskTimeEntity> taskTimeEntities);
}
