package com.zja.ai.langchain4j.starter;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * @Author: zhengja
 * @Date: 2024-05-15 15:12
 */
@AiService
public interface Assistant {
    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);
}
