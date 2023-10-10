/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:14
 * @Since:
 */
package com.zja.task;

import com.zja.task.enums.ActionType;

/**任务暂停状态
 * @author: zhengja
 * @since: 2023/06/14 16:14
 */
public class TaskPaused implements State {

    @Override
    public void update(Task task, ActionType actionType) {
        // do nothing
        System.out.println("TaskPaused");
    }
}
