# springboot-bigdata-hadoop

**说明**

Hadoop Java 提供了一些常用的依赖，这些依赖可以通过 Maven 或其他构建工具来管理。

## 安装 Hadoop 环境

### Windows 安装 Hadoop 环境

Windows 安装 Hadoop 环境，并配置环境变量，不然，没法启动依赖hadoop的相关应用程序。

下载适合 Windows版本的Hadoop版本：https://archive.apache.org/dist/hadoop/common/

例如：[hadoop-3.3.1-aarch64.tar.gz](https://archive.apache.org/dist/hadoop/common/hadoop-3.3.1/hadoop-3.3.1-aarch64.tar.gz)

解压后，配置系统环境变量：

```shell
HADOOP_HOME=E:\App\hadoop-3.3.1

path=%HADOOP_HOME%\bin;%HADOOP_HOME%\sbin
```

配置 Hadoop 环境：

修改 etc/hadoop/hadoop-env.cmd 文件：配置 JAVA_HOME

```shell
# C:\Users\zhengjiaao>echo %JAVA_HOME%
# E:\LocalDevEnvJava\Java\jdk1.8.0_231
set JAVA_HOME= E:\LocalDevEnvJava\Java\jdk1.8.0_231
```
修改 etc/hadoop/core-site.xml 文件：配置 HDFS
```shell
<name>fs.defaultFS</name> 的值为 hdfs://localhost:9000，以指定默认的 HDFS 地址。
```

验证部署成功：
```shell
hadoop -version
```


## Springboot 集成 Hadoop 步骤和依赖

**步骤 1**: 创建 Spring Boot 项目

**步骤 2**: 添加 Hadoop 依赖

```xml

<dependencies>
    <!-- Hadoop Core -->
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-hdfs</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-mapreduce-client-core</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-yarn-client</artifactId>
        <version>3.3.1</version>
    </dependency>

    <!-- 其他依赖根据需求添加 -->
</dependencies>
```

**步骤 3**: 配置 Hadoop

在 Spring Boot 项目的配置文件（如 application.properties 或 application.yml）中，添加 Hadoop 相关的配置。具体配置取决于你的
Hadoop 部署，包括 Hadoop 的文件系统（如 HDFS）和资源管理器（如 YARN）等。

例如，以下是一个示例的 application.properties 文件：

```properties
# Hadoop Configuration
hadoop.fs.defaultFS=hdfs://localhost:9000
hadoop.yarn.resourcemanager.address=localhost:8032
```

**步骤 4**: 编写 Hadoop 相关的代码

在 Spring Boot 项目中编写与 Hadoop 相关的代码，包括使用 Hadoop 文件系统（HDFS）读写文件、执行 MapReduce 作业等。

你可以使用 Hadoop 提供的 Java API，例如 org.apache.hadoop.fs.FileSystem 类来操作 HDFS，或者使用 Hadoop MapReduce API
来编写和执行 MapReduce 作业。

下面是一个简单的示例代码，演示如何使用 Hadoop API 读取 HDFS 上的文件：

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class HdfsService {

    public String readFileFromHdfs(String filePath) throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(path)));

        // 读取文件内容
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line);
            content.append("\n");
        }

        // 关闭资源
        bufferedReader.close();
        fileSystem.close();

        return content.toString();
    }
}
```

**步骤 5**: 运行和测试
运行 Spring Boot 应用程序，并测试 Hadoop 相关的功能。你可以使用 Spring Boot 提供的测试框架（如 JUnit）编写单元测试来验证
Hadoop 功能的正确性。

这是一个包含了 Spring Boot 和 Hadoop 集成的基本方案。具体实施方式可能因项目需求而有所不同，你可以根据自己的需求进行适当的调整和扩展。

## Hadoop 核心依赖

```xml

<dependencies>
    <!-- Hadoop Core -->
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-hdfs</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-mapreduce-client-core</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-yarn-client</artifactId>
        <version>3.3.1</version>
    </dependency>

    <!-- 其他依赖根据需求添加 -->

</dependencies>
```

## Hadoop 常用依赖

Hadoop Java 提供了一些常用的依赖，这些依赖可以通过 Maven 或其他构建工具来管理。以下是一些常见的 Hadoop Java 依赖：

Hadoop Common：这是 Hadoop 的共享库，它包含了 Hadoop 的核心功能和公共工具类。

```xml

<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-common</artifactId>
    <version>3.3.1</version>
</dependency>
```

Hadoop HDFS：这是 Hadoop 分布式文件系统（HDFS）的客户端库，用于操作和管理 HDFS 上的文件和目录。

```xml

<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-hdfs</artifactId>
    <version>3.3.1</version>
</dependency>
```

Hadoop MapReduce：这是 Hadoop 的 MapReduce 框架的客户端库，用于编写和执行 MapReduce 作业。

```xml

<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-mapreduce-client-core</artifactId>
    <version>3.3.1</version>
</dependency>
```

Hadoop YARN：这是 Hadoop 的资源调度和集群管理框架 YARN 的客户端库，用于提交和管理应用程序。

```xml

<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-yarn-client</artifactId>
    <version>3.3.1</version>
</dependency>
```

Hadoop Client是一个Hadoop生态系统中的核心组件，它提供了与Hadoop集群进行交互的客户端库。该库提供了访问Hadoop分布式文件系统（HDFS）和执行MapReduce作业的API。

```xml

<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-client</artifactId>
    <version>3.3.1</version>
</dependency>
```

hadoop-client依赖于以下几个关键模块：

1. hadoop-common: 提供了Hadoop的通用功能和工具类，包括配置管理、文件系统操作、网络通信等。
2. hadoop-hdfs: 提供了与Hadoop分布式文件系统（HDFS）进行交互的API，包括文件的读写、权限管理、块操作等。
3. hadoop-mapreduce-client-core: 提供了与Hadoop MapReduce框架进行交互的API，包括作业的提交、任务的配置和监控等。
4. hadoop-yarn-client: 提供了与Hadoop资源管理器（YARN）进行交互的API，包括应用程序的提交、资源的请求和分配等。

Hadoop AWS：这是 Hadoop 与亚马逊 Web Services（AWS）集成的客户端库，用于在 AWS 上使用 Hadoop。

```xml

<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-aws</artifactId>
    <version>3.3.1</version>
</dependency>
```

Hadoop Hive：这是 Hadoop 的数据仓库基础架构 Hive 的客户端库，用于编写和执行 Hive 查询。

```xml

<dependency>
    <groupId>org.apache.hive</groupId>
    <artifactId>hive-exec</artifactId>
    <version>3.1.1</version>
</dependency>
```

Hadoop Pig：这是 Hadoop 的数据流编程语言 Pig 的客户端库，用于编写和执行 Pig 脚本。

```xml

<dependency>
    <groupId>org.apache.pig</groupId>
    <artifactId>pig</artifactId>
    <version>0.17.0</version>
</dependency>
```

Hadoop Spark：这是 Hadoop 与大数据处理框架 Spark 的集成库，用于在 Hadoop 上运行 Spark 应用程序。

```xml

<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-core_2.12</artifactId>
    <version>3.2.0</version>
</dependency>
```

Hadoop HBase：这是 Hadoop 的分布式数据库 HBase 的客户端库，用于与 HBase 进行交互。

```xml

<dependency>
    <groupId>org.apache.hbase</groupId>
    <artifactId>hbase-client</artifactId>
    <version>2.4.7</version>
</dependency>
```

Hadoop Kafka：这是 Hadoop 与消息队列系统 Kafka 的集成库，用于在 Hadoop 中读取和写入 Kafka 消息。

```xml

<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.8.1</version>
</dependency>
```

Hadoop Flume：这是 Hadoop 的日志收集和聚合系统 Flume 的客户端库，用于将日志数据传输到 Hadoop。

```xml

<dependency>
    <groupId>org.apache.flume</groupId>
    <artifactId>flume-ng-core</artifactId>
    <version>1.9.0</version>
</dependency>
```

Hadoop Cascading：这是 Hadoop 的数据流编程框架 Cascading 的客户端库，用于定义和执行复杂的数据处理流程。

```xml

<dependency>
    <groupId>cascading</groupId>
    <artifactId>cascading-core</artifactId>
    <version>3.3.1</version>
</dependency>
```

Hadoop Mahout：这是 Hadoop 的机器学习库 Mahout 的客户端库，用于在 Hadoop 上执行机器学习算法。

```xml

<dependency>
    <groupId>org.apache.mahout</groupId>
    <artifactId>mahout-core</artifactId>
    <version>0.13.0</version>
</dependency>
```

Hadoop ZooKeeper：这是 Hadoop 的分布式协调服务 ZooKeeper 的客户端库，用于在 Hadoop 中实现分布式锁、配置管理等功能。

```xml

<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.7.0</version>
</dependency>
```