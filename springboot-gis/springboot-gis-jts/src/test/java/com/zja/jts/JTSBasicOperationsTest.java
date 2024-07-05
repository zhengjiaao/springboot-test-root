package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.io.*;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

/**
 * @Author: zhengja
 * @Date: 2024-07-04 16:55
 */
public class JTSBasicOperationsTest {

    private final GeometryFactory factory = new GeometryFactory();

    /**
     * 创建几何对象(标准的Geometry对象): 点、线、面、多点、多线、多面、多几何
     */
    @Test
    public void CreatGeometry_test() {
        // 创建点对象
        Point point = factory.createPoint(new Coordinate(100, 200));
        System.out.println("Point: " + point);

        // 创建线段对象（注意：LineSegment并非Geometry接口的直接子类，但可作为构造其他几何体的组件）
        Coordinate[] lineSegmentCoords = new Coordinate[]{new Coordinate(0, 0), new Coordinate(10, 10)};
        LineSegment lineSegment = new LineSegment(lineSegmentCoords[0], lineSegmentCoords[1]);
        // 注意：直接使用不展示，因为LineSegment主要用于内部计算，不是标准的Geometry对象

        // 创建线对象
        Coordinate[] lineCoords = new Coordinate[]{new Coordinate(0, 0), new Coordinate(10, 10), new Coordinate(20, 20)};
        LineString line = factory.createLineString(lineCoords);
        System.out.println("LineString: " + line);

        // 创建多边形
        Coordinate[] polygonExterior = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        LinearRing exteriorRing = factory.createLinearRing(polygonExterior);
        Polygon polygon = factory.createPolygon(exteriorRing, null);
        System.out.println("Polygon: " + polygon);

        System.out.println("---------------");

        // 创建多点
        Point[] multiPoints = new Point[]{point};
        MultiPoint multiPoint = factory.createMultiPoint(multiPoints);
        System.out.println("MultiPoint: " + multiPoint);

        // 创建多线String
        LineString[] multiLineStrings = new LineString[]{line};
        MultiLineString multiLineString = factory.createMultiLineString(multiLineStrings);
        System.out.println("MultiLineString: " + multiLineString);

        // 创建多边形集合
        Polygon[] polygons = new Polygon[]{polygon};
        MultiPolygon multiPolygon = factory.createMultiPolygon(polygons);
        System.out.println("MultiPolygon: " + multiPolygon);

        System.out.println("---------------");

        // 创建几何集合
        Geometry[] geometries = new Geometry[]{point, line, polygon};
        GeometryCollection geometryCollection = factory.createGeometryCollection(geometries);
        System.out.println("GeometryCollection: " + geometryCollection);
    }

    /**
     * 几何对象读写（WKT/WKB）
     */
    @Test
    public void GeometryReadWrite_test() throws ParseException {
        // 创建GeometryFactory用于构造几何对象
        GeometryFactory factory = new GeometryFactory();

        // 创建一个示例点
        Point point = factory.createPoint(new Coordinate(100, 200));

        // WKT读写
        WKTWriter wktWriter = new WKTWriter();
        String wktOutput = wktWriter.write(point);
        System.out.println("WKT Output: " + wktOutput);

        WKTReader wktReader = new WKTReader();
        Geometry geometryFromWKT = wktReader.read(wktOutput);
        System.out.println("Geometry from WKT: " + geometryFromWKT);

        // WKB读写
        WKBWriter wkbWriter = new WKBWriter();
        byte[] wkbOutput = wkbWriter.write(point);

        // 假设这是从某处获得的WKB数据
        byte[] wkbData = wkbOutput;

        WKBReader wkbReader = new WKBReader();
        Geometry geometryFromWKB = wkbReader.read(wkbData);
        System.out.println("Geometry from WKB: " + geometryFromWKB);
    }

    /**
     * 空间关系判断: intersects、contains、within、equals、touches
     */
    @Test
    public void GeometryRelations_test() {
        // 创建GeometryFactory对象
        GeometryFactory factory = new GeometryFactory();

        // 定义多边形1的坐标: 坐标数组coordinates1，用于表示一个矩形多边形的顶点坐标序列。每个Coordinate对象代表多边形边界上的一个点，包含两个坐标值：经度（或X坐标）和纬度（或Y坐标）。按照顺时针顺序定义的坐标点，用于构建一个简单的矩形多边形。
        Coordinate[] coordinates1 = new Coordinate[]{new Coordinate(0, 0), // new Coordinate(0, 0)：多边形的左下角顶点，X坐标为0，Y坐标也为0。
                new Coordinate(0, 10), // new Coordinate(0, 10)：多边形的左上角顶点，X坐标不变仍为0，Y坐标增加到10。
                new Coordinate(10, 10), // new Coordinate(10, 10)：多边形的右上角顶点，X坐标增加到10，Y坐标保持为10。
                new Coordinate(10, 0), // new Coordinate(10, 0)：多边形的右下角顶点，X坐标保持为10，Y坐标回到0。
                new Coordinate(0, 0)}; // 最后再次返回起始点new Coordinate(0, 0)，形成闭合的多边形边界。

        // 定义多边形2的坐标，使其与多边形1相交(有重叠)
        // Coordinate[] coordinates2 = new Coordinate[]{new Coordinate(5, 5), new Coordinate(15, 5), new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)};
        // 定义多边形2的坐标，使其与多边形1不相交(没有有重叠)
        // Coordinate[] coordinates2 = new Coordinate[]{new Coordinate(20, 20), new Coordinate(20, 30), new Coordinate(30, 30), new Coordinate(30, 20), new Coordinate(20, 20)};
        // 定义多边形2的坐标，使其包含在多边形1中
        // Coordinate[] coordinates2 = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        // 定义多边形2的坐标，使其包含多边形1
        // Coordinate[] coordinates2 = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 20), new Coordinate(20, 20), new Coordinate(20, 0), new Coordinate(0, 0)};
        // 定义多边形2的坐标，使其与多边形1相等
        // Coordinate[] coordinates2 = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        // 定义多边形2的坐标，使其与多边形1有接触（共享边界）
        // 定义多边形2的坐标，使其与多边形1共享左边界的下半部分
        // 注意：为了共享边界，我们简单地复制了多边形1的左边界坐标，并添加了新的坐标以形成一个新的封闭多边形
        Coordinate[] coordinates2 = new Coordinate[]{new Coordinate(0, 5),  // 从多边形1的边界上的一点开始
                new Coordinate(0, 0), new Coordinate(5, 0),  // 新增坐标，使多边形2与多边形1仅在左侧共享边界
                new Coordinate(5, 5),  // 这里假设我们想要一个正方形，实际情况可能不同
                new Coordinate(0, 5)   // 返回起点闭合多边形
        };

        // 创建多边形几何对象
        Polygon polygon1 = factory.createPolygon(coordinates1);
        Polygon polygon2 = factory.createPolygon(coordinates2);

        // 空间关系判断
        boolean intersects = polygon1.intersects(polygon2); // 是否相交
        boolean contains = polygon1.contains(polygon2);   // 是否包含
        boolean within = polygon1.within(polygon2);  // 是否被包含
        boolean equals = polygon1.equals(polygon2);  // 是否相等
        boolean touches = polygon1.touches(polygon2);// 是否接触（共享边界）

        // 输出结果
        System.out.println("是否相交: " + intersects);
        System.out.println("是否包含: " + contains);
        System.out.println("是否被包含: " + within);
        System.out.println("是否相等: " + equals);
        System.out.println("是否接触: " + touches); // 未通过
    }


    /**
     * 几何对象属性: 面积、长度、边界框
     */
    @Test
    public void GeometricProperties_test() {
        // 创建GeometryFactory对象
        GeometryFactory factory = new GeometryFactory();

        // 构建一个矩形多边形
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(20, 10), new Coordinate(20, 0), new Coordinate(0, 0)};
        LinearRing ring = factory.createLinearRing(coordinates);
        Polygon polygon = factory.createPolygon(ring, null);

        // 计算面积
        double area = polygon.getArea();
        System.out.println("面积: " + area);

        // 计算周长（长度）
        double perimeter = polygon.getLength();
        System.out.println("周长: " + perimeter);

        // 获取边界框
        Geometry envelope = polygon.getEnvelope();
        System.out.println("边界框: " + envelope.toString()); // POLYGON

        Geometry boundary = polygon.getBoundary();
        System.out.println("边界框: " + boundary); // 输出边界框 LINEARRING

        // 质心
        Geometry centroid = polygon.getCentroid();
        System.out.println("质心: " + centroid);
        System.out.println("质心: " + centroid.getCoordinate());
        System.out.println("质心: " + centroid.getCoordinate().x + "," + centroid.getCoordinate().y);

        // 维度
        int boundaryDimension = polygon.getBoundaryDimension();
        System.out.println("边界维度: " + boundaryDimension);

        int dimension = polygon.getDimension();
        System.out.println("维度: " + dimension);

        String geometryType = polygon.getGeometryType();
        System.out.println("几何类型: " + geometryType);

        GeometryFactory geometryFactory = polygon.getFactory();
        System.out.println("几何工厂: " + geometryFactory);
    }

    /**
     * 几何对象简化: simplify
     * <p>
     * 作用：减少顶点数量：通过删除那些对整体形状影响较小的顶点，显著减少几何对象中的点数。这对于大量点构成的线或面特别有效，例如道路网络、海岸线等。
     * 优点：保持主要形状特、提高性能、优化存储、降低计算复杂度
     */
    @Test
    public void simplify_test() {
        // 创建GeometryFactory对象
        GeometryFactory factory = new GeometryFactory();

        // 构建一个复杂的线对象，这里以一个曲折的折线为例
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(20, 0), new Coordinate(20, 20), new Coordinate(0, 20), new Coordinate(0, 10), new Coordinate(5, 10), new Coordinate(5, 5), new Coordinate(15, 5), new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 10), new Coordinate(0, 10)};
        LineString complexLine = factory.createLineString(coordinates);

        // 简化因子，决定了简化程度（容差）
        // double distanceTolerance = 0.01; // 单位与坐标系单位相同，此处假设为度
        double distanceTolerance = 0.1; // 单位与坐标系单位相同，此处假设为度

        // 简化线对象
        Geometry simplifiedLine = TopologyPreservingSimplifier.simplify(complexLine, distanceTolerance);

        // 确保简化后的几何对象是有效的
        if (!simplifiedLine.isValid()) {
            throw new TopologyException("The simplified geometry is not valid.");
        }

        // 输出简化前后的信息
        System.out.println("原始线对象: " + complexLine);
        System.out.println("简化后的线对象: " + simplifiedLine);

        System.out.println("原始顶点数: " + simplifiedLine.getNumPoints());
        System.out.println("简化后的顶点数: " + simplifiedLine.getNumPoints());
    }

    /**
     * 几何对象基础操作：几何属性查询、空间关系判断、几何变换(平移/缩放)、...
     * <p>
     * 几何属性查询：计算一个多边形的面积和获取其边界框。
     * 空间关系判断：判断一个多边形是否包含一个指定的点。
     * 几何变换：
     * 1.平移：将多边形沿x轴和y轴各平移5个单位。
     * 2.缩放：以多边形的质心为中心，将多边形的大小放大两倍。
     */
    @Test
    public void testGeometryOperations() {
        // 创建GeometryFactory对象
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形几何对象
        Coordinate[] coordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, 10),
                new Coordinate(10, 10),
                new Coordinate(10, 0),
                new Coordinate(0, 0)};
        Polygon polygon = factory.createPolygon(coordinates);

        // 几何属性查询 - 面积
        double area = polygon.getArea();
        System.out.println("多边形面积: " + area);

        // 几何属性查询 - 边界框
        Geometry envelope = polygon.getEnvelope();
        System.out.println("多边形边界框: " + envelope);

        // 空间关系判断 - 是否包含一个点
        Point pointInside = factory.createPoint(new Coordinate(5, 5));
        boolean contains = polygon.contains(pointInside);
        System.out.println("多边形是否包含给定点: " + contains);

        // 几何变换 - 平移
        AffineTransformation translation = AffineTransformation.translationInstance(5, 5);
        Geometry translatedPolygon = translation.transform(polygon);
        System.out.println("平移后的多边形: " + translatedPolygon);

        // 几何变换 - 缩放
        AffineTransformation scale = AffineTransformation.scaleInstance(2, 2, polygon.getCentroid().getX(), polygon.getCentroid().getY());
        Geometry scaledPolygon = scale.transform(polygon);
        System.out.println("缩放后的多边形: " + scaledPolygon);
    }
}
