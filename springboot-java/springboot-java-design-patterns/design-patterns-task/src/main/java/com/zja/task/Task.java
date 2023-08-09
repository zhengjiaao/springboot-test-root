/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:04
 * @Since:
 */
package com.zja.task;

import com.zja.task.enums.ActionType;
import lombok.Data;

/**环境类
 * @author: zhengja
 * @since: 2023/06/14 16:04
 */
@Data
public class Task {
    private Long taskId;
    // 初始化为初始态
    private State state = new TaskInit();

    // 更新状态
    public void updateState(ActionType actionType) {
        state.update(this, actionType);
    }
}
