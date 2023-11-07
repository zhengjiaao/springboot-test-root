# springboot-guava


**说明**

Guava 项目包含几个在我们基于java项目中依赖的Google的核心类库：集合（collections）,缓存（caching），原始类型支持，并发库，常用注解，字符串处理，I/O等等。

集合：Guava对JDK集合生态系统的扩展。这些是Guava最受欢迎最成熟的部分。

## Guava库的主要功能和模块

1. 基本工具（Basic Utilities）：
   * Preconditions：前置条件检查工具，用于参数校验和前置条件的断言。
   * Objects：用于处理对象的工具类，包括对象的比较、hashCode计算等。
   * Strings：字符串处理工具，包括字符串的拆分、连接、填充等。
   * MoreObjects：提供了更多的对象处理方法，如toStringHelper等。
   * Throwables：异常处理工具，包括异常的包装、栈轨迹的提取等。
2. 集合（Collections）：
   * ImmutableCollections：不可变集合，提供了线程安全且不可变的集合实现。
   * NewCollections：提供了一些新的集合类型，如Multiset、Multimap、Table等。
   * Lists、Sets、Maps：提供了对List、Set、Map等集合类型的增强工具和实现。
3. 缓存（Caches）：
   * CacheBuilder：缓存构建器，用于创建本地缓存。
   * CacheLoader：缓存加载器，用于定义缓存加载的行为。
   * LoadingCache：带有自动加载功能的缓存接口。
4. 函数式编程（Functional Programming）：
   * Functional：函数式编程工具，包括函数接口、函数组合、函数应用等。
   * Predicates：谓词工具，用于条件判断和过滤。
   * Suppliers：供应者工具，用于生成对象的工具类。
   * Functions：函数工具，用于转换、组合和操作函数。
5. 并发（Concurrency）：
   * ListenableFuture：可监听的Future，用于异步计算结果的处理。
   * Futures：Future工具，用于处理异步计算的结果。
   * RateLimiter：速率限制工具，用于控制访问速率。
   * Striped：锁分段工具，用于并发操作的分段锁。
6. IO：
   * Files：文件工具，用于文件和目录的操作。
   * Resources：资源工具，用于处理类路径和文件系统资源的读取。
7. 字符串处理（String Handling）：
   * CharMatcher：字符匹配器，用于字符的查找、替换、删除等操作。
   * CaseFormat：字符串格式转换工具，用于不同命名风格之间的转换。
8. 数学运算（Math Utilities）：
   * IntMath、LongMath：整数和长整数运算工具。
   * DoubleMath：浮点数运算工具。
   * BigIntegerMath、BigIntegerMath：大整数运算工具。
9. 反射（Reflection）：
   * TypeToken：类型令牌，用于操纵和解析泛型类型。
   * Invokable：可调用对象的包装器，用于反射调用。
10. 事件总线（EventBus）：事件发布和订阅的机制，用于解耦组件之间的通信。

## 常见的Guava工具类和函数的整理

1. Preconditions（前置条件）：提供了一组用于进行参数校验和前置条件检查的静态方法，可以方便地检查参数的有效性。
2. CharMatcher（字符匹配器）：提供了对字符的匹配和处理工具，包括字符的查找、替换、删除等操作。
3. Splitter（字符串拆分器）和Joiner（字符串连接器）：提供了对字符串的拆分和连接工具，支持自定义分隔符和处理选项。
4. Ordering（排序器）：提供了对对象的排序工具，支持自定义排序规则、链式排序以及对null值的处理。
5. Range（范围）：提供了对范围的处理工具，包括范围的比较、相交、合并等操作。
6. CacheBuilder（缓存构建器）：提供了构建缓存的构建器模式，支持设置缓存大小、过期策略、回收策略等。
7. EventBus（事件总线）：提供了事件总线的实现，用于简化组件之间的事件发布和订阅。
8. Optional（可选值）：提供了一种方便的方式来处理可能为null的值，避免了空指针异常，同时提供了一些便利的操作方法。
9. Throwables（异常工具）：提供了对异常的处理工具，包括异常的包装、栈轨迹的提取、异常链的操作等。
10. Stopwatch（计时器）：提供了简便的计时器工具，用于测量代码的执行时间。
11. ComparisonChain（比较链）：提供了一种简洁的方式来进行多个字段的比较排序，避免了繁琐的比较语句。
12. Enums（枚举工具）：提供了对枚举类型的处理工具，包括获取枚举常量、解析字符串为枚举值等。
13. Files（文件工具）：提供了对文件和目录的操作工具，包括递归列出文件、文件拷贝、移动等。
14. Resources（资源工具）：提供了对类路径和文件系统资源的访问工具，可以方便地读取和处理各种资源。
15. Primitives（基本类型工具）：提供了对Java基本类型的操作和处理，如整数、浮点数、布尔值等。
16. Retryer（重试器）：提供了自定义重试策略的实现，方便处理需要重试的操作。
17. Network（网络工具）：提供了对网络编程的支持，包括IP地址处理、网络连接、Socket工具等。
18. XML（XML工具）：提供了对XML的处理工具，包括XML解析、生成、转换等。
19. Guava Graph：提供了图（Graph）数据结构的实现，支持有向图、无向图、带权图等，以及相关的遍历和算法。
20. Guava Cache：提供了更高级的缓存实现，支持自定义缓存策略、过期机制、并发控制等。
21. Guava EventBus：提供了事件总线的实现，用于简化组件之间的事件发布和订阅。
22. Guava Math：提供了更丰富的数学计算工具，包括数值范围、分数、二进制运算等。
23. Guava Network：提供了对网络编程的支持，包括IP地址处理、网络连接、Socket工具等。
24. Guava Reflection：提供了更便捷的反射操作工具，包括动态代理、类型解析、注解处理等。
25. Guava Strings：提供了更多字符串处理工具，如Unicode处理、字符集转换、正则表达式等。
26. Guava Hashing：提供了更多散列（Hash）算法的实现，包括MD5、SHA1、MurmurHash等。
27. Guava Primitives：提供了对Java基本类型的操作和处理，如整数、浮点数、布尔值等。
28. Guava XML：提供了对XML的处理工具，包括XML解析、生成、转换等。
29. Guava Multimap：提供了一种键映射到多个值的数据结构，简化了处理一对多关系的操作。
30. Guava Table：提供了类似于二维表格的数据结构，支持在两个键的维度上进行数据访问和操作。
31. Guava BiMap：提供了一种双向映射的数据结构，可以根据键或值进行快速的双向查找。
32. Guava RangeSet：提供了对范围集合的操作，包括范围的合并、交集、差集等。
33. Guava ClassPath：提供了对类路径资源的扫描和读取，可以获取类路径下的所有类、资源等信息。
34. Guava Enums：提供了对枚举类型的处理工具，包括获取枚举常量、解析字符串为枚举值等。
35. Guava Stopwatch：提供了简便的计时器工具，用于测量代码的执行时间。
36. Guava Throwables：提供了对异常的处理工具，包括异常的包装、栈轨迹的提取、异常链的操作等。
37. Guava Files：提供了对文件和目录的操作工具，包括递归列出文件、文件拷贝、移动等。
38. Guava Retryer：提供了自定义重试策略的实现，方便处理需要重试的操作。
39. Guava Preconditions（前置条件）：提供了一组用于进行参数校验和前置条件检查的静态方法，可以方便地检查参数的有效性。
40. Guava CharMatcher（字符匹配器）：提供了对字符的匹配和处理工具，包括字符的查找、替换、删除等操作。
41. Guava Splitter（字符串拆分器）和Joiner（字符串连接器）：提供了对字符串的拆分和连接工具，支持自定义分隔符和处理选项。
42. Guava Ordering（排序器）：提供了对对象的排序工具，支持自定义排序规则、链式排序以及对null值的处理。
43. Guava Range（范围）：提供了对范围的处理工具，包括范围的比较、相交、合并等操作。
44. Guava CacheBuilder（缓存构建器）：提供了构建缓存的构建器模式，支持设置缓存大小、过期策略、回收策略等。
45. Guava EventBus（事件总线）：提供了事件总线的实现，用于简化组件之间的事件发布和订阅。
46. Guava Optional（可选值）：提供了一种方便的方式来处理可能为null的值，避免了空指针异常，同时提供了一些便利的操作方法。
47. Guava Resources（资源工具）：提供了对类路径和文件系统资源的访问工具，可以方便地读取和处理各种资源。
48. Guava Collections（集合工具）：提供了对集合的处理工具，包括集合的创建、合并、过滤、转换等操作。
49. Guava Iterables（可迭代工具）：提供了对可迭代对象的处理工具，包括迭代器的操作、元素的查找、转换等。







