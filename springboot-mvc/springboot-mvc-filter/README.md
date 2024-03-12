# springboot-mvc-filter

**说明**

Spring Boot提供了一种方便的方式来实现过滤器，使用javax.servlet.Filter接口。过滤器用于拦截HTTP请求和响应，执行预处理和后处理任务。

## Spring Boot中实现过滤器的步骤

1. 创建一个实现javax.servlet.Filter接口的类，该类将包含您的过滤器逻辑。
2. 使用@WebFilter注解对过滤器类进行注解，并指定过滤器应该应用的URL模式。在上面的示例中，urlPatterns = "/*"表示过滤器将应用于所有URL。
3. 可选地，您可以通过实现org.springframework.core.Ordered接口或使用@Order注解来定义过滤器执行的顺序。具有较低排序值的过滤器首先执行。
4. 使用Spring Boot注册过滤器。如果您使用Spring
   Boot的自动配置，只需将@ServletComponentScan注解添加到主应用程序类即可自动注册过滤器。如果您更喜欢Java配置，可以创建一个FilterRegistrationBean并手动注册过滤器。

## FilterRegistrationBean提供了以下一些常用的配置选项

1. setFilter(Filter filter)：设置要注册的过滤器实例。
2. addUrlPatterns(String... urlPatterns)：设置过滤器的URL模式，用于指定过滤器要过滤的URL路径。
3. setOrder(int order)：设置过滤器的执行顺序，数字越小优先级越高。
4. setName(String name)：设置过滤器的名称。
5. setInitParameters(Map<String, String> initParameters)：设置过滤器的初始化参数，可以在过滤器中通过FilterConfig对象获取这些参数。
6. setAsyncSupported(boolean asyncSupported)：设置过滤器是否支持异步操作，默认为false。
7. setDispatcherTypes(DispatcherType... dispatcherTypes)
   ：设置过滤器要过滤的DispatcherType类型，用于控制过滤器的调用时机，默认为DispatcherType.REQUEST。
8. setEnabled(boolean enabled)：设置过滤器是否启用，默认为true。
9. setMatchAfter(boolean matchAfter)：设置过滤器是否在其他过滤器之后执行，默认为false。

## springboot Filter 注册过滤器几种方式

### 1.使用@WebFilter注解

```java
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码放在这里
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 预处理代码放在这里

        // 将请求和响应传递给过滤器链中的下一个过滤器
        chain.doFilter(request, response);

        // 后处理代码放在这里
    }

    @Override
    public void destroy() {
        // 清理代码放在这里
    }
}

```

自动注册过滤器：

```java

@ServletComponentScan // 自动注册过滤器，需配合 @WebFilter 使用
@SpringBootApplication
public class MvcFilterApplication extends SpringBootServletInitializer {
    // ...
}
```

### 2.使用@Bean注解

通过@Bean注解注册的过滤器不需要显式声明过滤器的顺序，Spring Boot会根据@Bean方法的调用顺序自动确定过滤器链的顺序

```java

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter> myFilterRegistration() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter());  // 注：这里是 new MyFilter()
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    public class MyFilter implements Filter {
        // MyFilter的实现
    }
}
```

### 3. 使用FilterRegistrationBean注册单个过滤器

通过FilterRegistrationBean注册的过滤器可以显式地设置过滤器的顺序，确保过滤器按照指定的顺序执行

```java

@Configuration
public class FilterConfig {

    @Bean
    public MyFilter myFilter() {
        return new MyFilter();
    }

    @Bean
    public FilterRegistrationBean<MyFilter> myFilterRegistration() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(myFilter()); // 注：这里是 myFilter()
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    public class MyFilter implements Filter {
        // MyFilter的实现
    }
}
```

### 4.使用FilterRegistrationBean注册多个过滤器

可以使用多个FilterRegistrationBean来注册多个过滤器，并通过设置order属性来指定它们的执行顺序

```java

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> myFilter1Registration() {
        FilterRegistrationBean<MyFilter1> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter1());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // 设置过滤器执行顺序

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> myFilter2Registration() {
        FilterRegistrationBean<MyFilter2> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFilter2());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2); // 设置过滤器执行顺序

        return registrationBean;
    }

    private static class MyFilter1 implements Filter {
        // MyFilter1的实现
    }

    private static class MyFilter2 implements Filter {
        // MyFilter2的实现
    }
}
```

### Filter 执行顺序

1. 优先级：`FilterRegistrationBean 注入的Filter`  > `@WebFilter 注入的Filter`
2. 优先级：`FilterRegistrationBean + setOrder(1)` > `@Component + @Order(2)` > `FilterRegistrationBean + setOrder(3)` > `FilterRegistrationBean`

注：

* @WebFilter 可以设置拦截路径，不支持设置优先级，不能与 @Order(1) 搭配
* @Component 不支持设置拦截路径，支持设置优先级：可以与 @Order(1) 搭配
* FilterRegistrationBean 支持设置拦截路径，支持设置优先级：FilterRegistrationBean.setOrder()

### Filter实现类获取 spring Bean

Filter不能直接获取到spring Bean原因：在Servlet规范中，过滤器是在Servlet容器级别上运行的，而不是在Spring容器中。因此，在过滤器中无法直接使用Spring的依赖注入来获取其他的Bean实例。

方式一：使用WebApplicationContextUtils

```java
public class MyFilter implements Filter {
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        // 通过上下文获取其他的Bean实例
        this.userService = applicationContext.getBean(UserService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 输出业务Bean读取到的数据
        UserDTO userDTO = userService.findById("1");
        System.out.println(userDTO);

        // 将请求和响应传递给过滤器链中的下一个过滤器
        chain.doFilter(request, response);
    }
}
```

方式二：使用DelegatingFilterProxy

```java

@Configuration
public class AppConfig {
    @Bean
    public MyFilter myFilter() {
        return new MyFilter();
    }

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
        FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DelegatingFilterProxy("myFilter"));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```