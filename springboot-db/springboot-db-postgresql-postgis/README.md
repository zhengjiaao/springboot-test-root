# springboot-db-postgresql-postgis

    PostGIS 是一个开源的GIS数据库，它基于PostgreSQL数据库，提供了PostgreSQL数据库的所有功能，并添加了地理空间数据支持。

    注：PostGIS 是PostgreSQL数据库的一个扩展，它提供了对地理空间数据的支持。

框架搭建：springboot+jpa+postgis

## 安装PostgreSQL

- [PostgreSQL官网](https://www.postgresql.org/)
- [PostgreSQL安装教程](https://www.postgresql.org/download/)
- [PostgreSQL windows安装教程](https://www.postgresql.org/download/windows/)

## 安装PostGIS

- [PostGIS官网](https://postgis.net/)
- [PostGIS安装教程](https://postgis.net/install/)
- [PostGIS windows安装教程](https://postgis.net/documentation/getting_started/install_windows/)
- [PostGIS windows安装教程-博客详解](https://blog.csdn.net/qq_42402854/article/details/135697475)

重点：[PostGIS windows安装教程-博客详解](https://blog.csdn.net/qq_42402854/article/details/135697475)

## 创建PostGIS数据库

在一个`PostgreSQL`普通数据库下创建一个`PostGIS`数据库。只需要在`PostgreSQL`数据库下执行以下SQL语句即可。进行安装扩展时，需要先创建一个用户，并授予超级用户权限。

```sql
CREATE
USER postgis_user WITH PASSWORD 'postgis_password';
CREATE
DATABASE postgis_db;
GRANT ALL PRIVILEGES ON DATABASE
postgis_db TO postgis_user;
GRANT ALL PRIVILEGES ON ALL
TABLES IN SCHEMA public TO postgis_user;
ALTER
USER postgis_user WITH SUPERUSER; -- 授予超级用户权限,只有超级用户才能安装扩展

cd
postgis_db;

-- 安装扩展（postgis）
-- 创建空间数据库插件扩展
CREATE
EXTENSION postgis;               --用于创建空间数据库的插件，仅支持矢量数据扩展
CREATE
EXTENSION postgis_topology;      --使空间数据库支持拓扑检查
CREATE
EXTENSION fuzzystrmatch;         --使空间数据库支持地理编码的模糊匹配
CREATE
EXTENSION postgis_tiger_geocoder;  --使空间数据库支持地理编码
CREATE
EXTENSION postgis_raster;        --使空间数据库支持栅格数据扩展
CREATE
EXTENSION address_standardizer;  --使空间数据库支持地址标准化
CREATE
EXTENSION ogr_fdw;               --使空间数据库支持不同数据库之间的跨库操作
CREATE
EXTENSION pgrouting;             --使空间数据库支持网络分析
CREATE
EXTENSION pointcloud;            --使空间数据库支持点云数据存储
CREATE
EXTENSION pointcloud_postgis;    --使空间数据库支持点云数据操作
CREATE
EXTENSION postgis_sfcgal;        --使空间数据库支持2D和3D的数据操作
-- 移除相关插件扩展
DROP
EXTENSION 插件名称;
DROP
EXTENSION postgis;
-- 更新插件扩展
ALTER
EXTENSION 插件名称
update to "版本号";
ALTER
EXTENSION postgis
update to "3.0.1";

-- 验证是否安装 postgis 扩展
SELECT postgis_full_version();
```

### PostGIS 常用函数

- [PostGIS 常用函数 来源参考](https://blog.csdn.net/weixin_43850384/article/details/134418223)
- [PostGIS 常用函数 几何图形的练习](https://blog.csdn.net/weixin_43850384/article/details/134430449)

### 通用的空间函数

```sql
-- 获取空间数据集的文本表示
SELECT ST_AsText(geom)
FROM geometries
WHERE name = 'Point';
-- 获取空间数据集的文本表示
SELECT ST_AsText(geom)
FROM geometries
WHERE name = 'Linestring';
-- 获取空间数据集的文本表示，将返回两个多边形图形的信息（在ST_AsText列中）：
SELECT ST_AsText(geom)
FROM geometries
WHERE name LIKE 'Polygon%';

-- 获取空间数据集的坐标系
SELECT ST_SRID(geom)
FROM geometries
WHERE name = 'Point';
-- 设置空间数据集的坐标系
SELECT ST_SetSRID(geom, 4326)
FROM geometries
WHERE name = 'Point';
```

### 点（Points）

用于处理点的一些特定空间函数：

* ST_X(geometry) —— 返回X坐标
* ST_Y(geometry) —— 返回Y坐标
* ST_Z(geometry) —— 返回Z坐标
* ST_M(geometry) —— 返回M信息

示例：

```sql
-- 读取一个点图形的坐标值
SELECT ST_X(geom), ST_Y(geom)
FROM geometries
WHERE name = 'Point';

-- 纽约市地铁站（nyc_subway_stations）表是一个以点表示的数据集,查询将返回一个点图形数据（在ST_AsText列中）：
SELECT name, ST_AsText(geom)
FROM nyc_subway_stations LIMIT 1;
```

### 线串（Linestring）

用于处理线串的一些特定空间函数：

* ST_Length(geometry) —— 返回线串的长度
* ST_StartPoint(geometry) —— 将线串的第一个坐标作为点返回
* ST_EndPoint(geometry） —— 将线串的最后一个坐标作为点返回
* ST_NPoints(geometry) —— 返回线串的坐标数量

示例：

```sql
-- 计算线串的长度
SELECT ST_Length(geom)
FROM geometries
WHERE name = 'Linestring';
```

### 多边形（Polygon）

用于处理多边形一些特定空间函数：

* ST_Area(geometry) —— 返回多边形的面积
* ST_NRings(geometry) —— 返回多边形中环的数量（通常为1个，其他是孔）
* ST_ExteriorRing(geometry) —— 以线串的形式返回多边形最外面的环
* ST_InteriorRingN(geometry, n) —— 以线串形式返回指定的内部环
* ST_Perimeter(geometry) —— 返回所有环的长度

示例：

```sql
-- 使用空间函数计算多边形的面积
SELECT name, ST_Area(geom)
FROM geometries
WHERE name LIKE 'Polygon%';
```

### 图形集合（Collection）

有四种图形集合（Collection）类型，它们将多个简单几何图形组合为图形集合：

1. MultiPoint —— 点集合
2. MultiLineString —— 线串集合
3. MultiPolygon —— 多边形集合
4. GeometryCollection —— 由任意几何图形（包括其他GeometryCollection）组成的异构集合

用于处理图形集合的一些特定空间函数：

* ST_NumGeometries(geometry) —— 返回集合中的组成部分的数量
* ST_GeometryN(geometry, n) —— 返回集合中指定的组成部分
* ST_Area(geometry) —— 返回集合中所有多边形组成部分的总面积
* ST_Length(geometry) —— 返回所有线段组成部分的总长度

几何图形集合包含一个多边形和一个点：

```sql
SELECT name, ST_AsText(geom)
FROM geometries
WHERE name = 'Collection';

```

## 几何图形输入和输出

    在数据库中，几何图形（Geometry）以仅供PostGIS使用的格式存储在磁盘上。为了让外部程序插入和检索有用的几何图形信息，需要将它们转换为其他应用程序可以理解和解析的格式。

PostGIS支持以多种格式进行几何图形的输入和输出：

```text
1、Well-known text（WKT）

ST_GeomFromText(text, srid) —— 返回geometry
ST_AsText(geometry) —— 返回text
ST_AsEWKT(geometry) —— 返回text

2、Well-known binary（WKB）

ST_GeomFromWKB(bytea) —— 返回geometry
ST_AsBinary(geometry) —— 返回bytea
ST_AsEWKB(geometry) —— 返回bytea

3、Geographic Mark-up Language（GML）

ST_GeomFromGML(text) —— 返回geometry
ST_ASGML(geometry) —— 返回text

4、Keyhole Mark-up Language（KML）

ST_GeomFromKML(text) —— 返回geometry
ST_ASKML(geometry) —— 返回text

5、GeoJson

ST_AsGeoJSON(geometry) —— 返回text

6、Scalable Vector Graphics(SVG）

ST_AsSVG(geometry) —— 返回text
```

除了用于各种格式（WKT、WKB、GML、KML、JSON、SVG、MVT等）的输出函数外，PostGIS还有各种格式（WKT、WKB、GML、KML、JSON等）的输入函数。

使用GML输入和输出JSON的示例：

```sql
SELECT ST_AsGeoJSON(ST_GeomFromGML('<gml:Point><gml:coordinates>1,1</gml:coordinates></gml:Point>'));

```

## 空间关系

- [PostGIS 空间关系 来源参考](https://blog.csdn.net/weixin_43850384/article/details/134722441)

OGC标准定义了以下一组用于比较几何图形的方法：
1. ST_Equals(geometry A, geometry B)用于测试两个图形的空间相等性。
2. ST_Intersects、ST_Crosses和ST_Overlaps都用于测试两个几何图形内部是否相交。
3. ST_Touches(geometry A, geometry B)测试两个几何图形是否在它们的边界上接触，但在它们的内部不相交。
4. ST_Within(geometry A, geometry B)和ST_Contains()测试一个几何图形是否完全位于另一个几何图形内。第一个几何图形完全位于第二个几何图形内,则ST_Within返回TRUE ； 第二个几何图形B完全包含在第一个几何图形A内，则ST_Contains(geometry A, geometry B)返回TRUE
5. ST_Distance和ST_DWithin 用于测试两个几何图形之间的距离(找到这个物体周围距离它X的所有其他物体)。ST_Distance(geometry A, geometry B)计算两个几何图形之间的最短距离，并将其作为浮点数返回。ST_DWithin()函数提供了一个基于索引加速的功能，两个几何图形之间的距离是否在某个范围之内，例如：这对于"在距离道路500米的缓冲区内有多少棵树?"这样的问题很有用，你不必计算实际的缓冲区，只需测试距离关系即可。


示例

```sql
SELECT name, geom, ST_AsText(geom)
FROM nyc_subway_stations
WHERE name = 'Broad St';

SELECT name
FROM nyc_subway_stations
WHERE ST_Equals(geom, '0101000020266900000EEBD4CF27CF2141BC17D69516315141');
```

## QGis 使用 PostGIS

