# springboot-gis-supermap

## 介绍

    1. 矢量数据：超图、geojson、shp、kml、gpx、osm、gdb、dxf、dgn、cad、dwg、dgn、dwf等
    2. 栅格数据：tif、png、jpg、bmp、gif、tiff等

## 超图数据格式

### 文件数据源

将空间数据和属性数据直接存储到文件中。存储扩展名为.udb 或.udbx 的文件。在小数据量情况下使用文件型数据源地图的显示更快，且数据迁移方便。

| **类型**       | **描述**                                                                                                       |
|--------------|--------------------------------------------------------------------------------------------------------------|
| UDB          | 跨平台文件引擎类型，针对 UDB 数据源。                                                                                        |
| UDBX         | UDBX 文件引擎，可以读写以及管理 Spatialite 空间数据，针对 UDBX数据源，支持的数据集类型包括：点、线、面、文本、CAD、属性表、三维点/线/面、EPS复合点/线/面/文本、栅格、影像、镶嵌数据集。 |
| ImagePlugins | 影像只读引擎类型，针对通用影像格式如 BMP，JPG，TIFF，以及超图自定义的影像格式 SIT 等。                                                          |
| 矢量文件         | 矢量文件引擎类，针对通用矢量格式如 shp，tab，Acad等,支持矢量文件的编辑和保存。                                                                |

### 文件引擎

包含有四类：

1. SuperMap 自定义的 UDB 引擎（可读写）
2. UDBX 引擎（可读写）
3. 影像插件引擎（直接访问一些影像数据）
4. 矢量文件引擎（直接访问外部矢量文件)

```text
1. UDB引擎是SuperMap Objects的自定义文件型空间数据引擎，采用文件+数据库混合存储方式。一个UDB工程包括UDB文件和UDD文件，分别用于存储空间数据和属性数据库(采用Access的MDB数据库格式)。通过复合文档技术，UDB引擎可以在一个工程中存储多个数据集。它适用于中、小型系统和桌面应用，旨在提高效率并弥补纯数据库引擎的不足之处。

2. UDBX引擎是SuperMap的文件引擎，用于读写和管理Spatialite空间数据。它利用了Spatialite的高效管理和轻量级数据库（SQLite）的特点。使用UDBX文件引擎无需安装和部署数据库系统，而是创建一个.udbx文件作为数据源。UDBX支持多种数据集类型，并具有开放性，可以直接操作第三方导入到Spatialite数据库中的空间数据。

3. SuperMap的影像插件引擎支持只读显示栅格类型的数据，包括多种格式如BMP、JPEG、RAW、TIFF、SCI、SIT和ERDAS IMAGINE。针对每种格式，提供了相应的只读引擎。

4. 矢量文件引擎是SuperMap的文件引擎，用于读写矢量文件。它支持多种矢量文件格式，包括shp、tab、dxf等。
```

注意：空间数据引擎型，包括 UDBX(.udbx)、SpatiaLite(.sqlite)文件和 GeoPackage(.gpkg)，其中，SpatiaLite 是 UDBX
的原生数据库，GeoPackage 为 SQLite 数据库中的空间数据。

## 如何读取 UDBX 文件？

1. SuperMap Objects（SuperMap对象）提供的API，读取UDBX文件中的数据
2. UDBX是基于Spatialite扩展的SQLite数据库，可以使用SQLite的库或工具来处理UDBX文件

### SuperMap Objects API 读取UDBX文件

1、添加SuperMap Objects依赖

```xml

<project>
    <properties>
        <supermap.version>10.2.0-SNAPSHOT</supermap.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.supermap.data</groupId>
            <artifactId>data</artifactId>
            <version>${supermap.version}</version>
        </dependency>
    </dependencies>
</project>
```

2、读取UDBX文件

```java
import com.supermap.data.*;

public class UDBXReader {

    public static void main(String[] args) {
        // 初始化 SuperMap Objects
        Environment.initialize();

        // 打开 UDBX 文件
        Workspace workspace = new Workspace();
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setEngineType(EngineType.UDBX);
        connectionInfo.setServer("数据源路径/数据源.udbx");
        connectionInfo.setAlias("UDBX_Datasource");
        workspace.getDatasources().open(connectionInfo);

        // 获取 UDBX 数据源
        Datasource datasource = workspace.getDatasources().get("UDBX_Datasource");

        // 获取数据集
        Dataset dataset = datasource.getDatasets().get("数据集名称");

        // 读取数据集中的记录
        Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);

        // 遍历记录集，读取数据
        while (!recordset.isEOF()) {
            // 读取属性数据
            for (int i = 0; i < recordset.getFieldCount(); i++) {
                Field field = recordset.getField(i);
                Object value = recordset.getFieldValue(i);
                System.out.println(field.getName() + ": " + value);
            }

            // 读取空间数据
            Geometry geometry = recordset.getGeometry();
            // 处理空间数据，例如获取坐标、几何类型等

            recordset.moveNext();
        }

        // 释放资源
        recordset.close();
        dataset.close();
        datasource.close();
        workspace.close();

        // 释放 SuperMap Objects
        Environment.dispose();
    }
}
```

创建超图许可

设置合适的许可路径（"your_license_path"）并传入UDBX文件路径（udbxFilePath）来创建SuperMap对象。

```java
import com.supermap.data.*;

public class SuperMapHelper {
    private DatasourceConnectionInfo datasourceConnectionInfo;

    public SuperMapHelper(String udbxFilePath) {
        // 设置SuperMap的许可信息
        Environment.setLicensePath("your_license_path");

        // 初始化SuperMap引擎
        Environment.initialize();

        // 创建数据源连接信息
        datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(udbxFilePath);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
    }

    public Datasource openDatasource() throws Exception {
        // 打开数据源
        Workspace workspace = new Workspace();
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);
        return datasource;
    }

    public void close() {
        // 释放SuperMap资源
        Environment.dispose();
    }
}
```

读取UDBX数据：

```java
public void readUDBXData(String udbxFilePath) throws Exception {
    SuperMapHelper superMapHelper = new SuperMapHelper(udbxFilePath);
    Datasource datasource = superMapHelper.openDatasource();

    // 获取数据集名称
    String datasetName = "your_dataset_name";

    // 打开数据集
    Dataset dataset = datasource.getDatasets().get(datasetName);

    if (dataset != null) {
        // 获取记录集
        Recordset recordset = dataset.getRecordset(false, CursorType.DYNAMIC);

        // 遍历记录集
        while (!recordset.isEOF()) {
            // 读取记录数据
            String value1 = recordset.getString("column1");
            int value2 = recordset.getInt32("column2");

            // 处理数据...

            // 移动到下一条记录
            recordset.moveNext();
        }

        // 关闭记录集
        recordset.close();
    }

    // 关闭数据源和SuperMap对象
    datasource.close();
    superMapHelper.close();
}
```

写入UDBX数据：

```java
public void writeUDBXData(String udbxFilePath) throws Exception {
    SuperMapHelper superMapHelper = new SuperMapHelper(udbxFilePath);
    Datasource datasource = superMapHelper.openDatasource();

    // 获取数据集名称
    String datasetName = "your_dataset_name";

    // 打开数据集
    Dataset dataset = datasource.getDatasets().get(datasetName);

    if (dataset != null) {
        // 创建记录集
        Recordset recordset = dataset.getRecordset(false, CursorType.DYNAMIC);

        // 添加新记录
        recordset.addNew();
        recordset.setString("column1", "value1");
        recordset.setInt32("column2", 123);

        // 保存记录
        recordset.update();

        // 关闭记录集
        recordset.close();
    }

    // 关闭数据源和SuperMap对象
    datasource.close();
    superMapHelper.close();
}
```

### SQLite 命令行工具读取UDBX文件

SQLite的命令行工具来读取UDBX文件:

安装SQLite的命令行工具：

- [sqlite 官网](https://www.sqlite.org/index.html)
- [sqlite 官网下载地址](https://www.sqlite.org/download.html)

windows下载例如：`sqlite-dll-win-x64-3460000.zip`和`sqlite-tools-win-x64-3460000.zip`，解压后放到一起，添加环境变量。

```shell

# 打开SQLite命令行工具: 验证是否安装成功
sqlite3

# 打开UDBX文件
sqlite3 数据库文件路径.udbx

# 执行SQL查询：查看可用的数据表
.tables

# 执行SQL查询：查看数据
SELECT * FROM 数据集名称;

# 关闭UDBX文件，退出SQLite命令行工具
.quit
```

### SQLite JDBC 读取UDBX文件

请确保您已经将SQLite JDBC驱动程序（JAR文件）添加到您的Java项目中，并根据实际情况修改数据库文件路径、表名和列名。

1、添加SQLite JDBC驱动程序依赖

```xml

<dependencies>
   <!-- SQLite JDBC driver -->
   <dependency>
      <groupId>org.xerial</groupId>
      <artifactId>sqlite-jdbc</artifactId>
      <version>3.46.0.0</version>
   </dependency>
</dependencies>

```

2、读取UDBX文件

```java
import java.sql.*;

public class UDBXReader {

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 加载SQLite JDBC驱动程序
            Class.forName("org.sqlite.JDBC");

            // 连接UDBX数据库
            connection = DriverManager.getConnection("jdbc:sqlite:数据库文件路径.udbx");

            // 创建Statement对象
            statement = connection.createStatement();

            // 执行SQL查询
            String sql = "SELECT * FROM 表名";
            resultSet = statement.executeQuery(sql);

            // 处理查询结果
            while (resultSet.next()) {
                // 读取属性数据
                String column1 = resultSet.getString("列名1");
                int column2 = resultSet.getInt("列名2");
                // ...

                System.out.println("Column 1: " + column1);
                System.out.println("Column 2: " + column2);
                // ...
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### Spring Boot中使用SQLite和JPA来读取UDBX文件

支持创建udbx文件，修改、读取udbx文件数据等。

1. 添加依赖:

```xml

<dependencies>
    <!-- SQLite JDBC driver -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.36.0</version>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

2. 配置数据库连接:

```yaml
spring:
  datasource:
    url: jdbc:sqlite:数据库文件路径.udbx
    #    url: jdbc:sqlite:新数据库文件路径.udbx
    driver-class-name: org.sqlite.JDBC
```

3. 创建实体类：

请根据UDBX文件中实际的表和列名进行调整。

```java
import javax.persistence.*;

@Entity
@Table(name = "表名")
public class MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "列名1")
    private String column1;

    @Column(name = "列名2")
    private int column2;

    // 省略其他属性和方法的定义

    // Getter和Setter方法
}
```

## 安装超图GIS开发环境

类似与Java、jre、sdk开发环境

- [超图GIS开发环境-组件-SuperMap iObjects Java 11i](http://support.supermap.com.cn/DownloadCenter/ProductPlatform.aspx)

## SDK

### SuperMap SDK

较重 udbx

二维：开源GIS sdk(轻量级)、超图sdk(较重-二维、三维)

三维：超图sdk(开源) x、y、z（三维）、m（？）

超图三维是加密的，可能无法转为开源的协议格式？带预研

开源GIS 转 超图GIS
超图GIS 转 开源GIS

### 超图许可认证

### 代码示例

### 开源GIS SDK

    采用开源GIS SDK (gdal、geotools、jts等)进行处理超图二维数据。

## UDBX与一些开源GIS文件格式进行互相转换

SuperMap Objects提供了一些API和功能，可用于将UDBX与一些开源GIS文件格式进行互相转换。以下是一些示例转换的方法：

1. 将UDBX转换为Shapefile：
    * 打开UDBX数据源和数据集。
    * 创建Shapefile数据源。
    * 遍历UDBX数据集的记录，将数据写入Shapefile数据集。
    * 关闭数据源和数据集。
2. 将Shapefile转换为UDBX：
    * 打开Shapefile数据源和数据集。
    * 创建UDBX数据源。
    * 创建UDBX数据集。
    * 遍历Shapefile数据集的记录，将数据写入UDBX数据集。
    * 关闭数据源和数据集。
3. 将UDBX转换为GeoJSON：
    * 打开UDBX数据源和数据集。
    * 创建GeoJSON文件。
    * 遍历UDBX数据集的记录，将数据写入GeoJSON文件。
    * 关闭数据源和数据集。
4. 将GeoJSON转换为UDBX：
    * 打开GeoJSON文件。
    * 创建UDBX数据源。
    * 创建UDBX数据集。
    * 遍历GeoJSON文件中的要素，将数据写入UDBX数据集。
    * 关闭数据源和数据集。