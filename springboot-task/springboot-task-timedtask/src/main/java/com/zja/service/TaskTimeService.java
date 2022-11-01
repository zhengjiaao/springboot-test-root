package com.zja.service;

import com.zja.entity.TaskTimeEntity;

import java.util.List;

/**任务配置接口
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 10:10
 */
public interface TaskTimeService {

    /**
     * 新增任务
     * @param timeEntity
     * @return
     */
    TaskTimeEntity saveTask(TaskTimeEntity timeEntity);

    /**
     * 修改任务
     * @param timeEntity
     * @return
     */
    TaskTimeEntity updateTask(TaskTimeEntity timeEntity);

    /**
     * 获取所有任务
     * @return
     */
    List<TaskTimeEntity> getAllTask();

    /**
     * 获取某个任务
     * @param taskKey
     * @return
     */
    TaskTimeEntity getTask(String taskKey);

    /**
     * 删除任务
     * @param taskKey
     * @return
     */
    boolean deleteTask(String taskKey);

}
