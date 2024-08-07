# springboot-gis-arcgis

## 如何使用

### 1. ArcGis Maps SDK for Java 环境搭建

### 2.引入依赖

2.1 方式一：通过maven

```xml

<repositories>
    <repository>
        <id>arcgis</id>
        <url>https://esri.jfrog.io/artifactory/arcgis</url>
    </repository>
</repositories>
```

```xml
        <!--ArcGIS dependencies -->
<dependency>
    <groupId>com.esri.arcgisruntime</groupId>
    <artifactId>arcgis-java</artifactId>
    <version>${arcgis.version}</version>
</dependency>
```

2.2 方式二：通过jar包

添加
JAR，官网下载：[arcgis-java-200.4.0.jar](https://esri.jfrog.io/ui/native/arcgis/com/esri/arcgisruntime/arcgis-java/200.4.0/)

把所需的jar包安装到本地仓库：

```shell
# jdk 版本不对应
mvn install:install-file -Dfile='arcgis-java-100.10.0.jar' -DgroupId='com.esri.arcgisruntime' -DartifactId='arcgis-java' -Dversion='100.10.0' -Dpackaging='jar'
mvn install:install-file -Dfile='arcgis-java-100.12.0.jar' -DgroupId='com.esri.arcgisruntime' -DartifactId='arcgis-java' -Dversion='100.12.0' -Dpackaging='jar'
mvn install:install-file -Dfile='arcgis-java-200.4.0.jar' -DgroupId='com.esri.arcgisruntime' -DartifactId='arcgis-java' -Dversion='200.4.0' -Dpackaging='jar'
```

```xml
        <!--ArcGIS dependencies -->
<dependency>
    <groupId>com.esri.arcgisruntime</groupId>
    <artifactId>arcgis-java</artifactId>
    <version>${arcgis.version}</version>
</dependency>
```

### 3.编写实例代码

```java
public class Main {

}
```