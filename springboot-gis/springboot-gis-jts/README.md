# springboot-gis-jts

**说明**

locationtech.jts 是一个 Java 库，提供了用于处理和操作地理空间数据的功能。它是一个开源项目，由 Eclipse LocationTech 社区维护和支持。
该库实现了 OGC（Open Geospatial Consortium）规范中定义的几何对象模型和操作，用于表示和处理点、线、面等地理空间数据。

## jts 库的主要特点和功能

以下是一些 locationtech.jts 库的主要特点和功能：

1. 几何对象模型：locationtech.jts 实现了几何对象模型，包括点（Point）、线（LineString）、面（Polygon）等。这些几何对象可以表示和操作地理空间数据。
2. 空间关系和操作：该库提供了许多用于计算和操作几何对象的方法，如计算距离、交集、并集、缓冲区等。这些方法可以用于解决地理空间分析和处理问题。
3. WKT 和 WKB 的支持：locationtech.jts 支持将几何对象与 WKT（Well-Known Text）和 WKB（Well-Known Binary）之间进行转换。WKT
   是一种文本格式，用于表示几何对象，而 WKB 是二进制格式。
4. 空间索引：该库提供了空间索引的实现，如 R 树和四叉树。这些索引结构可以加速空间查询和过滤操作，提高查询效率。
5. 地理空间算法：locationtech.jts 实现了许多地理空间算法，如点在多边形内判断、多边形拓扑关系计算等。这些算法可以用于解决地理空间分析和计算问题。

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