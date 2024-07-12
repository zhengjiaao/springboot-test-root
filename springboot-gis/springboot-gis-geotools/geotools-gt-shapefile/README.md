# geotools-gt-shapefile

**说明**

gt-shapefile 模块（geotools-shapefile）是 GeoTools 库的一个扩展模块，专门用于读取和写入 Shapefile 格式的数据。

## Shapefile文件格式介绍

> Shapefile 是一种常见的地理空间数据格式，通常用于存储矢量数据，如点、线、面等几何对象。    
> Shapefile文件可以包含一个或多个图层。每个图层对应Shapefile文件中的一个文件，这些文件具有相同的前缀名称但不同的扩展名。

Shapefile文件由以下几个文件组成：

1. .shp：包含几何数据的主要文件，存储点、线、多边形等几何实体的空间信息。
2. .shx：空间索引文件，提供.shp文件中几何实体的快速访问和查询。
3. .dbf：属性数据文件，存储与每个几何实体相关联的属性信息。
4. .prj：投影文件，定义Shapefile文件中几何数据的地理参考系统。

每个Shapefile文件可以包含一个图层，也可以包含多个图层。
如果Shapefile文件包含多个图层，则将使用相同的.shp、.shx和.prj文件，但每个图层将有自己的.dbf文件，其中包含该图层的属性数据。

要确定Shapefile文件中有多少个图层，可以查看.dbf文件的数量。每个.dbf文件对应一个图层。

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

## gt-shapefile API 的一些主要功能

gt-shapefile 是 GeoTools 库的一部分，它提供了读取和写入 Shapefile 格式文件的能力。
Shapefile 是一种流行的地理空间矢量数据格式，主要用于存储点、线和多边形特征。

以下是 gt-shapefile API 的一些主要功能：

1. 读取 Shapefile 数据
    * 能够解析 Shapefile 文件，包括 .shp, .shx, 和 .dbf 文件。
    * 支持读取各种几何类型：点（Point）、线（LineString）、多边形（Polygon）以及它们的集合类型。
    * 提供对属性表（Attribute Table）的访问，可以读取与几何对象相关的属性数据。
2. 写入 Shapefile 数据
    * 可以创建新的 Shapefile 文件，并写入几何数据和属性数据。
    * 支持写入多种几何类型和属性数据。
3. 数据过滤和查询
    * 提供了基于 CQL (Common Query Language) 的SQL查询功能，允许用户根据属性或空间条件筛选数据。
    * 支持空间索 引和优化，以提高查询性能。
    * 支持过滤器模型，可以使用 Filter API 来构建复杂的查询表达式。
4. 空间分析：
    * 能够进行基本的空间分析，如计算面积、周长、质心等。
    * 可以进行空间关系判断，如点是否在多边形内。
5. 数据转换
    * 能够将 Shapefile 数据转换为其他 GeoTools 支持的数据格式，如 GeoJSON 或者其他的矢量数据格式。
    * 支持坐标系转换，可以在不同的投影之间转换数据。
6. 错误处理和修复
    * 包含了处理 Shapefile 中常见错误的工具，比如修复几何拓扑错误或者处理损坏的 Shapefile 文件。
7. 元数据支持
    * 可以读取和写入 Shapefile 文件的元数据信息。如文件名、几何类型、字段信息等。
8. 图形渲染
    * 虽然主要功能集中在数据读写上，但 geotools-gt-shapefile 通常会与 GeoTools 的其他模块一起使用，以支持图形渲染和地图可视化。
9. 批处理和流式处理
    * 支持批处理读写操作，也支持流式处理大量数据。
10. 数据验证：
    * 提供验证 Shapefile 结构和数据完整性的工具。
11. 与 GeoTools 生态系统的集成：
    * 完全兼容 GeoTools 的其他模块，可以轻松地与其他数据格式（如 GeoJSON、WFS、PostGIS 等）进行转换和交互。
12. 跨平台兼容性
    * 由于是基于 Java 开发的，因此可以在任何支持 Java 的平台上使用。
13. 社区和文档支持；比较活跃的

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

```xml

<dependencies>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-main</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-referencing</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

简单示例：读写shapefile文件

由于代码过多，参考单元示例 `shapefile/ShapefileReadExample.java` 和`shapefile/ShapefileWriterExample.java`

### Java实现 shape文件转成 geojson

```xml

<dependencies>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-geojson</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

代码示例: `ShapefileToGeoJSONExample.java`

### Java实现 shape文件转成 geojson

```xml

<dependencies>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-geojson</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>com.vividsolutions</groupId>
        <artifactId>jts</artifactId>
        <version>1.13</version>
    </dependency>
</dependencies>
```

代码示例: `GeoJSONToShapefileTest.java`

### Shapefile 与 GeoTools 的 CRS 功能集成，进行坐标转换

> Shapefile 与 GeoTools 的 CRS 功能集成，进行坐标转换

```xml

<dependencies>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <!--坐标转换-->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-epsg-hsql</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-referencing</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-geometry</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

代码示例: `CoordinateTransformExample.java`

### Shapefile 与 GeoTools 的 CRS 功能集成，进行投影操作

代码示例: `ProjectionExample.java`

### shapefile 集成 gt-swing 显示与交互

```xml

<dependencies>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-main</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-referencing</artifactId>
        <version>${geotools.version}</version>
    </dependency>

    <!--显示和交互,在 Swing 应用程序中显示 Shapefile 文件-->
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-swing</artifactId>
        <version>${geotools.version}</version>
    </dependency>
</dependencies>
```

代码示例: `ShapefileSwingExample.java`

