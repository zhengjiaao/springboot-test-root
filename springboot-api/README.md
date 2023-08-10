# springboot-api

- [swagger 官方文档](https://swagger.io/)

## springboot-api 应用REST API管理(组件)示例

> 以下是已经完成的示例模块

- [springboot-api-swagger2](./springboot-api-swagger2)
- [springboot-api-swagger2-knife4j](./springboot-api-swagger2-knife4j)
- [springboot-api-swagger3](./springboot-api-swagger3)
- [springboot-api-swagger3-knife4j](./springboot-api-swagger3-knife4j)
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

## 开启认证

> 为了通过项目的安全检查，及等保测评要求，必须要对swagger接口进行权限认证，或者现场环境禁用swagger访问。  
> 这里以swagger+knife4j进行权限认证方式实现，访问swagger页面时，进行拦截弹出登录页面，只有登录成功才能访问swagger页面。

```properties
#作用：拦截swagger api，例如：/swagger-ui.html,/v2/api-docs 等api
# # 开启增强配置 ，默认 false
knife4j.enable=true
# 开启Swagger的Basic认证功能 ，默认是false
knife4j.basic.enable=true
# Basic认证用户名和密码
knife4j.basic.username=test
knife4j.basic.password=pass
```