package com.zja.process.util;

import lombok.experimental.UtilityClass;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-09-02 16:31
 */
@UtilityClass
public class FlowableUtils {

    public Task oneTaskByBizKey(TaskService taskService, String bizKey) {
        List<Task> list = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .list();
        if (list.isEmpty()) return null;
        if (list.size() > 1) throw new IllegalStateException("同一业务主键存在多个激活任务，请精确定位");
        return list.get(0);
    }
}