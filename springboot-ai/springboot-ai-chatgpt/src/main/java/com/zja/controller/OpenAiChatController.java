/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-30 19:07
 * @Since:
 */
package com.zja.controller;

import com.alibaba.fastjson.JSONObject;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2023/03/30 19:07
 */
@Api("")
@RestController
@RequestMapping("/ai")
public class OpenAiChatController {

    @Value("${open.ai.model}")
    private String openAiModel;
    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/chat")
    @ApiOperation(value = "聊天", notes = "")
    public String chat(String prompt) {
        CompletionRequest completionRequest = CompletionRequest.builder().prompt(prompt).model(openAiModel).echo(true).temperature(0.7).topP(1d).frequencyPenalty(0d).presencePenalty(0d).maxTokens(1000).build();
        CompletionResult completionResult = openAiService.createCompletion(completionRequest);
        return completionResult.getChoices().get(0).getText();
    }

    @PostMapping("/chat/v2")
    @ApiOperation(value = "聊天2", notes = "")
    public Map chatV2() {

        try {
            // json字符串的两种形式
            String k = "{\n" + "     \"model\": \"gpt-3.5-turbo\",\n" + "     \"messages\": [{\"role\": \"user\", \"content\": \"Say this is a test!\"}],\n" + "     \"temperature\": 0.7\n" + "}";

            // 创建一个OkHttpClient
            OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(Duration.ofSeconds(60 * 10)).readTimeout(Duration.ofSeconds(60 * 10)).callTimeout(Duration.ofSeconds(60 * 10)).build();
            // client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
            // client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

            // 设置请求头
            MediaType mediaType = MediaType.parse("application/json");
            // 请求体
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, k);
            // 创建一个request
            Request request = new Request.Builder().post(requestBody).url("https://api.openai.com/v1/chat/completions").addHeader("content-type", "application/json").addHeader("authorization", "Bearer sess-b1N9dTWt9QIW8NtcSwSiTtQQc7p66KXfMGDQ1G9x").build();

            // 获得返回
            String responseData = null;
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // 获取返回值
                if (response.body() != null) {
                    responseData = response.body().string();
                }
            }
            // 将json字符串转为JSONObject
            JSONObject data = JSONObject.parseObject(responseData);
            // 将jsonObject 转为Map
            return data.toJavaObject(Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();

    }

}