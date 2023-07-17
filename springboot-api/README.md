# springboot-api

- [swagger 官方文档](https://swagger.io/)

## springboot-api 应用REST API管理(组件)示例

> 以下是已经完成的示例模块

- [springboot-api-swagger2](./springboot-api-swagger2)
- [springboot-api-swagger3](./springboot-api-swagger3)
- [Swagger API 生成word文档](https://github.com/zhengjiaao/swagger-api-docs-root)

## 版本选型

| swagger | knife4j | 描述           |
|---------|---------|--------------|
| 2.9.2   | 不支持     | 建议升级为 2.10.5 |
| 2.10.5  | 2.0.9   |              |
| 3.0.0   | 3.0.3   |              |

注：swagger 从`2.9.2`升级为`2.10.5`，需要切换启动注解

```java
//@EnableSwagger2	// 2.9.2 注解启动方式
@EnableSwagger2WebMvc  //2.10.5 注解启动方式
@EnableKnife4j  //仅需加入此注解就可以了，swagger配置不变，必须配合 swagger2.10.5
@Configuration
public class Swagger2Config {

}
```
