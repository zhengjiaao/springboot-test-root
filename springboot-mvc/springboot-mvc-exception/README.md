# springboot-mvc-exception

**java 异常处理**

全局异常自定义处理：`@ControllerAdvice`和`@ExceptionHandler`是在Spring框架中用于处理全局异常的注解。

## 全局异常注解

`@ControllerAdvice`和`@ExceptionHandler`是在Spring框架中用于处理全局异常的注解。它们的应用场景如下：

@ControllerAdvice注解通常用于定义全局异常处理器类。这个类可以包含多个异常处理方法，用于处理应用程序中抛出的不同类型的异常。它的主要应用场景包括：

1. 全局异常处理：当应用程序中的任何控制器抛出异常时，@ControllerAdvice注解标记的类中的异常处理方法将会被调用，以提供全局的异常处理逻辑。
2. 统一数据处理：您可以在全局异常处理器类中添加方法，用于处理响应数据的统一格式化、封装或处理。例如，您可以将异常信息封装为统一的错误响应对象，并设置正确的HTTP状态码。
3. 全局日志记录：通过在全局异常处理器类中添加方法，您可以实现全局的异常日志记录逻辑。这样可以方便地记录应用程序中的异常信息，以便后续的故障诊断和调试。

@ExceptionHandler注解用于标记异常处理方法。这个方法将处理特定类型的异常，并提供自定义的异常处理逻辑。它的应用场景包括：

1. 局部异常处理：您可以将@ExceptionHandler注解直接应用于控制器类或处理器方法上，用于局部处理特定类型的异常。这样可以在需要的地方精确处理特定的异常，提供定制化的异常处理逻辑。
2.

异常处理链：多个控制器类中的方法可能会抛出相同类型的异常。通过在一个控制器类中定义该异常的@ExceptionHandler方法，可以为整个应用程序创建一个异常处理链。这样，无论在哪个控制器类中抛出该异常，都会被相应的@ExceptionHandler方法捕获并处理。

总而言之，@ControllerAdvice和@ExceptionHandler是Spring框架中用于处理全局异常的强大工具。它们可以帮助您统一处理应用程序中的异常，提供自定义的异常处理逻辑，并简化代码的编写和维护。

## 自定义全局异常实例

在Spring Boot中，您可以使用@ControllerAdvice注解和@ExceptionHandler注解来实现全局异常的自定义处理。

首先，您需要创建一个全局异常处理器类，使用@ControllerAdvice注解标记该类。这个类将处理应用程序中抛出的所有异常。

```java

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // 处理异常逻辑
        // 返回自定义错误信息
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    // 可以添加其他的异常处理方法
}
```

在上面的示例中，handleException方法使用@ExceptionHandler注解来标记处理Exception类型的异常。您可以根据需要添加其他的异常处理方法，每个方法可以处理不同类型的异常。

在异常处理方法中，您可以执行特定的逻辑来处理异常。例如，您可以记录异常、发送警报、返回自定义错误消息等。

最后，确保您的全局异常处理器类被扫描到，并且在应用程序中起作用。您可以将该类放在主应用程序类所在的包或其子包中，或者使用@ComponentScan注解指定扫描的包。

这样，当应用程序中抛出异常时，全局异常处理器将会捕获并处理它们，根据您定义的逻辑返回自定义的错误响应。
