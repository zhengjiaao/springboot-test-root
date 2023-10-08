# 1-adapter-pattern

**说明**

适配器模式（Adapter Pattern）：适配器模式是一种结构型设计模式，用于将一个类的接口转换成客户端所期望的另一个接口。适配器模式允许不兼容的类能够合作，使得原本由于接口不匹配而无法一起工作的类能够协同工作。

适配器模式通常涉及三个角色：

* 目标接口（Target Interface）：客户端期望的接口，它定义了客户端所需的方法或操作。
* 适配器（Adapter）：适配器是一个实现了目标接口的类，它包装了一个被适配者对象，并将客户端请求转换为适配者能够理解的形式。
* 被适配者（Adaptee）：被适配者是需要被适配的类或对象，它定义了不兼容目标接口的方法或操作。

适配器模式的工作过程如下：

1. 客户端通过目标接口调用适配器的方法。
2. 适配器接收到客户端的请求后，将请求转换为适配者能够处理的形式。
3. 适配器将转换后的请求传递给适配者，并获取适配者的结果。
4. 适配器将适配者的结果转换为客户端期望的形式，并返回给客户端。

## 适配器模式的优缺点

适配器模式的优点包括：

1. 对象适配器和类适配器：适配器模式可以通过对象适配器和类适配器两种方式实现。对象适配器使用组合关系，可以适配多个适配者对象，而类适配器使用继承关系，可以适配一个适配者类。这样可以根据需要选择适配器的实现方式。
2. 解耦客户端和适配者：通过适配器模式，客户端和适配者之间的耦合度降低。客户端只需要与目标接口进行交互，而不需要直接与适配者交互。
3. 重用现有功能：适配器模式可以重用现有的适配者类，使其能够与其他不兼容的接口进行协作，而无需修改适配者的代码。

然而，适配器模式也有一些缺点：

1. 过多的适配器类：如果系统中存在多个不兼容的接口，可能需要创建多个适配器类来进行适配，这可能会导致类的数量增加。
2. 不支持所有适配者功能：适配器模式可能无法完全适配所有适配者的功能，特别是当适配者接口与目标接口差异较大时。

适配器模式在实际应用中经常用于将现有的类或接口与新系统进行集成，或者在不修改现有代码的情况下使用第三方库。它能够提供接口的统一性，简化客户端的代码，同时保持现有功能的完整性。

## 适配器模式常见的应用场景

1. 第三方库的集成：当我们需要使用一个第三方库或组件，但其接口与我们的系统接口不兼容时，可以使用适配器模式进行适配。适配器将第三方库的接口转换为符合我们系统要求的接口，使得我们能够无缝地集成和使用该库。
2. 数据格式的转换：当系统需要将一种数据格式转换为另一种数据格式时，适配器模式可以派上用场。例如，将XML格式转换为JSON格式或将不同数据库的查询结果转换为统一的数据结构。
3. 接口升级和兼容性处理：在软件演进过程中，接口的变更是常见的情况。适配器模式可以用于处理接口的升级和兼容性问题。通过适配器，我们可以保持旧有接口的兼容性，同时引入新接口的功能。
4. 日志记录：适配器模式可以用于将不同日志框架的接口进行适配，使得应用程序可以无缝地切换使用不同的日志框架，而无需修改现有的日志记录代码。
5. 硬件设备的适配：在与硬件设备进行交互时，可能会遇到不同设备之间的接口不兼容的情况。适配器模式可以用于将不同硬件设备的接口适配为统一的接口，以便系统能够与各种设备进行交互。
6. 旧系统的重构：当我们需要对旧系统进行重构或升级时，适配器模式可以用于保留旧系统的接口，并适配新系统的接口。这样可以让新系统能够与旧系统进行交互，逐步替换旧系统的功能。

## 适配器模式的简单实例

### 将英国插头适配到中国插座实例

以下是一个在Java中使用适配器模式的简单实例，用于将英国插头适配到中国插座：

首先，我们有一个中国插座接口定义如下：

```java
public interface ChinaSocket {
    void insertChinaPlug();
}
```

然后，我们有一个英国插头类，其接口与中国插座接口不兼容：

```java
public class UKPlug {
    public void insertUKPlug() {
        System.out.println("插上英国插头");
    }
}
```

接下来，我们创建一个适配器类实现中国插座接口，同时持有英国插头的实例，并将英国插头的方法适配到中国插座接口：

```java
public class UKPlugAdapter implements ChinaSocket {
    private UKPlug ukPlug;

    public UKPlugAdapter(UKPlug ukPlug) {
        this.ukPlug = ukPlug;
    }

    @Override
    public void insertChinaPlug() {
        ukPlug.insertUKPlug();
        System.out.println("使用适配器将英国插头适配到中国插座");
    }
}
```

最后，我们可以使用适配器将英国插头适配到中国插座，并进行插拔操作：

```java
public class Main {
    public static void main(String[] args) {
        UKPlug ukPlug = new UKPlug();
        ChinaSocket chinaSocket = new UKPlugAdapter(ukPlug);

        chinaSocket.insertChinaPlug();
    }
}
```

输出结果将是：

```text
插上英国插头
使用适配器将英国插头适配到中国插座
```

通过适配器模式，我们成功地将英国插头适配到了中国插座，实现了不兼容接口之间的协同工作。适配器隐藏了底层的兼容性细节，使得使用者只需要与适配器进行交互，而无需关注不兼容接口的具体实现。

### Spring框架中使用控制器适配器

一个具体的例子是使用RequestMappingHandlerAdapter来适配处理基于注解的控制器方法。

首先，我们定义一个控制器类，并使用@Controller和@RequestMapping注解来标记它：

```java

@Controller
@RequestMapping("/users")
public class UserController {
    @RequestMapping("/{id}")
    public String getUser(@PathVariable("id") int id) {
        // 处理获取用户信息的逻辑
        return "userPage";
    }
}
```

在上述例子中，`UserController`类是一个控制器，并使用`@RequestMapping("/users")`注解标记根路径。`getUser()`
方法使用`@RequestMapping("/{id}")`注解标记了路径参数，指定了请求的URL模式。

接下来，我们需要配置控制器适配器来适配RequestMappingHandlerAdapter。这可以通过Spring配置文件（如XML配置）或使用注解（如Java代码配置）来完成。

Java代码配置示例：

```java

@Configuration
@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter {
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }
}
```

在配置完成后，`Spring`框架会自动识别并使用`RequestMappingHandlerAdapter`作为控制器适配器。

最后，当有请求发送到路径`/users/{id}`时，`RequestMappingHandlerAdapter`会适配并调用`UserController`中的`getUser()`
方法，传递请求的路径参数。

控制器适配器负责解析请求的URL和参数，调用相应的控制器方法，并处理返回结果。在这个例子中，适配器将根据请求的URL路径参数调用`getUser()`
方法，并将结果返回给用户。

### 在Java类中使用适配器模式的例子

以下是一个在Java类中使用适配器模式的例子：

假设我们有两个不同的日志记录器接口：`Logger`和`AdvancedLogge`r。`Logger`接口提供了最基本的日志记录功能，而`AdvancedLogger`
接口则提供了更高级的日志记录功能，例如日志的级别、时间戳等。

现在，我们希望在一个应用程序中使用`AdvancedLogger`接口，但是我们只有一个已经实现了`Logger`
接口的日志记录器类。这时，我们可以使用适配器模式来适配已有的`Logger`接口实现到`AdvancedLogger`接口。

以下是一个示例代码：

```java
// Logger接口
public interface Logger {
    void log(String message);
}

// 已有的Logger接口实现
public class SimpleLogger implements Logger {
    public void log(String message) {
        System.out.println("Logging: " + message);
    }
}

// AdvancedLogger接口
public interface AdvancedLogger {
    void logInfo(String message);

    void logError(String message);
}

// 适配器类：将Logger适配到AdvancedLogger接口
public class LoggerAdapter implements AdvancedLogger {
    private Logger logger;

    public LoggerAdapter(Logger logger) {
        this.logger = logger;
    }

    public void logInfo(String message) {
        logger.log("[INFO] " + message);
    }

    public void logError(String message) {
        logger.log("[ERROR] " + message);
    }
}
```

客户端代码:

```java
// 客户端代码
public class Client {
    public static void main(String[] args) {
        Logger logger = new SimpleLogger();
        AdvancedLogger advancedLogger = new LoggerAdapter(logger);

        advancedLogger.logInfo("This is an information message.");
        advancedLogger.logError("This is an error message.");
    }
}
```

在上述示例中，我们有一个`Logger`接口和一个已经实现了该接口的`SimpleLogger`类。然后，我们有一个`AdvancedLogger`
接口，它提供了更高级的日志记录功能。

`LoggerAdapter`是适配器类，它实现了`AdvancedLogger`接口，并将已有的`Logger`接口实现适配到其中。适配器类内部持有一个`Logger`
对象，并在其方法中调用`Logger`接口相应的方法，同时进行额外的处理（在日志消息前添加日志级别）。

在客户端代码中，我们创建一个`SimpleLogger`对象并将其适配到`AdvancedLogger`接口上。然后，我们可以通过`AdvancedLogger`
接口调用更高级的日志记录方法，实际上会通过适配器调用`Logger`接口的方法并进行适配。

通过使用适配器模式，我们可以在已有的接口实现和需要的接口之间进行适配，使得它们能够协同工作。在这个例子中，适配器将已有的`Logger`
接口适配到`AdvancedLogger`接口，提供了更高级的日志记录功能，而不需要修改已有的`Logger`接口实现。
