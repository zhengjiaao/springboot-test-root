/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:06
 * @Since:
 */
package com.zja.chain1;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.zja.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RunFlowExecutorTests {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testConfig() {

        User user = new User();
        user.setId("1");
        user.setName("user1");

        User user2 = new User();
        user2.setId("2");
        user2.setName("user2");

//        LiteflowResponse response = flowExecutor.execute2Resp("chain1", user, user2);
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", user, user2);

        System.out.println(response.isSuccess());
        System.out.println(response.getCode());
        System.out.println(response.getMessage());
        System.out.println(response.getRequestId());
        System.out.println(response.getExecuteStepStr());
        System.out.println(response.getExecuteStepStrWithoutTime());
        System.out.println(response.getExecuteStepStrWithTime());
    }

}
