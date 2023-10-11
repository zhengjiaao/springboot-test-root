# geotools-gt-geojson

**说明**

gt-geojson 模块是 GeoTools 库的一个扩展模块，用于处理 GeoJSON 格式的地理数据。

## gt-geojson 模块的主要功能

以下是 gt-geojson 模块的一些主要功能：

1. 读取和写入 GeoJSON 文件：gt-geojson 提供了读取和写入 GeoJSON 文件的功能。您可以使用 GeoJSONDataStore 类来读取 GeoJSON
   文件，并将其转换为 GeoTools 的特征集（FeatureCollection）对象。同样地，您也可以使用 GeoJSONDataStore 来将 GeoTools
   的特征集对象写入为 GeoJSON 文件。
2. 解析和生成 GeoJSON 数据：gt-geojson 提供了解析和生成 GeoJSON 数据的功能。您可以使用 GeoJSONReader 类来解析 GeoJSON
   字符串，并将其转换为 GeoTools 的特征集对象。反之，您可以使用 GeoJSONWriter 类将 GeoTools 的特征集对象转换为 GeoJSON 字符串。
3. 支持的几何类型：gt-geojson
   支持处理多种几何类型，例如点（Point）、线（LineString）、多边形（Polygon）、多点（MultiPoint）、多线（MultiLineString）和多多边形（MultiPolygon）等。
4. 属性字段处理：gt-geojson 可以处理 GeoJSON 中的属性字段。它能够将属性字段与几何对象进行关联，并提供读取和写入属性字段的能力。
5. 投影转换：gt-geojson 允许进行投影转换。您可以指定源数据的投影（CRS），并使用合适的转换工具将其转换为目标投影。
6. 扩展功能：gt-geojson 还提供了其他一些功能，如对拓扑关系的分析、空间查询和过滤等。

这些是 gt-geojson 模块的一些主要功能。通过使用该模块，您可以方便地读取、写入和处理 GeoJSON 格式的地理数据，并与 GeoTools
库的其他功能进行整合。

## gt-geojson 模块功能实例

### 读取和写入 GeoJSON 文件

由于代码过多，参考单元测试类：`ReadGeoJSONExample.java`和`WriteGeoJSONExample.java`

### 解析和生成 GeoJSON 数据

由于代码过多，参考单元测试类：`GeoJSONUtilTest.java`