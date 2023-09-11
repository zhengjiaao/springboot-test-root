# springboot-remote-openfeign

* [OpenFeign GitHub](https://github.com/OpenFeign/feign)
* [OpenFeign 官方文档-翻译](https://blog.csdn.net/gou553323/article/details/112651700)
* [OpenFeign 参考文档](http://events.jianshu.io/p/e4e51e30b111)
* [Feign 详解.md](./Feign 详解.md)

## 引入依赖

> 注，此方式是springboot 集成 openfeign使用案例，不是springcloud+openfeign

```xml
        <!--openfeign start-->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-core</artifactId>
            <scope>compile</scope>
        </dependency>
        <!--以下都是可选的-->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-slf4j</artifactId>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-hystrix</artifactId>
            <scope>compile</scope>
        </dependency>
        <!--客户端-->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-httpclient</artifactId>
        </dependency>
        <!--解码器和编码器-->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-jackson</artifactId>
        </dependency>
        <!--openfeign end-->
```

## 简单示例

### openfeign 原生注解 @RequestLine 实现方式

```java 
interface GitHub {
  @RequestLine("GET /repos/{owner}/{repo}/contributors")
  List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);

  @RequestLine("POST /repos/{owner}/{repo}/issues")
  void createIssue(Issue issue, @Param("owner") String owner, @Param("repo") String repo);

}

public static class Contributor {
  String login;
  int contributions;
}

public static class Issue {
  String title;
  String body;
  List<String> assignees;
  int milestone;
  List<String> labels;
}

public class MyApp {
  public static void main(String... args) {
    GitHub github = Feign.builder()
                         .decoder(new GsonDecoder())
                         .target(GitHub.class, "https://api.github.com");

    // Fetch and print a list of the contributors to this library.
    List<Contributor> contributors = github.contributors("OpenFeign", "feign");
    for (Contributor contributor : contributors) {
      System.out.println(contributor.login + " (" + contributor.contributions + ")");
    }
  }
}
```

## 实战示例
```java
@Configuration
@ConditionalOnProperty(name = "fegin.oa.enabled", matchIfMissing = true)
public class OAFeginAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OAFeginProperties oaConfigProperties() {
        return new OAFeginProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAUserFeign oaUserFeign(OAFeginProperties oaConfigProperties) {
        final Encoder encoder = new JacksonEncoder();
        final Decoder decoder = new JacksonDecoder();
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .requestInterceptor(new OAFeginRequestInterceptor(oaConfigProperties.getApp(), oaConfigProperties.getAppkey()))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(5, TimeUnit.SECONDS, 300, TimeUnit.SECONDS, true))
                .retryer(new Retryer.Default(5000, 5000, 1))
                .target(OAUserFeign.class, oaConfigProperties.getOaUrl());
    }

    @Bean
    @ConditionalOnBean(OAUserFeign.class)
    public OAFeginClient oaFeginClient(OAUserFeign oaUserFeign) {
        return new OAFeginClient(oaUserFeign);
    }

}
```

**日志级别配置详解：**
- NONE：不记录 (DEFAULT)
- BASIC,：仅记录请求方式和URL及响应的状态代码与执行时间
- HEADERS：日志的基本信息与请求及响应的头
- FULL：记录请求与响应的头和正文及元数据

Feign接口配置

```java
@Headers({"Accept: application/json"})
public interface OAUserFeign {

    @RequestLine("GET /userService/serviceapi/organize/get/child/{organizeId}/{depth}")
    OAResultVO<List<OAOrg>> organizeChild(@Param("organizeId") String organizeId,
                                          @Param("depth") String depth);
}
```

拦截器配置

```java
public class OAFeginRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        long timestamp = System.currentTimeMillis();
        String token = UUID.randomUUID().toString().replaceAll("\\-", "");

        //添加额外参数 例：?timestamp=123&token=badsgd4312ds
        Map<String, Collection<String>> queries = new ConcurrentHashMap<>();
        queries.put("timestamp", Arrays.asList(timestamp + ""));
        queries.put("token", Arrays.asList(token));
        template.queries(queries);
    }
}
```


### ### springboot + mvc注解实现方式

> 例如：@PostMapping、@GetMapping 等注解

配置类

```java
    //推荐
    @Bean
    SpringFileFeignClient springFileFeignClient() {
//        final Decoder decoder = new JacksonDecoder();
        final Decoder decoder = new MyJacksonDecoder();
//        final Encoder encoder = new JacksonEncoder();
        //支持上传文件  @PostMapping @RequestPart MultipartFile
        final Encoder encoder = new SpringFormEncoder();
        return Feign.builder()
                .contract(new SpringContract()) // @RequestLine("GET /get") 转为 @GetMapping("/get")
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(SpringFileFeignClient.class, baseUrl);
    }
```

示例

```java
public interface SpringFileFeignClient {
    //文件上传

    /**
     * consume为： MULTIPART_FORM_DATA_VALUE，表明只接收FormData这个类型的数据
     * 必须添加：consumes = MediaType.MULTIPART_FORM_DATA_VALUE
     * 错误信息：No serializer found for class sun.nio.ch.ChannelInputStream and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.LinkedHashMap["file"]->org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile["inputStream"])
     */
    @PostMapping(value = "/post/upload/v1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    Object postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file);

    //文件下载

    @GetMapping(value = "get/download/v1")
    @ApiOperation(value = "下载文件-文件URL")
    String downloadfileURL(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @RequestParam("filename") String filename);

    @GetMapping(value = "get/download/v2")
    @ApiOperation(value = "下载文件-文件流")
    Response downloadfileStream(@ApiParam(value = "filename", defaultValue = "3840x2160.jpg") @RequestParam("filename") String filename);
}
```