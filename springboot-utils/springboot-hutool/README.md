# springboot-hutool

[hutool.cn 官网](https://hutool.cn/)

**说明**

Hutool是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。

Hutool中的工具方法来自每个用户的精雕细琢，它涵盖了Java开发底层代码中的方方面面，它既是大型项目开发中解决小问题的利器，也是小型项目中的效率担当；

Hutool是项目中“util”包友好的替代，它节省了开发人员对项目中公用类和公用工具方法的封装时间，使开发专注于业务，同时可以最大限度的避免封装不完善带来的bug。

## Hutool 包含组件

一个Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类，同时提供以下组件：

| 模块                 | 介绍                                             |
|--------------------|------------------------------------------------|
| hutool-aop         | JDK动态代理封装，提供非IOC下的切面支持                         |
| hutool-bloomFilter | 布隆过滤，提供一些Hash算法的布隆过滤                           |
| hutool-cache       | 简单缓存实现                                         |
| hutool-core        | 核心，包括Bean操作、日期、各种Util等                         |
| hutool-cron        | 定时任务模块，提供类Crontab表达式的定时任务                      |
| hutool-crypto      | 加密解密模块，提供对称、非对称和摘要算法封装                         |
| hutool-db          | JDBC封装后的数据操作，基于ActiveRecord思想                  |
| hutool-dfa         | 基于DFA模型的多关键字查找                                 |
| hutool-extra       | 扩展模块，对第三方封装（模板引擎、邮件、Servlet、二维码、Emoji、FTP、分词等） |
| hutool-http        | 基于HttpUrlConnection的Http客户端封装                  |
| hutool-log         | 自动识别日志实现的日志门面                                  |
| hutool-script      | 脚本执行封装，例如Javascript                            |
| hutool-setting     | 功能更强大的Setting配置文件和Properties封装                 |
| hutool-system      | 系统参数调用封装（JVM信息等）                               |
| hutool-json        | JSON实现                                         |
| hutool-captcha     | 图片验证码实现                                        |
| hutool-poi         | 针对POI中Excel和Word的封装                            |
| hutool-socket      | 基于Java的NIO和AIO的Socket封装                        |
| hutool-jwt         | JSON Web Token (JWT)封装实现                       |

## 引入方式

在父模块中加入：

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-bom</artifactId>
            <version>${hutool.version}</version>
            <type>pom</type>
            <!-- 注意这里是import -->
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

```

在子模块中就可以引入自己需要的模块了：

```xml

<dependencies>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-http</artifactId>
    </dependency>
</dependencies>

```


