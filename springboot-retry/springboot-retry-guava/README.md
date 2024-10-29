# springboot-retry-guava

guava-retrying 是一个线程安全的 Java 重试类库，提供了一种通用方法去处理任意需要重试的代码，可以方便灵活地控制重试次数、重试时机、重试频率、停止时机等，并具有异常处理功能.

## 快速开始

```xml

<dependency>
    <groupId>com.github.rholder</groupId>
    <artifactId>guava-retrying</artifactId>
    <version>2.0.0</version>
</dependency>
```

代码实例

```java

// 创建重试器, 设置重试策略, 等待策略, 停止策略, 重试结果过滤器, 监听器, 重试异常处理器
public class RetryExample {
    public static void main(String[] args) throws Exception {
        Retryer<String> retryer = Retrying.builder()
                .retryIfResult(result -> result == null)
                .retryIfExceptionOfType(IOException.class)
                .retryIfRuntimeException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .withWaitStrategy(WaitStrategies.fixedWait(100, TimeUnit.MILLISECONDS))
                .build();

        String result = retryer.call(() -> {
            return "hello world";
        });
    }
}
```