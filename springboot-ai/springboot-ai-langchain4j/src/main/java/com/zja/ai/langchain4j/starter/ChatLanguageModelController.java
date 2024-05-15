package com.zja.ai.langchain4j.starter;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天语言模型
 * This is an example of using a {@link ChatLanguageModel}, a low-level LangChain4j API.
 *
 * @Author: zhengja
 * @Date: 2024-05-15 15:28
 */
@RestController
class ChatLanguageModelController {

    ChatLanguageModel chatLanguageModel;

    ChatLanguageModelController(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @GetMapping("/model")
    public String model(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return chatLanguageModel.generate(message);
    }
}