# geotools-gt-main

**说明**

## gt-main 模块主要提供的功能

gt-main 模块是 GeoTools 中的核心模块之一，提供了许多重要的功能。以下是 gt-main 模块主要提供的功能：

1. 数据读写：gt-main 模块提供了读取和写入各种地理空间数据格式的功能。它支持读取和写入的数据格式包括
   Shapefile、GeoJSON、KML、GML 等。您可以使用该模块读取和写入这些格式的地理数据。
2. 数据源管理：gt-main 模块提供了各种数据源的管理和访问功能，包括文件数据源（如 Shapefile、GeoPackage、GeoJSON）、数据库数据源（如
   PostgreSQL/PostGIS、MySQL、Oracle）、Web 服务数据源（如 WMS、WFS）等。它提供了一致的接口和工具，使你能够轻松地连接到不同的数据源并读取/写入地理空间数据。
3. 空间查询：gt-main 模块提供了强大的空间查询功能。您可以执行空间过滤、属性过滤和空间关系查询等操作，以从地理数据集中检索满足特定条件的地理要素。
4. 坐标转换：gt-main 模块支持各种地图投影和坐标参考系统，提供了坐标转换的功能。您可以使用该模块将数据从一个坐标系统转换为另一个坐标系统，或执行地图投影转换。
5. 坐标参考系统（CRS）处理：gt-main 模块支持各种坐标参考系统的定义和转换。它提供了用于解析和创建坐标参考系统对象的工具，以及执行不同坐标参考系统之间的转换操作。
6. 地图渲染：gt-main 模块提供了地图渲染和数据可视化的功能。您可以使用该模块绘制点、线、面符号，进行颜色渲染，添加标签注记等，以将地理数据以可视化的方式呈现在地图上。
7. 空间分析：gt-main 模块提供了一些基本的空间分析功能，如缓冲区分析、空间交叉、空间插值等。这些功能可以帮助您进行地理空间数据的处理和分析任务。
8. 几何操作和分析：gt-main 模块包含了一组强大的几何操作和分析功能，可以对地理空间几何对象进行缓冲区分析、拓扑关系判断、空间查询等操作。它提供了一系列方法来执行几何操作，如求交、求并、求差等。
9. 数据过滤和转换：gt-main 模块支持数据过滤和转换操作。您可以使用该模块进行属性过滤、字段选择、数据转换等操作，以满足数据处理和准备的需求。
10. 数据转换和转换：gt-main 模块支持将地理空间数据在不同格式之间进行转换和转码。你可以将数据从一种格式转换为另一种格式，如
    Shapefile 转 GeoJSON，或者对数据进行投影转换、坐标转换等操作。

## gt-main 模块支持哪些数据格式的读取和写入？

gt-main 模块支持多种地理空间数据格式的读取和写入。以下是 gt-main 模块常用的数据格式：

1. Shapefile：Shapefile 是一种常见的地理空间数据格式，gt-main 模块提供了对 Shapefile 的读取和写入支持。
2. GeoJSON：GeoJSON 是一种基于 JSON 的地理空间数据格式，gt-main 模块可以读取和写入 GeoJSON 格式的数据。
3. KML：KML（Keyhole Markup Language）是一种用于描述地理数据的 XML 格式，gt-main 模块可以读取和写入 KML 格式的数据。
4. GML：GML（Geography Markup Language）是一种基于 XML 的地理空间数据格式，gt-main 模块支持读取和写入 GML 格式的数据。
5. WKT：WKT（Well-Known Text）是一种文本表示的地理空间数据格式，gt-main 模块可以读取和写入 WKT 格式的数据。

此外，gt-main 模块还支持其他一些格式的读取和写入，如 CSV（逗号分隔值）、DBF（dBASE 文件格式）等。此外，GeoTools 还提供了扩展模块和插件，可以扩展
gt-main 模块的数据格式支持，以满足更多特定格式的读取和写入需求。

## 依赖引入

```xml

<dependency>
    <groupId>org.geotools</groupId>
    <artifactId>gt-main</artifactId>
    <version>${geotools.version}</version>
</dependency>
```

## gt-main 实例

### gt-main 读写GeoJSON文件

使用 GeoTools 的 gt-main 模块进行 GeoJSON 数据的读取和写入。

引入依赖 gt-geojson

```xml

<dependencies>
    <!-- GeoTools 主要模块(内置JTS) -->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-main</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <!-- Geojson 支持 -->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-geojson</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

简单示例

```java
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.junit.jupiter.api.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;

public class GeoJSONReadWriteExample {

    // 读取 GeoJSON 数据
    public static void readGeoJSON(String filePath) throws IOException {
        File geojsonFile = new File(filePath);
        FeatureJSON featureJSON = new FeatureJSON();
        SimpleFeatureCollection featureCollection = featureJSON.readFeatureCollection(new FileInputStream(geojsonFile));

        try (SimpleFeatureIterator iterator = featureCollection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // 处理每个要素的属性和几何信息
                System.out.println("Feature ID: " + feature.getID());
                System.out.println("Geometry: " + feature.getDefaultGeometry());
                System.out.println("Attributes: " + feature.getAttributes());
                System.out.println();
            }
        }
    }

    // 写入 GeoJSON 数据
    public static void writeGeoJSON(String filePath) throws IOException {
        SimpleFeatureType featureType = ...; // 创建要素类型
        SimpleFeatureCollection featureCollection = ...; // 创建要素集合

        File geojsonFile = new File(filePath);
        FeatureJSON featureJSON = new FeatureJSON();
        try (FileWriter writer = new FileWriter(geojsonFile)) {
            featureJSON.writeFeatureCollection(featureCollection, writer);
        }
    }

    @Test
    public void testGeoJSONReadWrite() {
        String filePath = "path/to/input/geojsonfile.geojson";

        try {
            writeGeoJSON(filePath);
            readGeoJSON(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### geotools读写Shapefile文件-未能测试成功

使用 GeoTools 的 gt-main 模块进行 GeoJSON 数据的读取和写入。

引入依赖 gt-geojson

```xml

<dependencies>
    <!-- GeoTools 主要模块(内置JTS) -->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-main</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <!-- Shapefile 支持 -->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

简单示例：读写shapefile文件

由于代码过多，参考单元示例 `shapefile/ShapefileReadExample.java` 和`shapefile/ShapefileWriterExample.java`

### gt-main 读写 WKT 格式的数据

在 GeoTools 中，gt-main 模块（geotools-main）提供了读取和写入 WKT（Well-Known Text）格式数据的功能。WKT
是一种文本表示形式，用于描述地理空间几何对象和坐标参考系统。

引入依赖：

```xml

<dependencies>
    <!-- GeoTools 主要模块(内置JTS) -->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-main</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

要读取 WKT 格式的数据，可以使用 WKTReader 类。以下是一个读取 WKT 格式数据的示例代码：

```java
import org.locationtech.jts.geom.Geometry;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;

public class WKTExample {

    public static void main(String[] args) throws Exception {
        // 读取 WKT 格式的数据
        String wkt = "POINT (30 10)";
        Geometry geometry = readWKT(wkt);

        // 处理几何对象
        processGeometry(geometry);

        // 将几何对象转换为 WKT 格式的数据
        String convertedWKT = writeWKT(geometry);
        System.out.println("Converted WKT: " + convertedWKT);
    }

    public static Geometry readWKT(String wkt) throws Exception {
        WKTReader2 reader = new WKTReader2();
        return reader.read(wkt);
    }

    public static void processGeometry(Geometry geometry) {
        // 处理几何对象的逻辑
        System.out.println("Geometry Type: " + geometry.getGeometryType());
        System.out.println("Coordinates: " + geometry.getCoordinates().length);
    }

    public static String writeWKT(Geometry geometry) {
        WKTWriter2 writer = new WKTWriter2();
        return writer.write(geometry);
    }
}
```

输出结果：

```text
Geometry Type: Point
Coordinates: 1
Converted WKT: POINT (30 10)
```

在上述示例中，readWKT 方法使用 WKTReader2 从 WKT 格式的字符串中读取几何对象。WKTReader2 是 GeoTools 提供的 JTS（Java
Topology Suite）的 WKT 读取器。

processGeometry 方法用于处理几何对象。在示例中，我们简单地打印了几何对象的类型和坐标信息。

writeWKT 方法使用 WKTWriter2 将几何对象转换为 WKT 格式的字符串。

请确保在运行示例代码之前，将 "POINT (30 10)" 替换为实际的 WKT 格式数据。

通过使用 WKTReader 和 WKTWriter，gt-main 模块可以方便地读取和写入 WKT 格式的数据。这对于处理和转换地理空间几何对象是非常有用的。

