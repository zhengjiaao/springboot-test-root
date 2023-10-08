# 3-proxy-pattern

**说明**

代理模式（Proxy Pattern）：代理模式是一种结构型设计模式，它允许通过创建一个代理对象来控制对另一个对象的访问。代理对象充当了客户端和目标对象之间的中介，客户端通过代理对象与目标对象进行交互。

在代理模式中，有三个关键角色：

1. 抽象主题（Subject）：定义了代理对象和目标对象之间的共同接口，客户端通过该接口访问目标对象。
2. 具体主题（Real Subject）：也称为目标对象，它是真正需要被访问的对象。具体主题实现了抽象主题定义的接口，代理对象通过调用具体主题的方法来实现客户端的请求。
3. 代理（Proxy）：代理对象实现了抽象主题的接口，并持有一个对具体主题的引用。它可以在调用具体主题的方法之前或之后执行一些额外的操作，以控制对具体主题的访问。

代理模式的工作原理如下：

1. 客户端通过与代理对象交互，向代理对象发出请求。
2. 代理对象接收到请求后，可以在调用具体主题之前或之后执行一些额外的操作，例如权限控制、缓存、日志记录等。
3. 代理对象将请求转发给具体主题，并接收具体主题的执行结果。
4. 代理对象将执行结果返回给客户端，作为对客户端请求的响应。

## 代理模式的主要优缺点

代理模式的主要优点包括：

* 远程代理：代理对象可以代表远程对象，使得客户端可以通过代理对象访问位于不同地址空间的远程对象，实现分布式系统的透明访问。
* 虚拟代理：代理对象可以代表昂贵或复杂的对象，延迟真正创建或加载目标对象，从而提高系统的性能。
* 安全代理：代理对象可以控制对目标对象的访问权限，实现安全性的控制。
* 简化客户端：代理对象可以隐藏目标对象的复杂性，为客户端提供简化的接口，降低了客户端的复杂度。

代理模式的主要缺点包括：

* 增加了复杂性：引入代理对象会增加系统的复杂性。除了原本的目标对象，现在还需要管理代理对象和客户端之间的交互，这可能导致代码的理解和维护变得更加困难。
* 性能损耗：由于代理对象需要在目标对象的方法调用前后执行额外的操作，如权限验证、日志记录等，这可能导致性能的损耗。特别是在频繁调用的场景下，代理模式可能会引入额外的开销。
* 可能引入额外的依赖关系：引入代理对象可能会增加系统的依赖关系。客户端不再直接与目标对象交互，而是通过代理对象进行间接访问。这意味着客户端需要了解和依赖于代理对象的存在，增加了代码的耦合性。
* 可能破坏透明性：代理模式可能破坏对客户端的透明性。客户端可能无法感知到自己实际上正在与代理对象而不是真正的目标对象进行交互，这可能导致客户端对系统行为的误解或错误的预期。
* 可能引起线程安全问题：如果在多线程环境下使用代理模式，需要特别注意线程安全性。代理对象和目标对象的共享状态可能引发并发访问的问题，需要额外的同步措施来确保线程安全。

尽管代理模式存在一些缺点，但在特定的应用场景下，仍然可以提供许多有价值的好处。在使用代理模式时，需要仔细权衡其优点和缺点，并根据实际需求进行设计和实现。

## 代理模式常见的应用场景

代理模式的一些常见应用场景包括：远程代理、虚拟代理、安全代理、缓存代理、日志记录代理等。

* 远程代理：代理模式可以用于实现远程代理，即在不同的地址空间中代理对象和客户端之间的通信。客户端通过代理对象发送请求，代理对象将请求转发给远程对象，并将结果返回给客户端。这种模式常用于分布式系统中，实现跨网络的对象访问。
* 虚拟代理：代理模式可以用于实现虚拟代理，即延迟加载或创建昂贵或复杂的对象。代理对象在需要时才实例化真正的目标对象，并在目标对象加载完成之前，代理对象可以执行一些额外的操作，如显示加载进度、缓存等。
* 安全代理：代理模式可以用于实现安全代理，即控制对目标对象的访问权限。代理对象可以在调用目标对象的方法之前进行权限检查，确保只有具有足够权限的客户端可以访问目标对象。
* 缓存代理：代理模式可以用于实现缓存代理，以提高系统的性能。代理对象可以在调用目标对象的方法之前检查缓存是否存在请求结果，并在缓存中找到结果时直接返回，避免了频繁的计算或查询操作。
* 日志记录代理：代理模式可以用于实现日志记录代理，即在调用目标对象的方法之前或之后记录日志信息。代理对象可以用于收集和记录系统的运行日志，方便后续的排查和分析。
* 限制访问代理：代理模式可以用于实现限制访问代理，即限制对目标对象的访问次数或频率。代理对象可以记录对目标对象的访问次数，并在达到限制条件时拒绝进一步的访问。

## 代理模式简单示例

### 计算器操作时添加日志记录

下面是一个简单的示例，演示了代理模式的应用:

> 假设我们有一个计算器接口 Calculator，定义了两个方法 add 和 subtract，我们希望在执行计算操作时添加一些额外的日志记录。

首先，定义 Calculator 接口：

```java
public interface Calculator {
    int add(int a, int b);

    int subtract(int a, int b);
}
```

然后，我们实现目标对象 CalculatorImpl，实现了 Calculator 接口的方法：

```java
public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}
```

接下来，我们创建代理对象 CalculatorProxy，它实现了 Calculator 接口，并在调用目标对象的方法之前和之后添加了日志记录：

```java
public class CalculatorProxy implements Calculator {
    private Calculator calculator;

    public CalculatorProxy(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public int add(int a, int b) {
        System.out.println("Before add operation");
        int result = calculator.add(a, b);
        System.out.println("After add operation");
        return result;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("Before subtract operation");
        int result = calculator.subtract(a, b);
        System.out.println("After subtract operation");
        return result;
    }
}
```

最后，我们可以使用代理对象进行计算，并查看日志记录的效果：

```java
public class Main {
    public static void main(String[] args) {
        Calculator calculator = new CalculatorImpl();
        Calculator proxy = new CalculatorProxy(calculator);

        int sum = proxy.add(5, 3);
        System.out.println("Sum: " + sum);

        int difference = proxy.subtract(10, 6);
        System.out.println("Difference: " + difference);
    }
}
```

当我们运行上述代码时，将得到以下输出：

```text
Before add operation
After add operation
Sum: 8
Before subtract operation
After subtract operation
Difference: 4
```

可以看到，代理对象在执行目标对象的方法之前和之后添加了日志记录，实现了代理模式的效果。这个示例展示了代理模式如何在调用目标对象的方法前后进行额外操作，并且对客户端透明，使其无需直接与目标对象交互。

### 远程代理模式实例

一个更复杂的远程代理模式示例，展示了如何使用远程代理模式实现远程方法调用和数据传输。

> 假设我们有一个电子商务系统，其中包含客户端（购物者）、远程服务器和商品接口。客户端希望能够通过远程调用来获取商品信息并执行购买操作。

首先，我们定义商品接口 Product，该接口包含获取商品信息和购买商品的方法：

```java
public interface Product {
    String getName();

    double getPrice();

    void buy();
}
```

然后，我们实现商品类 ProductImpl，该类实现了 Product 接口的方法：

```java
public class ProductImpl implements Product {
    private String name;
    private double price;

    public ProductImpl(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void buy() {
        System.out.println("购买商品：" + name);
    }
}
```

接下来，我们创建远程代理类 RemoteProductProxy，该类实现了 Product 接口，并通过网络与远程服务器进行通信：

```java
public class RemoteProductProxy implements Product {
    private String serverUrl;

    public RemoteProductProxy(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String getName() {
        // 发送远程调用请求获取商品名称
        String request = "GET /product/name";
        String response = sendRequest(request);
        return response;
    }

    @Override
    public double getPrice() {
        // 发送远程调用请求获取商品价格
        String request = "GET /product/price";
        String response = sendRequest(request);
        return Double.parseDouble(response);
    }

    @Override
    public void buy() {
        // 发送远程调用请求购买商品
        String request = "POST /product/buy";
        sendRequest(request);
    }

    private String sendRequest(String request) {
        // 创建网络连接并发送请求到远程服务器
        // ...
        // 接收并解析远程服务器的响应
        // ...
        return "Response from server";
    }
}
```

在 RemoteProductProxy 类中，我们通过网络发送请求到远程服务器，并接收和解析服务器的响应。通过远程代理对象，客户端可以调用
getName()、getPrice() 和 buy() 方法，实现远程调用和商品操作。

最后，我们可以在客户端中使用远程代理对象进行远程商品操作：

```java
public class Client {
    public static void main(String[] args) {
        Product product = new RemoteProductProxy("http://example.com");
        System.out.println("Product Name: " + product.getName());
        System.out.println("Product Price: " + product.getPrice());
        product.buy();
    }
}
```

在上述示例中，客户端通过 RemoteProductProxy 对象调用远程商品的方法，而无需了解底层的网络通信细节。远程代理模式实现了远程调用和数据传输的透明化，使得客户端可以像调用本地对象一样调用远程对象的方法。

请注意，示例中的网络通信部分只是简化的模拟，实际的远程调用可能涉及更复杂的协议和通信机制。

### 代理模式在java中的应用

在Java中，代理模式有两种主要的实现方式：静态代理和动态代理。

1. 静态代理： 静态代理是通过手动编写代理类来实现的，代理类和目标类在编译时就已经确定。
2. 动态代理： 动态代理是在运行时生成代理类的方式，它利用Java的反射机制动态地创建代理对象。

以下是一个简单的静态代理的示例：

```java
// 定义接口
interface Image {
    void display();
}

// 实际的目标类
class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading image: " + filename);
    }

    @Override
    public void display() {
        System.out.println("Displaying image: " + filename);
    }
}

// 代理类
class ImageProxy implements Image {
    private RealImage realImage;
    private String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}

// 使用示例
public class Client {
    public static void main(String[] args) {
        Image image = new ImageProxy("image.jpg");
        image.display();
    }
}
```

在上述示例中，Image 接口是目标类和代理类的共同接口，RealImage 是实际的目标类，ImageProxy 是代理类。代理类在 display()
方法中创建并管理真实的目标类对象。客户端通过代理对象调用目标对象的方法，代理对象在必要时才创建真实的目标对象，并在调用目标对象的方法之前或之后进行额外的操作。

以下是一个简单的动态代理的示例：

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 定义接口
interface Image {
    void display();
}

// 实际的目标类
class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading image: " + filename);
    }

    @Override
    public void display() {
        System.out.println("Displaying image: " + filename);
    }
}

// 动态代理处理器
class ImageProxyHandler implements InvocationHandler {
    private Object realObject;

    public ImageProxyHandler(Object realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("Before method invocation");
        result = method.invoke(realObject, args);
        System.out.println("After method invocation");
        return result;
    }
}

// 使用示例
public class Client {
    public static void main(String[] args) {
        Image realImage = new RealImage("image.jpg");
        Image proxyImage = (Image) Proxy.newProxyInstance(
                realImage.getClass().getClassLoader(),
                realImage.getClass().getInterfaces(),
                new ImageProxyHandler(realImage));
        proxyImage.display();
    }
}
```

在上述示例中，`Image`接口和 `RealImage` 类与静态代理的示例相同。`ImageProxyHandler`
是动态代理的处理器，它实现了 `InvocationHandler` 接口，并在 `invoke()`
方法中实现了额外的操作。在客户端，通过调用 `Proxy.newProxyInstance()` 方法动态地创建代理对象，并将代理对象传递给处理器。代理对象在调用方法时会自动调用处理器的
invoke() 方法，从而实现了动态代理的功能。

这里只是简单示例了Java中代理模式的实现，实际应用中可能还涉及更多的细节和复杂性。无论是静态代理还是动态代理，代理模式都可以帮助在不同的场景中实现对象的封装和控制，提供了额外的灵活性和功能扩展性。你可以根据具体的需求和场景选择适合的代理模式实现方式。

### 代理模式在spring中应用实例

在Spring框架中，代理模式广泛应用于AOP（面向切面编程）的实现。通过AOP，可以将横切关注点（如日志记录、事务管理等）与核心业务逻辑分离，提高了代码的模块性和可重用性。Spring提供了两种主要的代理模式实现：基于JDK动态代理和基于CGLIB的动态代理。

下面是Spring中应用代理模式的示例：

1. 基于JDK动态代理：

```java
// 目标接口
interface UserService {
    void addUser(String username);
}

// 目标类
class UserServiceImpl implements UserService {
    @Override
    public void addUser(String username) {
        System.out.println("Adding user: " + username);
    }
}

// 切面类
class LoggingAspect implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before method execution");
        Object result = invocation.proceed();
        System.out.println("After method execution");
        return result;
    }
}

// 配置文件中使用代理
@Configuration
public class AppConfig {
    //    @Bean // todo 会与动态代理bean冲突？
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public ProxyFactoryBean userServiceProxy() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(userService());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("addUser");

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(loggingAspect());

        proxyFactoryBean.addAdvisor(advisor);

        return proxyFactoryBean;
    }
}

// 使用示例
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.addUser("John");
    }
}
```

上述示例中，`UserServiceImpl` 是目标类，实现了 `UserService` 接口。`LoggingAspect` 是切面类，实现了 `MethodInterceptor`
接口，通过在方法前后添加日志输出实现了额外的功能。在配置文件中，通过 `ProxyFactoryBean`
将目标类和切面类结合起来，生成动态代理对象。在使用时，通过Spring容器获取代理对象，并调用方法。

2. 基于CGLIB的动态代理：

```java
// 目标类
class UserService {
    public void addUser(String username) {
        System.out.println("Adding user: " + username);
    }
}

// 切面类
class LoggingAspect implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before method execution");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After method execution");
        return result;
    }
}

// 配置类中使用代理
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}

// 使用示例
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.addUser("John");
    }
}
```

在上述示例中，`UserService` 是目标类，`LoggingAspect` 是切面类，实现了 `MethodInterceptor` 接口。在配置类中，通过
`@EnableAspectJAutoProxy` 注解启用了`AOP`代理的自动配置。Spring框架会在运行时使用`CGLIB`生成目标类的子类作为代理对象，并在方法前后添加切面的逻辑。

无论是基于`JDK`动态代理还是基于`CGLIB`
的动态代理，Spring都提供了方便的支持和集成，使得在应用中使用代理模式更加简单和灵活。通过AOP，可以将横切关注点与核心业务逻辑解耦，提高了代码的可维护性和可扩展性。
