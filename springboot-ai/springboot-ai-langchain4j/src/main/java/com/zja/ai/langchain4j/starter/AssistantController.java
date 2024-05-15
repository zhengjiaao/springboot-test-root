package com.zja.ai.langchain4j.starter;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.spring.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 助理模型
 * This is an example of using an {@link AiService}, a high-level LangChain4j API.
 *
 * @Author: zhengja
 * @Date: 2024-05-15 15:12
 */
@RestController
class AssistantController {

    @Autowired
    Assistant assistant;

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "What is the time now?") String message) {
        return assistant.chat(message);
    }
}