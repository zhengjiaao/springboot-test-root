# springboot-test-remote-openfeign

## OpenFeign

[GitHub](https://github.com/OpenFeign/feign)、[官方文档-翻译](https://blog.csdn.net/gou553323/article/details/112651700)、[参考文档](http://events.jianshu.io/p/e4e51e30b111)

### 简单示例
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
