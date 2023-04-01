/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-30 19:02
 * @Since:
 */
package com.zja.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhengja
 * @since: 2023/03/30 19:02
 */
@Configuration
public class OpenAiConfiguration {

    @Value("${open.ai.key}")
    private String openAiKey;
    @Value("${open.ai.request.timeout}")
    private long timeout;

    @Bean
    public OpenAiService openAiService(){
        return new OpenAiService(openAiKey);
    }

}
