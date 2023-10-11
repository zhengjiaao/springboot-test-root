# geotools-gt-shapefile

**说明**

gt-shapefile 模块（geotools-shapefile）是 GeoTools 库的一个扩展模块，专门用于读取和写入 Shapefile 格式的数据。
Shapefile 是一种常见的地理空间数据格式，通常用于存储矢量数据，如点、线、面等几何对象。

## gt-shapefile 模块的功能

gt-shapefile 模块提供了以下主要功能：

1. 读取 Shapefile 数据：gt-shapefile 模块提供了读取 Shapefile 格式数据的功能。它可以读取 Shapefile 文件并将其转换为
   GeoTools 中的几何对象，使你能够对其进行进一步的处理和分析。
2. 写入 Shapefile 数据：gt-shapefile 模块还支持将 GeoTools 的几何对象写入 Shapefile 格式。你可以将几何对象保存为 Shapefile
   文件，以便与其他 GIS 软件进行交互或共享。
3. 属性字段处理：除了几何对象，Shapefile 还包含属性字段的信息。gt-shapefile 模块可以读取 Shapefile
   的属性字段，并提供一些工具来处理和操作这些属性数据。
4. 坐标参考系统支持：Shapefile 数据通常包含坐标参考系统（CRS）的信息。gt-shapefile 模块可以读取和解析 Shapefile 中的 CRS
   信息，并与 GeoTools 的 CRS 功能集成，以便进行坐标转换和投影操作。
5. 数据集元数据：gt-shapefile 模块可以提供 Shapefile 数据集的元数据，如文件名、几何类型、字段信息等。这些元数据可以帮助你了解和理解
   Shapefile 数据的结构和内容。

## gt-shapefile 模块的功能实例

引入依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

### 读写 Shapefile 数据

简单示例：读写shapefile文件

由于代码过多，参考单元示例 `shapefile/ShapefileReadExample.java` 和`shapefile/ShapefileWriterExample.java`



