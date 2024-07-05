# springboot-gis-jts

- [https://github.com/locationtech/jts](https://github.com/locationtech/jts)

**说明**

locationtech.jts 是一个 Java 库，提供了用于处理和操作地理空间数据的功能。它是一个开源项目，由 Eclipse LocationTech 社区维护和支持。
该库实现了 OGC（Open Geospatial Consortium）规范中定义的几何对象模型和操作，用于表示和处理点、线、面等地理空间数据。

## jts 库的主要特点和功能

以下是一些 locationtech.jts 库的主要特点和功能：

1. 空间数据模型：支持点（Point）、线（LineString）、多边形（Polygon）、几何集合（GeometryCollection）等多种基本的几何类型，以及它们的二维和三维变体。
2. 几何操作：提供了几何对象的创建、读取、更新和删除（CRUD）操作，包括几何对象的合并、差异、交集、缓冲区分析等。
3. 空间关系分析：可以判断两个几何对象之间的空间关系，如相交（Intersects）、包含（Contains）、相等（Equals）、接触（Touches）、计算距离、交集、并集、缓冲区等。
4. 几何变换：可以进行几何对象的平移、旋转、缩放等几何变换操作。
5. 空间索引：支持使用R-Tree等空间索引结构，提高空间查询效率
6. 拓扑操作：提供了构建和操作拓扑关系的功能，如构建拓扑图、进行拓扑规则检查等。
7. 精确计算：采用Robust Predicates算法，确保在浮点运算中处理几何对象时的高精度和稳定性，避免了常见的浮点误差问题。
8. WKT/WKB支持：支持Well-Known Text (WKT) 和 Well-Known Binary (WKB) 格式的编码与解码，便于几何对象的序列化与反序列化，以及与其他GIS软件的交互。
9. 空间参考系（Coordinate Reference Systems, CRS）支持：虽然核心JTS库本身不直接处理坐标系转换，但与之相关的项目（如GeoTools）结合使用时，可以实现不同坐标系间的数据转换。
10. 空间分析工具：包括计算最近点、最短路径分析、凸包计算、多边形剖分等高级分析功能。
11. 几何校正与修复：提供了检测和修正几何对象中的自相交、重复点等问题的工具，保证几何对象的拓扑正确性。
12. 空间谓词计算：实现了DE-9IM模型，这是一种描述两个几何对象之间空间关系的严格模型，适用于复杂的地理空间逻辑判断。
13. 几何图形裁剪与裁切：支持使用多边形对其他几何对象进行裁剪，获取它们的交集部分，或者执行线与多边形的裁切操作。
14. 几何对象的几何属性计算：如计算面积、长度、中心点、边界框等。
15. 与标准兼容：遵循并实现了Open Geospatial Consortium (OGC) 的多个开放标准，如简单要素访问规范(Simple Feature Access Specification)，确保了与其他GIS标准软件和数据的互操作性。
16. 扩展性和可集成性：JTS设计为高度模块化，易于与其他Java库（如Hibernate Spatial用于数据库集成，GeoTools用于更广泛的GIS功能）集成，便于开发者根据需求定制和扩展功能。
17. 地理空间算法：locationtech.jts 实现了许多地理空间算法，如点在多边形内判断、多边形拓扑关系计算等。这些算法可以用于解决地理空间分析和计算问题。

locationtech.jts 是一个功能强大且广泛使用的地理空间处理库，适用于许多领域，包括地理信息系统（GIS）、地理空间分析、位置服务等。

## 实例

依赖引入

```xml

<dependencies>
    <!--jts-->
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

代码示例

```java
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * @author: zhengja
 * @since: 2023/11/07 14:14
 */
public class JTSWKTConversionTest {

    public static void main(String[] args) {
        // 创建点
        Point point = createPoint(10, 20);
        testGeometryConversion(point);

        // 创建线
        LineString lineString = createLineString(new Coordinate(0, 0), new Coordinate(10, 10), new Coordinate(20, 0));
        testGeometryConversion(lineString);

        // 创建面
        Polygon polygon = createPolygon(new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0));
        testGeometryConversion(polygon);

        // 创建多面
        Polygon hole = createPolygon(new Coordinate(2, 2), new Coordinate(2, 8), new Coordinate(8, 8), new Coordinate(8, 2), new Coordinate(2, 2));
        MultiPolygon multiPolygon = createMultiPolygon(polygon, hole);
        testGeometryConversion(multiPolygon);

        // 创建多边形
        Polygon simplePolygon = createPolygon(new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0));
        testGeometryConversion(simplePolygon);

        // 创建矩形边界
        Polygon envelopePolygon = createPolygon(new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0));
        Envelope envelope = new Envelope(0, 10, 0, 10);
        testGeometryConversion(envelopePolygon, envelope);
    }

    // 创建点
    private static Point createPoint(double x, double y) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    // 创建线
    private static LineString createLineString(Coordinate... coordinates) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createLineString(coordinates);
    }

    // 创建面
    private static Polygon createPolygon(Coordinate... coordinates) {
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell);
    }

    // 创建多面
    private static MultiPolygon createMultiPolygon(Polygon... polygons) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createMultiPolygon(polygons);
    }

    // 测试几何对象和 WKT 的互相转换
    private static void testGeometryConversion(Geometry geometry) {
        // 将 Geometry 对象转换为 WKT
        String wkt = geometryToWKT(geometry);
        System.out.println("Geometry: " + geometry);
        System.out.println("WKT: " + wkt);

        // 将 WKT 转换为 Geometry 对象
        Geometry convertedGeometry = wktToGeometry(wkt);
        System.out.println("Converted Geometry: " + convertedGeometry);
        System.out.println("----------------------------------------");
    }

    // 测试 Envelope 对象的边界框与 WKT 的转换
    private static void testGeometryConversion(Polygon polygon, Envelope envelope) {
        // 将 Polygon 对象转换为 WKT
        String polygonWKT = geometryToWKT(polygon);
        System.out.println("Polygon: " + polygon);
        System.out.println("Polygon WKT: " + polygonWKT);

        // 将 Envelope 对象转换为 WKT
        String envelopeWKT = envelopeToWKT(envelope);
        System.out.println("Envelope: " + envelope);
        System.out.println("Envelope WKT: " + envelopeWKT);

        // 将 Envelope 的 WKT 转换为 Polygon 对象
        Geometry convertedGeometry = wktToGeometry(envelopeWKT);
        System.out.println("Converted Geometry: " + convertedGeometry);
        System.out.println("----------------------------------------");
    }

    private static String envelopeToWKT(Envelope envelope) {
        StringBuilder sb = new StringBuilder();
        sb.append("POLYGON ((");
        sb.append(envelope.getMinX()).append(" ").append(envelope.getMinY()).append(", ");
        sb.append(envelope.getMinX()).append(" ").append(envelope.getMaxY()).append(", ");
        sb.append(envelope.getMaxX()).append(" ").append(envelope.getMaxY()).append(", ");
        sb.append(envelope.getMaxX()).append(" ").append(envelope.getMinY()).append(", ");
        sb.append(envelope.getMinX()).append(" ").append(envelope.getMinY());
        sb.append("))");
        return sb.toString();
    }

    // 将 Geometry 对象转换为 WKT
    private static String geometryToWKT(Geometry geometry) {
        WKTWriter writer = new WKTWriter();
        return writer.write(geometry);
    }

    // 将 WKT 转换为 Geometry 对象
    private static Geometry wktToGeometry(String wkt) {
        WKTReader reader = new WKTReader();
        try {
            return reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
```