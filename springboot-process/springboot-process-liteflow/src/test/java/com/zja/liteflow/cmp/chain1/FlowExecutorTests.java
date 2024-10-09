/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:06
 * @Since:
 */
package com.zja.liteflow.cmp.chain1;

import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.common.entity.ValidationResp;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.zja.liteflow.context.OrderContext;
import com.zja.liteflow.context.UserContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * EL规则的写法-基础
 */
@SpringBootTest
public class FlowExecutorTests {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testRun() throws ExecutionException, InterruptedException {

        OrderContext param = new OrderContext();
        param.setId("1");
        param.setName("order-1");
        param.setUserId("user-1");
        param.setType("海外订单");
        param.setOversea(true);
        param.setStatus("进行中");

        OrderContext context2 = new OrderContext();
        context2.setId("2");
        context2.setName("order-2");
        context2.setUserId("user-2");
        context2.setType("普通订单");
        context2.setOversea(false);
        context2.setStatus("已完成");

        // 第一个参数为流程ID，第二个参数为流程入参（可选的），后面可以传入多个上下文class（可选的）
        // LiteflowResponse response = flowExecutor.execute2Resp("chain1", 流程初始参数, OrderContext.class, UserContext.class);

        // 异步执行
        // Future<LiteflowResponse> responseFuture = flowExecutor.execute2Future("chain9", param, context2);
        // LiteflowResponse response = responseFuture.get();

        // 同步执行
        LiteflowResponse response = flowExecutor.execute2Resp("chain10", param, context2);

        // 获取上下文数据：流程在执行过程中，会对上下文数据进行读写操作。一个流程的返回数据也应当包含在上下文中。
        OrderContext orderContext = response.getContextBean(OrderContext.class);
        // UserContext userContext = response.getContextBean(UserContext.class);

        // 获取执行结果 LiteflowResponse对象
        System.out.println(response.isSuccess()); // 流程执行是否成功
        System.out.println(response.getCode());
        System.out.println(response.getMessage());
        System.out.println(response.getRequestId());
        System.out.println(response.getExecuteStepStr()); // 获得执行步骤详细信息
        // Assertions.assertEquals("A[A组件]==>B[B组件]==>C[C组件]==>D", response.getExecuteStepStr());
        System.out.println(response.getExecuteStepQueue()); // 获得执行步骤详细信息: 取到一个队列
        System.out.println(response.getExecuteStepStrWithTime()); // 获得步骤字符串信息
        System.out.println(response.getExecuteStepStrWithoutTime()); // 获得步骤字符串信息: 用于返回不带有耗时时间的步骤字符串。
        System.out.println(response.getTimeoutItems()); // 获得超时的对象Id

        // 获取异常
        if (!response.isSuccess()){
            Exception e = response.getCause();
            System.err.println(e);
        }

    }

    @Test
    public void testValidate() {
        boolean isValid = LiteFlowChainELBuilder.validate("THEN(a, b, h)");
        System.out.println(isValid);

        ValidationResp resp = LiteFlowChainELBuilder.validateWithEx("THEN(a, b, h)");
        if (!resp.isSuccess()) {
            System.err.println(resp.getCause());
        }
    }

}
