# springboot-gis-supermap-sqlite

- [Sqlite](https://www.sqlite.org/index.html)
- [SpatiaLite](https://www.gaia-gis.it/fossil/libspatialite/index)
- [Spatialite-gaia-sins](https://www.gaia-gis.it/gaia-sins/)


sqlite3 +SpatiaLite 空间数据处理并不特殊，只是众多 计算分支中的一个分支：空间数据具有战略意义和普遍性，因此不能局限于特定应用.


## 安装sqlite3数据库

- [sqlite3-下载](https://www.sqlite.org/download.html)

```shell
# sqlite3 加载扩展
SELECT load_extension('path_to_extension');
```

## 安装sqlite3 SpatiaLite扩展库

- [sqlite3-SpatiaLite扩展库-下载](https://www.gaia-gis.it/fossil/libspatialite/index)

步骤：
1、解压指定到目录下
2、配置环境变量

```shell
# 加载
SELECT load_extension('mod_spatialite');
SELECT spatialite_version();

# 测试
sqlite3 .\上海润和总部园.udbx
SELECT load_extension('mod_spatialite');
SELECT spatialite_version();
SELECT * FROM geometry_columns;
SELECT name, ST_AsText(smgeometry) FROM '上海润和总部园';
```

创建数据库文件：

```shell
sqlite3 db.sqlite3

# 创建一个空间数据表并添加空间列：
CREATE TABLE points (id INTEGER PRIMARY KEY, geom POINT);
SELECT AddGeometryColumn(4326, 'geom', 4326, 'POINT', 2);

# 插入几个点并创建一个空间索引：
INSERT INTO points (geom) VALUES (MakePoint(1.0, 1.0, 4326));
INSERT INTO points (geom) VALUES (MakePoint(2.0, 2.0, 4326));
CREATE SPATIAL INDEX idx_points_geom ON points(geom);

# 空间查询，例如查找距离某个点最近的点：
# 查询会返回距离点(1.5, 1.5)最近的点的ID和其距离。
SELECT id, Distance(geom, MakePoint(1.5, 1.5, 4326)) AS dist
FROM points
ORDER BY dist ASC
LIMIT 1;

```

## 命令行操作

简单操作：

```shell
# 打开SQLite命令行工具: 验证是否安装成功
sqlite3

# 打开数据库文件
sqlite3 db.sqlite3

# 查看数据库表
.tables

# 查看数据库表结构
.schema

# 执行SQL查询：查看数据
SELECT * FROM 表名称;

# 执行SQL查询：查看数据
SELECT column1,column2 FROM 表名称;

# 查看表结构
SELECT sql FROM sqlite_master WHERE type = 'table' AND tbl_name = '表名称';

# 查看所有表
SELECT tbl_name FROM sqlite_master WHERE type = 'table';

# 关闭DB文件，退出SQLite命令行工具
.quit
```

读取空间数据：

```shell
# 查看空间数据表
SELECT * FROM geometry_columns;
# 查看空间数据表结构
SELECT * FROM geometry_columns WHERE f_table_name = '表名称';
# 查看空间数据表空间列结构
SELECT * FROM geometry_columns WHERE f_table_name = '表名称' AND f_geometry_column = '空间列名称';
# 查看空间数据表空间列数据
SELECT * FROM 表名称;

# 查询表中的几何数据及其WKT表示：
SELECT geometry_column, ST_AsText(geometry_column) FROM '表名称';

# 根据空间关系查询表中符合条件的几何数据：（相交的）查询空间数据与几何数据是否相交
SELECT * FROM YourTableName WHERE ST_Intersects(geometry_column, ST_GeomFromText('表名称', SRID));

# 空间函数 示例：
SELECT * FROM 表名称 WHERE ST_Intersects(geometry_column, ST_GeomFromText('POINT(x y)', 4326));
SELECT * FROM 表名称 WHERE ST_Intersects(geometry_column, ST_GeomFromText('POLYGON((x1 y1, x2 y2, x3 y3, x4 y4, x1 y1))', 4326));
SELECT * FROM 表名称 WHERE ST_Intersects(geometry_column, ST_GeomFromText('MULTIPOLYGON(((x1 y1, x2 y2, x3 y3, x4 y4, x1 y1)), ((x1 y1, x2 y2, x3 y3, x4 y4, x1 y1)))', 4326));
SELECT * FROM 表名称 WHERE ST_Intersects(geometry_column, ST_GeomFromText('LINESTRING(x1 y1, x2 y2)', 4326));
SELECT * FROM 表名称 WHERE ST_Intersects(geometry_column, ST_GeomFromText('MULTILINESTRING((x1 y1, x2 y2), (x1 y1, x2 y2))', 4326));
```

处理geometry_columns表：

```shell
sqlite> SELECT sql FROM sqlite_master WHERE type = 'table' AND tbl_name = 'geometry_columns';
CREATE TABLE geometry_columns (
f_table_name TEXT NOT NULL, # 表名
f_geometry_column TEXT NOT NULL, # 几何列
geometry_type INTEGER NOT NULL, # 几何类型
coord_dimension INTEGER NOT NULL, # 坐标维度
srid INTEGER NOT NULL,  # srid
spatial_index_enabled INTEGER NOT NULL,
CONSTRAINT pk_geom_cols PRIMARY KEY (f_table_name, f_geometry_column),
CONSTRAINT fk_gc_srs FOREIGN KEY (srid) REFERENCES spatial_ref_sys (srid),
CONSTRAINT ck_gc_rtree CHECK (spatial_index_enabled IN (0,1,2)))

sqlite> SELECT * FROM geometry_columns;
上海润和总部园|smgeometry|6|2|4326|0
上海润和总部园_1|smindexkey|3|2|4326|0

sqlite> SELECT * FROM geometry_columns WHERE f_table_name = '上海润和总部园';
上海润和总部园|smgeometry|6|2|4326|0

sqlite> SELECT * FROM geometry_columns WHERE f_table_name = '上海润和总部园';

sqlite> SELECT * FROM geometry_columns WHERE f_table_name = '上海润和总部园' AND f_geometry_column = 'smgeometry';
上海润和总部园|smgeometry|6|2|4326|0

sqlite> SELECT name, ST_AsText(smgeometry) FROM '上海润和总部园'; # 需要SQLite安装"SpatiaLite"扩展中的函数ST_AsText
sqlite> SELECT name,HEX(smgeometry) FROM '上海润和总部园'; # 以文本形式获取几何数据
```


