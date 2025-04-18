# springboot-file-office-jodconverter


jodconverter 是 一个开源的 Java 文档转换器，它使用 LibreOffice 或 OpenOffice 作为底层引擎，支持 PDF、DOC、DOCX、XLS、XLSX、PPT、PPTX、HTML、TXT 等格式的转换。


## 快速开始

#### 安装组件 LibreOffice or OpenOffice

- [libreoffice 官网](https://www.libreoffice.org/)
- [OpenOffice 官网](https://www.openoffice.org/download/index.html)

#### 引入依赖

```xml
    <dependencies>
    <!--jodconverter 底层支持 LibreOffice or OpenOffice-->

    <!-- office文件预览及格式转换相关 -->
    <!-- 支持 springboot starter 配置 -->
    <dependency>
        <groupId>org.jodconverter</groupId>
        <artifactId>jodconverter-spring-boot-starter</artifactId>
        <version>4.4.8</version>
    </dependency>
    <!-- jodconverter 本地 or 远程 -->
    <dependency>
        <groupId>org.jodconverter</groupId>
        <artifactId>jodconverter-local</artifactId>
        <version>4.4.8</version>
    </dependency>
    <dependency>
        <groupId>org.jodconverter</groupId>
        <artifactId>jodconverter-remote</artifactId>
        <version>4.4.8</version>
    </dependency>

    </dependencies>
```