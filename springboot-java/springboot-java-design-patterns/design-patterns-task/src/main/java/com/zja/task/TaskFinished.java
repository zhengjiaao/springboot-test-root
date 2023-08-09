/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:13
 * @Since:
 */
package com.zja.task;

import com.zja.task.enums.ActionType;

/**任务完成状态
 * @author: zhengja
 * @since: 2023/06/14 16:13
 */
public class TaskFinished implements State {

    @Override
    public void update(Task task, ActionType actionType) {
        // do nothing
        System.out.println("TaskFinished");
    }
}
