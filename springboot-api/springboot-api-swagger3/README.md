# springboot-api-swagger3

> 简单介绍swagger的使用和配置

## 依赖引用
```xml
        <!--swagger3-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
        </dependency>
```

## 基本配置

> 先配置swagger3，使其生效

```java
/**
 * http://localhost:19000/swagger-ui/index.html#/
 */
@EnableOpenApi
@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).select()
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

## 访问swagger3

> 启动项目，进行访问swagger3 API管理页面

```java
/**
 * http://localhost:19000/swagger-ui/index.html#/
 */
@SpringBootApplication
public class ApiSwagger3Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiSwagger3Application.class, args);
    }

}
```

[点击-访问swagger3 API 管理页面](http://localhost:19000/swagger-ui/index.html#/)
