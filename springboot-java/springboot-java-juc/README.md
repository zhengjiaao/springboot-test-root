# springboot-java-juc

**使用`java.util.concurrent.*`包下的工具类**

- [多线程和并发面试题 108](https://www.jianshu.com/p/0446ad7d92bb)

## ThreadLocal、InheritableThreadLocal、TransmittableThreadLocal、FastThreadLocal 对比有哪些区别，以及各自的应用场景

在 Java 中，ThreadLocal、InheritableThreadLocal、TransmittableThreadLocal和FastThreadLocal 是用于创建线程本地变量的类，它们之间有一些区别和适用场景。

ThreadLocal：

* ThreadLocal 提供了线程本地变量的基本功能。
* 每个线程都拥有自己独立的变量副本，线程之间的修改不会相互影响。
* 适用于需要在线程之间隔离数据的场景，如线程池中的任务执行、多线程环境下的上下文传递等。

InheritableThreadLocal：

* InheritableThreadLocal 是 ThreadLocal 的一个子类，它允许子线程继承父线程的线程本地变量。
* 子线程会创建自己的变量副本，初始值从父线程那里继承，之后修改不会影响父线程或其他子线程。
* 适用于需要父子线程之间共享数据的场景，如线程池中的任务执行、线程中的任务划分等。

TransmittableThreadLocal：

* TransmittableThreadLocal 是一个扩展自 InheritableThreadLocal 的类，在多线程环境下提供了更灵活的线程本地变量传递能力。
* 通过 TransmittableThreadLocal，线程可以在线程池中的任务切换时，将当前的线程本地变量传递给下一个任务。
* 适用于需要在线程池中的任务之间传递线程本地变量的场景，如跨任务的上下文传递、跨任务的日志追踪等。

FastThreadLocal：

* FastThreadLocal 是一个高性能的线程本地变量实现，相较于标准的 ThreadLocal 实现，它在性能上有所优化。
* FastThreadLocal 的实现方式与 ThreadLocal 略有不同，适用于对性能要求较高的场景。
* 适用于需要高性能线程本地变量的场景，如高并发的网络服务器、框架的性能优化等。

总结：

* ThreadLocal 适用于在多线程环境下实现线程隔离的场景。
* InheritableThreadLocal 适用于父子线程之间共享数据的场景。
* TransmittableThreadLocal 适用于在线程池中跨任务传递线程本地变量的场景。
* FastThreadLocal 适用于对性能要求较高的场景。

选择适当的类取决于你的具体需求和线程模型。根据不同的场景和功能要求，选择合适的线程本地变量类可以提高代码的可维护性和性能。