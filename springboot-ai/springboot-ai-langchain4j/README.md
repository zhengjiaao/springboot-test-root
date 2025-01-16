# springboot-ai-langchain4j

- [github | langchain4j](https://github.com/langchain4j/langchain4j)
- [docs | langchain4j](https://docs.langchain4j.dev/)
- [Get Started | langchain4j](https://docs.langchain4j.dev/get-started)

LangChain4j 的目标是简化将 AI/LLM 功能集成到 Java 应用程序中。

方法如下：

* 统一 API： LLM 提供商（如 OpenAI 或 Google Vertex AI）和嵌入（向量）存储（如 Pinecone 或 Milvus）使用专有 API。LangChain4j 提供统一 API，避免了学习和实现每个 API 的特定 API。要尝试不同的 LLM 或嵌入存储，您可以轻松地在它们之间切换，而无需重写代码。LangChain4j 目前支持15 多个流行的 LLM 提供商 和15 多个嵌入存储。
* 全面的工具箱： 自 2023 年初以来，社区一直在构建大量由 LLM 驱动的应用程序，识别常见的抽象、模式和技术。LangChain4j 已将这些提炼为实用代码。我们的工具箱包括从低级提示模板、聊天内存管理和函数调用到高级模式（如代理和 RAG）的各种工具。对于每个抽象，我们都提供了一个接口以及基于常见技术的多种现成实现。无论您是构建聊天机器人还是开发具有从数据提取到检索的完整管道的 RAG，LangChain4j 都提供了多种选择。
* 大量示例： 这些示例展示了如何开始创建各种 LLM 驱动的应用程序，提供灵感并使您能够快速开始构建。

LangChain4j 于 2023 年初在 ChatGPT 炒作中开始开发。我们注意到，众多 Python 和 JavaScript LLM 库和框架缺乏 Java 对应物，我们必须解决这个问题！虽然我们的名字里有“LangChain”，但该项目融合了 LangChain、Haystack、LlamaIndex 和更广泛社区的思想和概念，并加入了我们自己的创新元素。

- [介绍 | langchain4j](https://docs.langchain4j.dev/intro)