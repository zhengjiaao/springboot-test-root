/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:24
 * @Since:
 */
package com.zja;

import com.zja.task.enums.ActionType;
import com.zja.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: zhengja
 * @since: 2023/06/14 16:24
 */
@SpringBootTest
public class TaskTests {
    private final Task task = new Task();

    @Test
    public void test() {
        task.setTaskId(100L);
        task.updateState(ActionType.START);
        task.updateState(ActionType.STOP);
        task.updateState(ActionType.ACHIEVE);
    }
}
