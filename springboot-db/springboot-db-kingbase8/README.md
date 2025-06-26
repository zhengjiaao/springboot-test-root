# springboot-db-kingbase8

## 介绍

kingbase8 是人大金仓数据库，这是一个基于springboot的kingbase8数据库连接示例。

## 本地运行环境搭建

> 以下是你必须要安装的基础软件,可以使项目正常打包及运行.

|       | 官网文档                                                                              | github | 使用版本下载                                                                       | 详细 | 是否必须安装 |
|-------|-----------------------------------------------------------------------------------|--------|------------------------------------------------------------------------------|----|--------| 
| java  | [www.oracle.com/java8](https://www.oracle.com/java/technologies/downloads/#java8) |        | [java8 downloads](https://www.oracle.com/java/technologies/downloads/#java8) |    | **必须** |
| maven | [maven.apache.org](https://maven.apache.org/)                                     |        | [maven3.6.2 downloads](https://maven.apache.org/download.cgi)                |    | **必须** |

## 适配的中间件版本

> 以下是你可能会用到的中间件

|           | 官网文档                                                | github | 使用版本下载 | 详细 | 推荐 |
|-----------|-----------------------------------------------------|--------|--------|----|----| 
| kingbase8 | [www.kingbase.com.cn](https://www.kingbase.com.cn/) |        |        |    |    |

## 快速开始

> 快速开始说明

#### 1.中间件安装

> 请阅读中间件安文档，若没有，可能需要跳过。

- 需要安装中间件，问度娘

#### 2.引入依赖

```xml

<properties>
    <kingbase.version>8.6.0</kingbase.version>
    <kingbase-for-hibernate.version>5.4.6.Finaldialect</kingbase-for-hibernate.version>
</properties>

        <!-- 人大金仓 -->
<dependency>
<groupId>com.kingbase8</groupId>
<artifactId>kingbase8</artifactId>
<version>${kingbase.version}</version>
</dependency>
<dependency>
<groupId>com.kingbase</groupId>
<artifactId>hibernate-dialect</artifactId>
<version>${kingbase-for-hibernate.version}</version>
</dependency>

```

#### 3.配置文件

> 可参考示例项目 配置文件

#### 4.代码示例

> 可参考示例项目 代码示例

## spring-boot 集成(组件)示例

> 以下是已经完成的示例模块

