package com.zja.ai.deepseek.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-02-17 16:02
 */
@Component
public class DeepseekApiClient {

    @Value("${deepseek.api_url}")
    private String apiUrl;

    @Value("${deepseek.api_key}")
    private String apiKey;

    public String sendMessageToDeepseek(String message) {
        try {
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            // systemMessage.put("content", "You are a helpful assistant."); // 回答的是英文
            systemMessage.put("content", "你是一个乐于助人的助手。"); // 回答的是中文
            messages.add(systemMessage);

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.add(userMessage);

            ChatRequest chatRequest = new ChatRequest("deepseek-chat", messages, false);

            String responseJson = sendPostRequest(chatRequest);
            if (responseJson != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseJson);

                // 提取助手的回复内容
                JsonNode choicesNode = rootNode.path("choices");
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode firstChoiceNode = choicesNode.get(0);
                    JsonNode messageNode = firstChoiceNode.path("message");
                    JsonNode contentNode = messageNode.path("content");
                    if (contentNode.isTextual()) {
                        return contentNode.asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error sending message to Deepseek API";
        }
        return "No response from Deepseek API";
    }

    public String sendPostRequest(Object requestBody) throws IOException {
        String apiChatUrl = apiUrl + "/chat/completions";

        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)  // 连接超时时间，单位毫秒
                .setConnectionRequestTimeout(5000)  // 从连接池获取连接的超时时间，单位毫秒
                .setSocketTimeout(1000 * 60 * 3)  // 读取超时时间，单位毫秒
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build()) {
            HttpPost request = new HttpPost(apiChatUrl);
            request.setHeader("Authorization", "Bearer " + apiKey);
            request.setHeader("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(requestBody);
            request.setEntity(new StringEntity(jsonRequest));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        }
        return null;
    }
}