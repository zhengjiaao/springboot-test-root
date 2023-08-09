/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:03
 * @Since:
 */
package com.zja.task;

import com.zja.task.enums.ActionType;

/**
 * @author: zhengja
 * @since: 2023/06/14 16:03
 */
public interface State {
    // 默认实现, 不做任何处理
    default void update(Task task, ActionType actionType) {
        // do nothing
        System.out.println("State");
    }
}
