package com.zja.service;

import com.zja.entity.TaskTimeEntity;

import java.util.List;

/**
 * 任务配置接口
 */
public interface TaskTimeService {

    /**
     * 新增任务
     */
    TaskTimeEntity saveTask(TaskTimeEntity timeEntity);

    /**
     * 修改任务
     */
    TaskTimeEntity updateTask(TaskTimeEntity timeEntity);

    /**
     * 获取所有任务
     */
    List<TaskTimeEntity> getAllTask();

    /**
     * 获取某个任务
     */
    TaskTimeEntity getTask(String taskKey);

    /**
     * 删除任务
     */
    boolean deleteTask(String taskKey);

}
