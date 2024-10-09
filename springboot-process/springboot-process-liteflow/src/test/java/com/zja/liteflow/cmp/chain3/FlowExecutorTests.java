package com.zja.liteflow.cmp.chain3;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 方法级别的声明式组件测试
 *
 * @Author: zhengja
 * @Date: 2024-10-09 10:53
 */
@SpringBootTest
public class FlowExecutorTests {

    @Autowired
    private FlowExecutor flowExecutor;

    @Test
    public void testConfig() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain13", "arg");
        Assertions.assertTrue(response.isSuccess());
    }

}
