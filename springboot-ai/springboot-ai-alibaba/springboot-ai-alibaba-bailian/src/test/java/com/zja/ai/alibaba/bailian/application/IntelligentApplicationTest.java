package com.zja.ai.alibaba.bailian.application;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.junit.jupiter.api.Test;

/**
 * 智能体应用
 * <p>
 * 功能特点
 * 智能体应用依靠大模型进行自主决策，在与用户进行自然语言交互的时候，根据用户问题自主选择使用RAG、插件、长期记忆等多种能力。智能体可以作为企业的智能助手，处理日常任务、提供信息查询和智能推荐等服务。
 * <p>
 * 应用场景和定义
 * 适用于客户服务、销售咨询、技术支持等场景。智能体可以理解客户需求，提供即时的解答和帮助，从而提升企业的服务效率和用户满意度。
 *
 * @Author: zhengja
 * @Date: 2024-10-22 9:23
 */
public class IntelligentApplicationTest {
    @Test
    public void test_1() {
        try {
            callAgentApp();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.out.printf("Exception: %s", e.getMessage());
        }
    }

    public static void callAgentApp()
            throws ApiException, NoApiKeyException, InputRequiredException {
        ApplicationParam param = ApplicationParam.builder()
                // .apiKey("YOUR_API_KEY")
                // .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .appId("fdafee1876f2402f99f1cdc75804212f")
                .prompt("Introduce the capital of China")
                .build();

        Application application = new Application();
        ApplicationResult result = application.call(param);

        System.out.printf("requestId: %s, text: %s, finishReason: %s\n",
                result.getRequestId(), result.getOutput().getText(), result.getOutput().getFinishReason());
    }

}
