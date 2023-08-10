# springboot-api-swagger2

> 简单介绍knife4j搭配swagger2的使用和配置

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

## 依赖引用
```xml

<dependencies>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.10.5</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.10.5</version>
    </dependency>

    <!--引入Knife4j的官方start包,Swagger2基于Springfox2.10.5项目-->
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <!--使用Swagger2-->
        <artifactId>knife4j-spring-boot-starter</artifactId>
        <version>2.0.9</version>
    </dependency>
</dependencies>
```

## 基本配置

> 先配置knife4j和swagger2，使其生效

```java
/**
 * http://localhost:19000/doc.html
 * http://localhost:19000/swagger-ui.html
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.zja")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("提供rest服务").description("我是描述").contact(new Contact("联系人", "www.baidu.com", "123@qq.com"))
                .version("1.0").build();
    }
}
```

## 简单使用

> 实体类和REST接口应用

```java
@ApiModel(value = "用户信息")
@Data
public class UserDTO implements Serializable{
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "时间")
    private Date date;
}


@Api("提供远程-Rest测试接口")
@RestController
@RequestMapping
public class WebRestController {

    @GetMapping(value = "/get/param/v2")
    @ApiOperation(value = "get-拼接参数", notes = "对象属性参数")
    public Object getPath3(UserDTO userDTO) {
        return "get 请求成功: " + userDTO;
    }
}
```

## 访问Knife4j和swagger2

> 启动项目，进行访问Knife4j和swagger2 API管理页面

```java
/**
 * http://localhost:19000/doc.html
 * http://localhost:19000/swagger-ui.html
 */
@SpringBootApplication
public class ApiSwagger2Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiSwagger2Application.class, args);
    }

}
```

[点击-访问Knife4j API 管理页面](http://localhost:19000/doc.html)
[点击-访问swagger2 API 管理页面](http://localhost:19000/swagger-ui.html)
