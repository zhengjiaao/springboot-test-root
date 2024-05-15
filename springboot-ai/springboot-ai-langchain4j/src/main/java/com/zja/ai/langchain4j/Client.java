package com.zja.ai.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * @Author: zhengja
 * @Date: 2024-05-15 15:04
 */
public class Client {

    public static void main(String[] args) {
        String apiKey = "demo";
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        String answer = model.generate("Say 'Hello World'");
        System.out.println(answer); // Hello World
    }
}
