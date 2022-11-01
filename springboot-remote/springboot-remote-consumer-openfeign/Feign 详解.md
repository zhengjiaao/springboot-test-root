# Feign 详解

### 1、Feign 是什么

`Feign`是一个`http`请求调用的轻量级框架，可以以`Java`接口**注解的方式**调用`Http`请求。`Feign`通过处理注解，将请求模板化，当实际调用的时候，传入参数，根据参数再应用到请求上，进而转化成真正的请求，封装了`http`调用流程。

### 2、为什么选择 Feign

如果不使用`rpc`框架，那么调用服务需要走`http`的话，无论是使用 JDK 自带的 `URLConnection`，还是使用Http工具包 Apache 的`httpclient`， 亦或是 `OkHttp`， 都需要自行配置请求`head`、`body`，然后才能发起请求。获得响应体后，还需解析等操作，十分繁琐。

而`Feign` 只需要定义一个接口，并且通过注解的形式定义好请求模板，就可以项使用本地接口一样，使用`Http`请求。

### 3、Feign 是怎么工作

`Feign` 是通过定义接口，并且在接口方法上使用注解定义请求模板，然后通过 `Feign.builder()` 进行构建后，即可像使用本地接口方法调用`Http`请求。简单实例如下：



```java
// 定义接口
interface GitHub {
   // 通过注解定义 请求模板
  @RequestLine("GET /repos/{owner}/{repo}/contributors")
  List<Contributor> contributors(@Param("owner") String owner, 
                                 @Param("repo") String repo);
}

public static class Contributor {
  String login;
  int contributions;
}

public class MyApp {
  public static void main(String... args) {
      // 构建接口
    GitHub github = Feign.builder()
                         .decoder(new GsonDecoder())
                         .target(GitHub.class, "https://api.github.com");

    // 发送请求
    List<Contributor> contributors = github.contributors("OpenFeign", "feign");
    for (Contributor contributor : contributors) {
      System.out.println(contributor.login + " (" + contributor.contributions + ")");
    }
  }
}
```

### 4、Feign 使用详解

#### 接口注解

| 注解         | 目标       | 用法                                                         |
| ------------ | ---------- | ------------------------------------------------------------ |
| @RequestLine | 方法       | 定义请求 的`HttpMethod`。用大括号括起来的`Expression`使用它们对应的带注释的参数来解析。 |
| @Param       | 参数       | 定义一个模板变量，其值将用于解析相应的模板`Expression`，按作为注释值提供的名称提供 |
| @Headers     | 方法、接口 | 定义一个请求头模板，其中可以使用大括号括起来的表达式，将使用 @Param 注解的参数解析，标注在方法上只针对某个请求，标注在类上，表示作用的所有的请求上 |
| @QueryMap    | 参数       | 定义`Map`名称-值对或 POJO，以扩展为查询字符串。              |
| @HeaderMap   | 参数       | 定义一个`Map`名称-值对，展开成 请求头                        |
| @Body        | 方法       | 类似于一个 URI 模板，他使用 @Param 注解的参数来解析模板中的表达式 |

#### 模板和表达式

Feign 是由 URI 模板 `Expressions` 定义的简单字符串表达式，并且使用 `@Param` 注解的方法参数，进行解析。



```java
public interface GitHub {
  @RequestLine("GET /repos/{owner}/{repo}/contributors")
  List<Contributor> contributors(@Param("owner") String owner,
                                 @Param("repo") String repository);
}
```

如上所示代码中 通过 `@RequestLine` 注解标注的为 URL模板，其中，有大括号括起来的  **woner** 和 **repo**  在发送请求时，会被有 `@Param` 注解的参数 `owner` 和`repo` 所替换。

表达式必须用大括号括起来，`{}`并且可以包含正则表达式模式，用冒号分隔`:` 以限制解析值。 *示例* `owner`必须是字母。`{owner:[a-zA-Z]*}`

#### 请求参数扩展

`RequestLine`和`QueryMap`模板遵循URI模板 规范，该规范指定以下内容：

- 未解析的表达式被省略。
- 所有文字和变量值都经过 `pct-encoded` 编码，如果尚未`encoded`通过`@Param`注释编码或标记。

#### 未定义或空值

未定义的表达式是指表达式的值是显式`null`或未提供值的表达式。根据[URI 模板 - RFC 6570](https://links.jianshu.com/go?to=https%3A%2F%2Ftools.ietf.org%2Fhtml%2Frfc6570)，可以为表达式提供空值。Feign 解析表达式时，首先判断该值是否已定义，如果已定义则查询参数将保留。如果表达式未定义，则删除查询参数。

##### 空字符串



```java
// 定义接口
@RequestLine("POST /user/map")
String map(@QueryMap Map<String, Object> queryMap);

Map<String, Object> queryMap = new LinkedHashMap<>();
 queryMap.put("param", "");

// 调用服务
this.client.map(queryMap);

// 解析后的路径为
http://localhost/user/map?param
```

##### 没有参数



```java
// 定义接口
@RequestLine("POST /user/map")
String map(@QueryMap Map<String, Object> queryMap);

Map<String, Object> queryMap = new LinkedHashMap<>();

// 调用服务
this.client.map(queryMap);

// 解析后的路径为
http://localhost/user/map
```

##### 未定义



```java
// 定义接口
@RequestLine("POST /user/map")
String map(@QueryMap Map<String, Object> queryMap);

Map<String, Object> queryMap = new LinkedHashMap<>();
 queryMap.put("param", null);

// 调用服务
this.client.map(queryMap);

// 解析后的路径为
http://localhost/user/map?param
```

#### 请求头扩展

在`Feign` 中可以通过 `Headers` 和`HeaderMap` 两个注解来扩展请求头，并且遵循以下规则，

- 未解析的表达式被忽略，如果请求头的值为空，这删除整个请求头。
- 不执行`pct-encoded` 编码

##### Headers

Headers 注解可以标注到 api 方法上，也可以标注到客户端即接口上，标注在接口上，表示对所有的请求都起作用，标注在 方法上 只对所标注的方法起作用。



```java
@Headers("Accept: application/json")  // 此处标注，表示对所有的请求都起作用
interface BaseApi<V> {
  @Headers("Content-Type: application/json") // 只对当前请求起作用
  @RequestLine("PUT /api/{key}")
  void put(@Param("key") String key, V value);
}
```

在方法上标注时可以设置动态的内容，如下所示：



```java
public interface Api {
   @RequestLine("POST /")
    // 动态指定 Token 值，在方法参数中需要存在 @Param 标注的名为 token的参数
   @Headers("X-Ping: {token}") 
   void post(@Param("token") String token);
}
```

##### HeaderMap

`Headers` 虽然也能动态设置头信息，但是，当请求头的键和个数不确定时，`Headers` 就不能满足了，此时我们可以使用 `HeaderMap` 注解的 方法参数来更灵活的动态指定请求头



```java
public interface Api {
   @RequestLine("POST /")
   void post(@HeaderMap Map<String, Object> headerMap);
}
```

#### 请求正文扩展

`Body` 模板遵循与请求参数扩展相同的扩展，但有一下更改

- 未解析的表达式被省略
- 扩展值在放置在正文之前不会被 `Encoder` 进行编码
- `Content-Type` 请求头必须设置

如下所示：



```java
interface LoginClient {

  @RequestLine("POST /")
  @Headers("Content-Type: application/xml")
  @Body("<login \"user_name\"=\"{user_name}\" \"password\"=\"{password}\"/>")
  void xml(@Param("user_name") String user, @Param("password") String password);

  @RequestLine("POST /")
  @Headers("Content-Type: application/json")
  // json 花括号必须转义
  @Body("%7B\"user_name\": \"{user_name}\", \"password\": \"{password}\"%7D")
  void json(@Param("user_name") String user, @Param("password") String password);
}

public class Example {
  public static void main(String[] args) {
      // <login "user_name"="denominator" "password"="secret"/>
    client.xml("denominator", "secret"); 
       // {"user_name": "denominator", "password": "secret"}
    client.json("denominator", "secret");
  }
}
```

#### 编码器

将请求正文发送到服务器的最简单方法是定义一个`POST`方法，该方法具有`String`or`byte[]`参数而没有任何注释。您可能需要添加`Content-Type`标题。



```java
interface LoginClient {
  @RequestLine("POST /")
  @Headers("Content-Type: application/json")
  void login(String content);
}

public class Example {
  public static void main(String[] args) {
    client.login("{\"user_name\": \"denominator\", \"password\": \"secret\"}");
  }
}
```

通过配置`Encoder`，您可以发送类型安全的请求正文。`feign-gson`这是使用扩展的示例：



```java
static class Credentials {
  final String user_name;
  final String password;

  Credentials(String user_name, String password) {
    this.user_name = user_name;
    this.password = password;
  }
}

interface LoginClient {
  @RequestLine("POST /")
  void login(Credentials creds);
}

public class Example {
  public static void main(String[] args) {
    LoginClient client = Feign.builder()
                              .encoder(new GsonEncoder()) // 引入GsonEncoder编码器
                              .target(LoginClient.class, "https://foo.com");

    client.login(new Credentials("denominator", "secret"));
  }
}
```

#### 解码器

在实际开发中，服务端返回的数据可能是个JSON字符串或者字节数组，在 `Feign` 中可以通过指定解码器，把响应数据解析为你想要的数据类型。以下是使用`feign-gson`  进行解码的实例：



```java
public interface WebFeign {
    
    @RequestLine("POST /user/body")
    @Headers({"Content-Type: application/json"})
    User postBody(User user);
}


WebFeign webFeign = Feign.builder()
    .encoder(new GsonEncoder())
    // 指定解码器
    .decoder(new GsonDecoder())
    .target(WebFeign.class, "http://localhost:10010");

User user = new User().setName("张三")
    .setAge(20);

User body = webFeign.postBody(user);
```

#### 请求客户端扩展

`Feign` 底层默认使用的是 JDK 自带的 `URLConnection` 实现的网络请求。在`Feign` 中也可以在构建时指定自定的底层网络请求工具，比如常用的`OkHttp` 和 `Apache HttpClient` 等。`Feign` 也已经实现了 这两个客户端，只需要引入依赖就可以直接使用。



```xml
<!-- 引入 HttpClient  -->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
    <version>${feign.version}</version>
</dependency>
<!-- 引入 OkHttp -->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-okhttp</artifactId>
    <version>${feign.version}</version>
</dependency>
```

根据自己的需求引入依赖即可，引入依赖后在构建时指定所需的客户端即可，如下：



```java
Feign.builder()
    .client(new ApacheHttpClient()) // 使用 HttpClient
    .logger(new Slf4jLogger())
    .decoder(new StringDecoder())
    .encoder(new GsonEncoder())
    .target(WebFeign.class, "http://localhost:10010");


Feign.builder()
    .client(new OkHttpClient()) // 使用 OkHttp
    .logger(new Slf4jLogger())
    .decoder(new StringDecoder())
    .encoder(new GsonEncoder())
    .target(WebFeign.class, "http://localhost:10010");
```

### 5、总结

`Feign` 是一个很好的框架工具，把繁琐的 Http 请求，抽象为以接口加注解的方式实现，也使开发者很好的面向接口编程。在目前微服务盛行的当下，Spring 也对 Feign 进行了封装，即`OpenFeign` ，并且相当流行。这里把最底层、最基础的`Feign`的用法梳理一下，能够更好的理解 Spring 封装的 `OpenFeign`。



1人点赞



[微服务]()





作者：瞎胡扯1
链接：http://events.jianshu.io/p/e4e51e30b111
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。