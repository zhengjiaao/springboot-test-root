# springboot-gis-proj4J

**说明**

Proj4J 是一个用于地理坐标系和投影坐标系转换的 Java 库，它提供了对 Proj.4 库的封装。
Proj.4 是一个广泛使用的坐标转换库，可以实现不同地理坐标系和投影坐标系之间的转换。
除了坐标转换功能，locationtech.proj4j 还提供了其他功能，如地理空间索引、几何运算和坐标系定义等。

## 实例

引入依赖

```xml

<dependencies>
    <!--proj4j-->
    <dependency>
        <groupId>org.locationtech.proj4j</groupId>
        <artifactId>proj4j</artifactId>
        <version>1.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.locationtech.proj4j</groupId>
        <artifactId>proj4j-epsg</artifactId>
        <version>1.3.0</version>
    </dependency>

    <!-- JTS Topology Suite -->
    <dependency>
        <groupId>org.locationtech.jts</groupId>
        <artifactId>jts-core</artifactId>
        <version>1.19.0</version>
    </dependency>
    <dependency>
        <groupId>org.locationtech.jts</groupId>
        <artifactId>jts-io</artifactId>
        <version>1.19.0</version>
        <type>pom</type>
    </dependency>
    <dependency>
        <groupId>org.locationtech.jts.io</groupId>
        <artifactId>jts-io-common</artifactId>
        <version>1.19.0</version>
    </dependency>
</dependencies>
```

实例1：坐标转换器

```java
public class Proj4JExample {

    public static void main(String[] args) {
        // 定义源坐标系和目标坐标系的 EPSG 编码
        String sourceCrsCode = "EPSG:4326"; // WGS84 经纬度坐标系
        String targetCrsCode = "EPSG:3857"; // Web Mercator 投影坐标系

        // 创建坐标转换器
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem sourceCrs = crsFactory.createFromName(sourceCrsCode);
        CoordinateReferenceSystem targetCrs = crsFactory.createFromName(targetCrsCode);
        CoordinateTransformFactory transformFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = transformFactory.createTransform(sourceCrs, targetCrs);

        // 定义源坐标
        double sourceX = -122.419416;
        double sourceY = 37.774929;

        // 进行坐标转换
        ProjCoordinate sourceCoord = new ProjCoordinate(sourceX, sourceY);
        ProjCoordinate targetCoord = new ProjCoordinate();
        transform.transform(sourceCoord, targetCoord);

        // 输出目标坐标
        double targetX = targetCoord.x;
        double targetY = targetCoord.y;
        System.out.println("Target Coordinate: " + targetX + ", " + targetY);
        //输出结果：Target Coordinate: -1.3627667052329928E7, 4547679.438563918
    }
}

```