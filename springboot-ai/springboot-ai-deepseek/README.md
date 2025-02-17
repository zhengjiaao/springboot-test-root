# springboot-ai-deepseek

- [DeepSeek 官网](https://www.deepseek.com/)
- [DeepSeek 官网开放平台 申请apiKey](https://platform.deepseek.com/usage)
- [DeepSeek 官网开放平台 接口文档](https://api-docs.deepseek.com/zh-cn/)

## 示例

```shell

curl https://api.deepseek.com/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <DeepSeek API Key>" \
  -d '{
        "model": "deepseek-chat",
        "messages": [
          {"role": "system", "content": "You are a helpful assistant."},
          {"role": "user", "content": "Hello!"}
        ],
        "stream": false
      }'
```