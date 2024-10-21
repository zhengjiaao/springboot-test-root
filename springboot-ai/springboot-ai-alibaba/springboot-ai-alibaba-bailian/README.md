# springboot-ai-alibaba

- [阿里云百炼-大模型](https://bailian.console.aliyun.com/#/home)
- [阿里云百炼-百炼文档](https://help.aliyun.com/zh/model-studio/getting-started/what-is-model-studio?spm=a2c4g.11174283.0.i0)

## 快速开始

### 介绍

阿里云的大模型服务平台百炼是一站式的大模型开发及应用构建平台。不论是开发者还是业务人员，都能深入参与大模型应用的设计和构建。您可以通过简单的界面操作，在5分钟内开发出一款大模型应用，或在几小时内训练出一个专属模型，从而将更多精力专注于应用创新。

### 开发参考

[阿里云百炼-开发参考](https://help.aliyun.com/zh/model-studio/developer-reference/?spm=a2c4g.11186623.0.0.3c8447bbqUcjGW)

1. [申请百炼API Key](https://help.aliyun.com/zh/model-studio/developer-reference/get-api-key)
2. [配置API Key到环境变量](https://help.aliyun.com/zh/model-studio/developer-reference/configure-api-key-through-environment-variables)
3. [使用DashScope SDK调用](https://help.aliyun.com/zh/model-studio/developer-reference/install-sdk?spm=a2c4g.11186623.0.0.3da67980NV5Txg#1dd22c4243o50)
4. [模型调用](https://help.aliyun.com/zh/model-studio/developer-reference/models/?spm=a2c4g.11186623.0.0.36b82117Ve2Rr0)

#### 使用

DashScope 公有云的API Endpoint：

[DashScope api 参考](https://bailian.console.aliyun.com/?spm=a2c4g.11186623.0.0.56f355efWqdX6y#/model-market/detail/qwen-plus-0919)

```text
通过HTTP调用时需配置的endpoint：

使用通义千问大语言模型：POST https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation

使用通义千问VL或通义千问Audio模型：POST https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation
```

您需要已[获取API Key](https://help.aliyun.com/zh/model-studio/developer-reference/get-api-key)
并[配置API Key到环境变量](https://help.aliyun.com/zh/model-studio/developer-reference/configure-api-key-through-environment-variables)
。如果通过DashScope
SDK进行调用，还需要[DashScope SDK](https://help.aliyun.com/zh/model-studio/developer-reference/install-sdk#f3e80b21069aa)。

#### Java 示例

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/dashscope-sdk-java -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dashscope-sdk-java</artifactId>
    <version>2.16.5</version>
</dependency>
```

```java
// 建议dashscope SDK的版本 >= 2.12.0

import java.util.Arrays;
import java.lang.System;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

public class Main {
    public static GenerationResult callWithMessage() throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("你是谁？")
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }

    public static void main(String[] args) {
        try {
            GenerationResult result = callWithMessage();
            System.out.println(JsonUtils.toJson(result));
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 使用日志框架记录异常信息
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }
        System.exit(0);
    }
}

```

响应示例：

```json
{
  "requestId": "86dd52a9-23ec-9804-8f82-85f4c7fd5114",
  "usage": {
    "input_tokens": 22,
    "output_tokens": 17,
    "total_tokens": 39
  },
  "output": {
    "choices": [
      {
        "finish_reason": "stop",
        "message": {
          "role": "assistant",
          "content": "我是阿里云开发的一款超大规模语言模型，我叫通义千问。"
        }
      }
    ]
  }
}
```

#### curl 示例

请求示例

```shell
curl --location "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation" \
--header "Authorization: Bearer $DASHSCOPE_API_KEY" \
--header "Content-Type: application/json" \
--data '{
    "model": "qwen-plus",
    "input":{
        "messages":[      
            {
                "role": "system",
                "content": "You are a helpful assistant."
            },
            {
                "role": "userVO",
                "content": "你是谁？"
            }
        ]
    },
    "parameters": {
        "result_format": "message"
    }
}'
```

响应示例：

```json
{
  "output": {
    "choices": [
      {
        "finish_reason": "stop",
        "message": {
          "role": "assistant",
          "content": "我是通义千问，由阿里云开发的AI助手。我被设计用来回答各种问题、提供信息和与用户进行对话。有什么我可以帮助你的吗？"
        }
      }
    ]
  },
  "usage": {
    "total_tokens": 58,
    "output_tokens": 36,
    "input_tokens": 22
  },
  "request_id": "39377fd7-26dd-99f5-b539-5fd004b6ecb5"
}
```