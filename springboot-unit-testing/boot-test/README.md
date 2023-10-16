# boot-test

**说明**

基于springboot test 单元测试实例。

## 单元测试实例

依赖引入

```xml

<dependencies>

    <!--单元测试-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## @WebMvcTest 注解使用

@WebMvcTest 是 Spring Boot Test 框架中的一个注解，用于针对 Spring MVC 应用程序的单元测试。它提供了一个轻量级的方式来测试控制器类，而无需启动完整的应用程序上下文。

@WebMvcTest 的作用是：

1. 限制测试范围：它只加载和构建与 Spring MVC 相关的组件，例如控制器、异常处理器、拦截器等。这样可以减少测试所需的资源和时间，使测试更加快速和高效。
2. 提供便捷的模拟请求和验证响应的方法：它通过 MockMvc 提供了模拟请求和验证响应的功能。你可以使用 MockMvc 来发送各种类型的
   HTTP 请求（GET、POST、PUT、DELETE 等）并验证返回的响应。

@WebMvcTest 的一般用法如下：

1. 在测试类上添加 @RunWith(SpringRunner.class) 注解以指定使用 Spring Runner 来运行测试。
2. 使用 @WebMvcTest 注解标记测试类，并指定要测试的控制器类。例如，@WebMvcTest(UserController.class) 将测试 UserController
   类。
3. 使用 @Autowired 注解注入 MockMvc 实例，以便在测试中使用。
4. 使用 @MockBean 注解创建模拟的依赖（例如服务、存储库等），以便在测试中模拟它们的行为。
5. 在测试方法中使用 MockMvc 发送请求并验证响应。你可以使用 perform() 方法来发送各种类型的请求，例如 get()、post()、put()
   、delete() 等。然后，使用 andExpect() 方法来验证响应的状态码、内容类型、响应体等。

下面是一个示例：

Service及Controller代码：

```java

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    private String name;
}

@Service
public class UserService {
    public User getUserById(Long id) {
        // 根据id从数据库或其他数据源获取用户信息
        // 省略具体实现
        return new User(id, "John Doe");
    }
}

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
```

单元测试示例：

```java
//@RunWith(SpringRunner.class) //junit5.x不需要添加
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        // 1.模拟userService的返回值
        User mockUser = new User(1L, "Mock User");
        // 2.使用 Mockito.when() 方法模拟了 userService.getUserById() 方法的行为，并返回了预期的 User 对象。
        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);

        // 3.发起GET请求，验证返回结果
        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mock User"));

        // 4.验证userService的方法是否被调用
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
        Mockito.verifyNoMoreInteractions(userService);
    }
}
```

## TestRestTemplate 单元测试实例

TestRestTemplate 是 Spring Framework 提供的一个用于进行集成测试的工具类，它是 RestTemplate 的一个特殊子类。
它提供了一组方便的方法，可以用于发送 HTTP 请求、接收响应，并进行断言和验证。

TestRestTemplate 的主要作用是在单元测试或集成测试中模拟客户端与 RESTful API 之间的交互。
通过使用 TestRestTemplate，你可以在测试中发送 HTTP 请求，获取响应，并对响应进行断言和验证，从而验证你的 API 的行为是否符合预期。

以下是 TestRestTemplate 的一些常见用法：

1. 发送 HTTP 请求：使用 getForEntity()、postForEntity()、put()、delete() 等方法发送 GET、POST、PUT、DELETE 等类型的请求。
2. 接收响应：TestRestTemplate 的请求方法返回一个 ResponseEntity 对象，其中包含响应的状态码、响应头和响应体。
3. 断言和验证：使用断言方法（如 assertThat()）来验证响应的状态码、响应体的内容、响应头等。
4. 设置请求参数和请求头：使用 exchange() 方法可以自定义请求参数、请求头和响应类型等。
5. 模拟请求和响应：TestRestTemplate 可以与其他测试工具（如 MockMvc）结合使用，以模拟请求和响应，进行更加复杂的测试场景。

以下是示例：

```java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 启动一个嵌入式的随机端口的服务器，并加载应用程序上下文
public class UserControllerTest2 {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * 要启动项目，可以通过 UnittestJunitApplication.java 类启动
     */
    @Test
    public void testGetUserById() {
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        User user = response.getBody();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
    }

    @MockBean
    private UserService userService;

    /**
     * 不需要启动项目
     * restTemplate 搭配 MockMvc、@MockBean、Mockito 等，来模拟依赖、验证方法调用和设置方法的行为。
     */
    @Test
    public void testGetUserByIdV2() {
        // 模拟 userService.getUserById() 方法的行为
        User user = new User(1L, "John");
        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        // 发送 GET 请求
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, 1L);

        // 验证响应
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        User responseBody = response.getBody();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getName()).isEqualTo("John");

        // 验证 userService.getUserById() 方法是否被调用
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
        Mockito.verifyNoMoreInteractions(userService);
    }
}
```

## @DataJpaTest 注解实现自动化的数据库测试

引入依赖

```xml

<dependencies>
    <!-- spring-boot-starter-test 单元测试-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- 使用 spring-boot-starter-test 模块来实现自动化的数据库测试-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- 在单元测试中使用嵌入式数据库 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

简单示例

```java

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // 省略构造函数、getter 和 setter 方法
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 可以定义其他自定义查询方法
}
```

单元测试类：

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        // 创建一个用户对象
        User user = new User();
        user.setName("John");

        // 保存用户到数据库
        User savedUser = userRepository.save(user);

        // 从数据库中查询用户
        User retrievedUser = entityManager.find(User.class, savedUser.getId());

        // 验证用户是否保存成功
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo("John");
    }
}
```
在这个示例中，我们使用了 @DataJpaTest 注解来创建一个嵌入式的数据库，并自动加载相关的实体类和存储库。

在 testSaveUser() 方法中，我们创建了一个 User 对象并保存到数据库。
然后，我们使用 TestEntityManager（提供了对嵌入式数据库的访问）来从数据库中查询保存的用户。最后，我们使用断言来验证用户是否保存成功。

一个复杂示例：

假设你有一个简单的博客应用程序，其中包含两个实体类：Post 和 Comment。
Post 表示博客文章，Comment 表示对博客文章的评论。它们之间是一对多的关系。

首先，定义实体类和相关的存储库接口：

```java
// Post.java
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 省略构造函数、getter 和 setter 方法
}

// Comment.java
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 省略构造函数、getter 和 setter 方法
}

// PostRepository.java
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 可以定义其他自定义查询方法
}

// CommentRepository.java
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 可以定义其他自定义查询方法
}
```

单元测试类：

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BlogIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testSavePostWithComments() {
        // 创建一个博客文章对象
        Post post = new Post();
        post.setTitle("Hello, World!");

        // 创建两个评论对象
        Comment comment1 = new Comment();
        comment1.setContent("Great post!");
        comment1.setPost(post);

        Comment comment2 = new Comment();
        comment2.setContent("Nice article!");
        comment2.setPost(post);

        // 添加评论到博客文章
        post.getComments().add(comment1);
        post.getComments().add(comment2);

        // 保存博客文章到数据库
        postRepository.save(post);

        // 从数据库中查询保存的博客文章和评论
        Post savedPost = entityManager.find(Post.class, post.getId());
        List<Comment> savedComments = commentRepository.findAll();

        // 验证博客文章和评论是否保存成功
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Hello, World!");

        assertThat(savedComments).hasSize(2);
        assertThat(savedComments.get(0).getContent()).isEqualTo("Great post!");
        assertThat(savedComments.get(1).getContent()).isEqualTo("Nice article!");
    }
}
```

在这个示例中，我们使用了 @DataJpaTest 注解来创建一个嵌入式的数据库，并自动加载相关的实体类和存储库。

在 testSavePostWithComments() 方法中，我们创建了一个 Post 对象和两个相关的 Comment 对象，并将评论关联到博客文章。
然后，我们保存博客文章到数据库，并使用 TestEntityManager 和存储库接口来从数据库中查询保存的博客文章和评论。最后，我们使用断言来验证数据是否保存成功。
