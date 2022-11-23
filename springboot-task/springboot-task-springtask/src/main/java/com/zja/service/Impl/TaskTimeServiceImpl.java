package com.zja.service.Impl;

import com.zja.dao.TaskTimeDao;
import com.zja.entity.TaskTimeEntity;
import com.zja.service.TaskTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 任务配置
 */
@Service
public class TaskTimeServiceImpl implements TaskTimeService {

    @Autowired
    private TaskTimeDao taskTimeDao;

    /**
     * 新增任务
     */
    @Override
    public TaskTimeEntity saveTask(TaskTimeEntity timeEntity) {
        TaskTimeEntity task = this.getTask(timeEntity.getTaskKey());
        if (task == null) {
            timeEntity.setCreateTime(new Date());
            return this.taskTimeDao.save(timeEntity);
        }
        System.out.println("任务已经存在");
        return null;
    }

    /**
     * 修改任务
     */
    @Override
    public TaskTimeEntity updateTask(TaskTimeEntity timeEntity) {
        TaskTimeEntity task = this.getTask(timeEntity.getTaskKey());
        if (task != null) {
            timeEntity.setId(task.getId());
            timeEntity.setUpdateTime(new Date());
            return this.taskTimeDao.save(timeEntity);
        }
        System.out.println("任务不存在");
        return null;
    }

    /**
     * 获取所有任务
     */
    @Override
    public List<TaskTimeEntity> getAllTask() {
        List<TaskTimeEntity> timeEntityList = this.taskTimeDao.findAll();
        //System.out.println("测试job1"+timeEntityList.toString());
        return timeEntityList;
    }

    /**
     * 获取某个任务
     */
    @Override
    public TaskTimeEntity getTask(String taskKey) {
        return this.taskTimeDao.queryByTaskKey(taskKey);
    }

    /**
     * 删除任务
     */
    @Override
    public boolean deleteTask(String taskKey) {
        return this.taskTimeDao.deleteByTaskKey(taskKey);
    }

    /**
     * 获取程序初始化需要自启的任务信息
     * @initStartFlag 程序初始化是否启动任务列表 1是 0否
     */
    public List<TaskTimeEntity> findByInitStartFlag(String initStartFlag) {
        return this.taskTimeDao.findByInitStartFlag(initStartFlag);
    }
}
