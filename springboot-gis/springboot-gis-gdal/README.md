# springboot-gis-gdal

## 介绍

    GDAL（Geospatial Data Abstraction Library）是一个用于处理地理空间数据的开源库。它提供了许多功能，包括读取、写入、转换和分析栅格和矢量数据，执行空间分析等。

## GDAL 安装

### GDAL 安装(推荐)

* 推荐通过 [gisinternals](https://www.gisinternals.com/release.php) 安装 GDAL

### GDAL官网安装(不推荐)

要在Java中使用GDAL，您需要按照以下步骤进行设置：

1. 官方网下载GDAL库进行安装：https://gdal.org/
2. 设置Java绑定：GDAL提供了Java绑定，以JNI（Java Native Interface）库的形式提供。在安装GDAL之后，您需要通过编译JNI库来设置Java绑定。此步骤可能因操作系统而异。
3. 创建Java项目：在您喜欢的IDE中或使用您喜欢的构建系统设置一个Java项目。确保在项目的类路径中包含GDAL Java绑定。
4. 导入GDAL类：在Java代码中，您可以导入GDAL类以访问GDAL功能。

## Java 使用 GDAL 实例

## 引入依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.gdal</groupId>
        <artifactId>gdal</artifactId>
        <version>3.8.0</version>
    </dependency>
</dependencies>
```

简单示例：

```java
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;

public class GDALRasterReadExample {
    public static void main(String[] args) {
        // 初始化GDAL
        gdal.AllRegister();

        // 打开栅格数据文件
        String rasterFile = "path/to/raster.tif";
        Dataset rasterDataset = gdal.Open(rasterFile);

        // 获取栅格数据信息
        int width = rasterDataset.GetRasterXSize();
        int height = rasterDataset.GetRasterYSize();
        int bands = rasterDataset.getRasterCount();

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("Bands: " + bands);

        // 关闭栅格数据文件
        rasterDataset.delete();
    }
}
```

## 矢量数据文件，都包含了哪些数据？

矢量数据文件主要用于存储地理空间要素的几何信息和属性信息，它们通常包含但不限于以下几个关键部分：

1. 几何信息（Geometry）:
    * 点（Points）: 表示单个位置，由一对坐标（经度和纬度）定义。
    * 线（Lines / LineStrings）: 由一系列有序的点构成，描述路径或边界。
    * 多线（MultiLineStrings）: 包含多条线，每条线独立定义。
    * 面（Polygons）: 闭合的线条，定义了一个区域，可以有多边形孔洞。
    * 多面（MultiPolygons）: 包含多个独立的面，每个面可以有多个环。
    * 多点（MultiPoints）: 包含一组未连接的点。
    * 混合类型（GeometryCollections）: 包含不同类型的几何对象集合。
2. 属性信息（Attributes or Attributes Table）:
    * 每个矢量要素（如一个点、一条线或一个面）都可以关联一系列属性，如名称、分类、颜色、人口数量等。这些属性为几何对象提供了额外的描述信息。
3. 元数据（Metadata）:
    * 描述数据集的整体信息，包括创建者、创建日期、数据来源、更新历史、版权信息、数据质量等。
4. 投影信息（Projection Definition）:
    * 定义了数据的坐标参考系统（CRS），确保矢量数据能够在正确的地理坐标系中被正确显示和分析。
5. 拓扑关系（Topology）:
    * 在某些矢量格式中，可能包含要素之间的拓扑关系，如共享边界、邻接关系等，这对于空间分析尤为重要。
6. 索引信息（Indexes）:
    * 为了提高查询效率，矢量数据文件可能包含空间或属性索引。
7. 样式信息（Styles or Rendering Instructions）:
    * 虽然不是所有矢量数据文件都包含样式信息，但有些格式允许存储要素的显示样式，如颜色、线宽、填充图案等。
8. 常见的矢量数据文件格式有：
    * Shapefile (.shp): 包含.shp（几何信息）、.dbf（属性表）、.shx（索引）、.prj（投影信息）等多个文件。
    * GeoJSON (.geojson): 一种基于JSON的地理空间数据交换格式，易于人阅读和机器解析。
    * KML/KMZ (.kml, .kmz): Google Earth支持的格式，用于展示地理要素和样式。
    * GML (.gml): Geography Markup Language，是一种基于XML的地理信息编码标准。
    * GeoPackage (.gpkg): 一种现代的、轻量级的、支持多种数据类型（矢量、栅格、属性表）的数据库容器格式。

以上各部分的具体内容和组织方式会根据矢量数据文件的格式有所不同。

## 遥感影像文件，都包含了哪些数据？

遥感影像文件通常包含多种类型的数据，用以存储和描述影像的像素信息、地理位置信息以及元数据等。以下是一些主要的数据组成部分：

1. 图像数据（Pixel Data）:
    * 像素数据（Pixel Data）:这是最核心的部分，存储了每个像素的位置及其对应的辐射值或反射率等信息。每个像素代表地表某一点的辐射亮度值或反射率，像素值可以是单波段（如灰度图）或多波段（如真彩色或假彩色合成图像）。
    * 波段信息（Band Information）:对于多波段影像，每个波段通常对应不同的光谱范围，如可见光、近红外、热红外等。多波段影像中的每个波段通常对应不同的电磁谱范围，如红、绿、蓝、近红外等，每个波段的数据和属性（如波长范围、数据类型）都会被记录。
2. 地理定位信息（Georeferencing Information）:
    * 地理变换参数（GeoTransform）: 一组六参数的地理变换矩阵，用于将影像的像素坐标转换为地理坐标（如经纬度）。
    * 投影信息（Projection）: 定义了影像所采用的地图投影方式，如WGS84、UTM、Albers等，这对于准确地在地球表面定位影像至关重要。
    * 坐标系统（Coordinate System）: 描述了影像数据的坐标参照系，如EPSG代码。
3. 元数据（Metadata）:
    * 影像头文件（Header）: 如TIFF文件中的.tif是图像数据，而.tfw或.tab等则是包含地理配准信息的头文件。
    * 影像属性（Image Attributes）: 包括影像的采集日期、时间、传感器类型（如Landsat、Sentinel等）、分辨率、云覆盖情况、太阳角度、传感器参数等。
    * 处理历史（Processing History）: 记录了影像从原始数据到最终产品的所有处理步骤和算法。
4. 图像头文件（Header File）:
    * 对于某些格式（如ENVI的HDR文件、TIFF的TFW/TFW2世界文件），会有一个单独的头文件或世界文件，存储了地理定位和元数据信息，以便其他软件能够识别和正确显示影像。
5. 质量指标（Quality Indicators）:
    * 包含关于数据质量的评估，如噪声水平、云遮挡情况、数据缺失区域等。帮助用户评估影像的适用性和准确性。
6. 附加信息（Auxiliary Data）:
    * 一些影像文件可能包含额外的信息，如镶嵌图层、掩膜数据（标识有效数据区域）、数字高程模型（DEM）等，用于辅助分析或增强影像的实用性。
7. 注释和标签（Annotations and Tags）:
    * 提供对影像内容的文本描述，有助于快速理解影像覆盖的区域和特征。

综上所述，遥感影像文件不仅仅是图像本身，还包括了一系列确保其在空间上可定位、在科学上可解释的附加信息。这些数据共同构成了一个完整、可信赖的遥感数据集。