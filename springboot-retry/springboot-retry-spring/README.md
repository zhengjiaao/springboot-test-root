# springboot-retry-spring

## 注解详解

### @EnableRetry  启用重试，proxyTargetClass属性为true时（默认false），使用CGLIB代理

### @Retryable：标记当前方法会使用重试机制

1. value：指定抛出那些异常才会触发重试(可以配置多个异常类型) 默认为空
2. include：就是value，默认为空，当exclude也为空时，默认所有异常都可以触发重试
3. exclude：指定那些异常不触发重试(可以配置多个异常类型)，默认为空
4. maxAttempts：最大重试次数，默认3次（包括第一次调用）
5. backoff：重试等待策略 默认使用@Backoff注解

### @Backoff：重试回退策略（立即重试还是等待一会再重试）

1. value: 重试的间隔时间默认为1000L，我们设置为2000L
2. delay：重试的间隔时间,就是value
3. maxDelay：重试次数之间的最大时间间隔，默认为0，如果小于delay的设置，则默认为30000L
4. multiplier：delay时间的间隔倍数，默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。

```text
1. 不设置参数时，默认使用FixedBackOffPolicy(固定时间等待策略)，重试等待1000ms
2. 只设置delay时，使用FixedBackOffPolicy，重试等待指定的毫秒数
3. 当设置delay和maxDealy时，重试等待在这两个值之间均态分布
4. 设置delay，maxDealy和multiplier时，使用ExponentialBackOffPolicy(倍数等待策略)
5. 当设置multiplier不等于0时，同时也设置了random时，使用ExponentialRandomBackOffPolicy(随机倍数等待策略)
```

### @Recover标记方法为@Retryable失败时的“兜底”处理方法

1. 传参与@Retryable的配置的value必须一样
2. @Recover标记方法必须要与@Retryable注解的方法“形参”保持一致，第一入参为要重试的异常(
   一定要是@Retryable方法里抛出的异常或者异常父类)，其他参数与@Retryable保持一致，返回值也要一样，否则无法执行！

### @CircuitBreaker：用于标记方法，实现熔断模式。

1. include 指定处理的异常类。默认为空
2. exclude指定不需要处理的异常。默认为空
3. vaue指定要重试的异常。默认为空
4. maxAttempts 最大重试次数。默认3次
5. openTimeout 配置熔断器打开的超时时间，默认5s，当超过openTimeout之后熔断器电路变成半打开状态（只要有一次重试成功，则闭合电路）
6. resetTimeout 配置熔断器重新闭合的超时时间，默认20s，超过这个时间断路器关闭

## 注意实现

1. 使用了@Retryable注解的方法直接实例化调用不会触发重试，要先将实现类实例化到Spring容器中，然后通过注入等方式使用
2. Spring-Retry是通过捕获异常的方式来触发重试的,@Retryable标注方法产生的异常不能使用try-catch捕获，要在方法上抛出异常，不然不会触发重试
3. 重试原则：查询可以进行重试，写操作要慎重，除非业务方支持重入




