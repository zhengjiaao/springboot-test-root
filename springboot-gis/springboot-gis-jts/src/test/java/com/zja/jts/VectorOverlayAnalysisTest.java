package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

import java.util.Comparator;
import java.util.List;

/**
 * 矢量叠加分析
 * <p>
 * 矢量叠加分析是GIS中一种重要的空间分析方法，它涉及多个矢量图层之间的空间关系计算。
 * <p>
 * 常见的操作：
 * 1.交集 (intersection)：找出两个多边形共同覆盖的区域。
 * 2.并集 (union)：合并两个多边形的所有区域。
 * 3.差集 (difference)：从第一个多边形中移除第二个多边形覆盖的部分，以及相反操作。
 * 4.对称差 (symDifference)：移除两个多边形的共同部分，保留各自独有的部分。
 * 最后，所有这些结果都被整合到了一个GeometryCollection中，方便一次性查看所有分析结果。
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:27
 */
public class VectorOverlayAnalysisTest {

    /**
     * 叠加分析(覆盖分析)（Overlay Analysis）:对两个或多个空间数据层进行叠加，以识别它们之间的空间关系，如交集、并集、差集、对称差等操作。
     */
    @Test
    public void overlayAnalysisTest() {
        GeometryFactory factory = new GeometryFactory();

        // 定义多边形1的坐标
        Coordinate[] polyCoords1 = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        // 定义多边形2的坐标，与多边形1部分重叠
        Coordinate[] polyCoords2 = {new Coordinate(5, 5), new Coordinate(15, 5), new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        System.out.println("Polygon1: " + polygon1);
        System.out.println("Polygon2: " + polygon2);
        System.out.println("--------------------");

        // 执行覆盖分析操作
        Geometry intersection = polygon1.intersection(polygon2); // 交集 (intersection)：找出两个多边形共同覆盖的区域。
        System.out.println("Intersection: " + intersection);
        Geometry union = polygon1.union(polygon2); // 并集 (union)：合并两个多边形的所有区域。
        System.out.println("Union: " + union);
        Geometry difference1 = polygon1.difference(polygon2); // 差集 (difference)：从第一个多边形中移除第二个多边形覆盖的部分，以及相反操作。
        System.out.println("Difference1: " + difference1);
        Geometry difference2 = polygon2.difference(polygon1); // 差集 (difference)：从第一个多边形中移除第二个多边形覆盖的部分，以及相反操作。
        System.out.println("Difference2: " + difference2);
        Geometry symDiff = polygon1.symDifference(polygon2); // 对称差 (symDifference)：移除两个多边形的共同部分，保留各自独有的部分。
        System.out.println("Symmetric Difference: " + symDiff);

        System.out.println("--------------------------");

        // 将所有结果整合到一个GeometryCollection中以便查看
        GeometryCollection results = factory.createGeometryCollection(new Geometry[]{intersection, union, difference1, difference2, symDiff});
        System.out.println("Overlay Analysis Results:\n" + results);
    }

    /**
     * 交集（Intersection）: 计算两个或多个多边形的交集。
     */
    @Test
    public void Intersection_test_1() {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        // Coordinate[] polyCoords1 = { /* 多边形1的坐标 */};
        // Polygon polygon1 = factory.createPolygon(polyCoords1);
        //
        // Coordinate[] polyCoords2 = { /* 多边形2的坐标 */};
        // Polygon polygon2 = factory.createPolygon(polyCoords2);

        // 定义多边形1的坐标
        Coordinate[] polyCoords1 = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        // 定义多边形2的坐标，确保它与多边形1有交集区域
        Coordinate[] polyCoords2 = {new Coordinate(5, 5), new Coordinate(15, 5), new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        System.out.println("Polygon1: " + polygon1);
        System.out.println("Polygon2: " + polygon2);
        System.out.println("--------------------");

        // 计算交集：输出它们相交部分的几何对象
        // 多边形1是一个位于(0,0)到(10,10)的正方形
        // 多边形2是一个稍大一些且中心位置相同的矩形，从(5,5)到(15,15)
        // 这两个多边形相交，因此调用intersection方法后，将输出它们相交部分的几何对象。
        Geometry intersection = polygon1.intersection(polygon2);
        System.out.println("Intersection: " + intersection);
    }

    /**
     * 并集（Union）: 计算两个或多个多边形的并集。
     * <p>
     * 并集操作在处理空间数据聚合、地图叠加分析等场景中非常有用。
     */
    @Test
    public void Union_test_2() {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        // 定义多边形1的坐标
        Coordinate[] polyCoords1 = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        // 定义多边形2的坐标，确保它与多边形1相邻或相交
        Coordinate[] polyCoords2 = {new Coordinate(10, 0), new Coordinate(10, 20), new Coordinate(20, 20), new Coordinate(20, 0), new Coordinate(10, 0)};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        System.out.println("Polygon1: " + polygon1);
        System.out.println("Polygon2: " + polygon2);
        System.out.println("--------------------");

        // 计算并集
        // polyCoords1定义了一个位于(0,0)到(10,10)的正方形，而polyCoords2定义了一个与之相邻的正方形，从(10,0)到(20,20)
        // 调用union方法后，将会得到一个合并这两个矩形的大矩形，即从(0,0)到(20,20)的新多边形。
        Geometry union = polygon1.union(polygon2);
        System.out.println("Union: " + union);
    }

    /**
     * 差集（Difference）: 计算两个或多个多边形的差集。将一个多边形裁剪或擦除另一个多边形。
     * <p>
     * 差集常用于空间分析中去除特定区域、计算专属区域等场景。
     */
    @Test
    public void Difference_test_3() {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        // 定义多边形1的坐标，作为一个较大的包围区域
        Coordinate[] polyCoords1 = {new Coordinate(0, 0), new Coordinate(0, 20), new Coordinate(20, 20), new Coordinate(20, 0), new Coordinate(0, 0)};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        // 定义多边形2的坐标，作为要从多边形1中减去的部分，位于多边形1内部
        Coordinate[] polyCoords2 = {new Coordinate(2, 2), new Coordinate(2, 18), new Coordinate(18, 18), new Coordinate(18, 2), new Coordinate(2, 2)};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        System.out.println("Polygon1: " + polygon1);
        System.out.println("Polygon2: " + polygon2);
        System.out.println("--------------------");

        // 计算差集
        // polyCoords1定义了一个大的正方形区域，坐标范围是从(0,0)到(20,20)。
        // 而polyCoords2定义了一个较小的正方形，完全位于大正方形内部，坐标范围是从(2,2)到(18,18)。
        // 执行差集操作后，结果将是从大正方形中移除了小正方形部分，剩余的几何部分将被打印出来。
        Geometry difference = polygon1.difference(polygon2);
        System.out.println("Difference: " + difference);
    }

    /**
     * 对称差（Symmetric Difference）: 计算两个多边形的对称差。
     * <p>
     * 对称差在分析地理空间数据差异、更新地图信息等领域有广泛应用。
     */
    @Test
    public void SymmetricDifference_test_4() {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        // 定义多边形1的坐标
        Coordinate[] polyCoords1 = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        // 定义多边形2的坐标，与多边形1部分重叠
        Coordinate[] polyCoords2 = {new Coordinate(5, 5), new Coordinate(15, 5), new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        System.out.println("Polygon1: " + polygon1);
        System.out.println("Polygon2: " + polygon2);
        System.out.println("--------------------");

        // 计算对称差
        // polyCoords1定义了一个位于(0,0)到(10,10)的正方形，
        // 而polyCoords2定义了一个与之部分重叠的矩形，从(5,5)到(15,15)
        // 执行对称差操作后，结果将包含两部分：多边形1中不与多边形2重叠的部分，以及多边形2中不与多边形1重叠的部分。
        Geometry symDifference = polygon1.symDifference(polygon2);
        System.out.println("Symmetric Difference: " + symDifference); // MULTIPOLYGON
    }

    /**
     * 覆盖分析（Clip or Erase）: 将一个多边形裁剪或擦除另一个多边形。
     * <p>
     * 覆盖分析可以看作是一种特殊的差集操作，其中一个是固定的裁剪区域，另一个是要被裁剪的区域。
     */
    @Test
    public void ClipOrErase_test_5() {
        GeometryFactory factory = new GeometryFactory();

        // 创建原始多边形和裁剪多边形
        // Coordinate[] originalCoords = { /* 原始多边形的坐标 */};
        // Polygon originalPolygon = factory.createPolygon(originalCoords);
        //
        // Coordinate[] clipCoords = { /* 裁剪多边形的坐标 */};
        // Polygon clipPolygon = factory.createPolygon(clipCoords);

        // 定义多边形1的坐标
        Coordinate[] polyCoords1 = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon originalPolygon = factory.createPolygon(polyCoords1);

        // 定义多边形2的坐标，与多边形1部分重叠
        Coordinate[] polyCoords2 = {new Coordinate(5, 5), new Coordinate(15, 5), new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)};
        Polygon clipPolygon = factory.createPolygon(polyCoords2);

        // 裁剪操作（实际上可以使用差集操作实现）
        Geometry clipped = originalPolygon.difference(clipPolygon);
        System.out.println("Clipped: " + clipped);
    }

    // todo 以下不是叠加分析代码

    /**
     * 缓冲区分析（Buffer）: 创建一个多边形的缓冲区。
     * <p>
     * 缓冲区分析是围绕矢量要素创建一定宽度范围内的区域，常用于影响范围分析、可视性分析等。
     */
    @Test
    public void Buffer_test_6() {
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形
        Coordinate[] polyCoords = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon = factory.createPolygon(polyCoords);

        // 创建缓冲区，指定缓冲区半径
        double bufferDistance = 2.0;
        Geometry bufferedPolygon = polygon.buffer(bufferDistance);

        System.out.println("Polygon: " + polygon);
        System.out.println("Buffered Polygon: " + bufferedPolygon);
    }

    /**
     * 点在多边形内判断（Contains）: 判断一个点是否在多边形内。
     * <p>
     * 判断一个点是否位于某个多边形内部，这是空间查询中的常见需求。
     */
    @Test
    public void Contains_test_7() {
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形
        Coordinate[] polyCoords = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon = factory.createPolygon(polyCoords);

        // 创建一个点
        Coordinate pointCoord = new Coordinate(5, 5);
        Point point = factory.createPoint(pointCoord);

        // 判断点是否在多边形内
        boolean isInside = polygon.contains(point);
        System.out.println("是多边形内的点: " + isInside);
    }

    /**
     * 几何对象简化（Simplification）: 简化一个几何对象，去除不必要的顶点。
     * <p>
     * 简化复杂几何对象，通常用于减少数据量，提高效率，同时尽量保持原有的空间特征。
     */
    @Test
    public void Simplification_test_8() {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 8307);

        // 创建一条复杂的线
        // Coordinate[] lineCoords = { /* 复杂线的坐标 */};
        // 创建一条复杂的线，这里以一个曲折的折线为例
        Coordinate[] lineCoords = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 20), new Coordinate(20, 20), new Coordinate(20, 15), new Coordinate(30, 15), new Coordinate(30, 0), new Coordinate(0, 0)};
        LineString line = factory.createLineString(lineCoords);

        // 简化线，指定简化容差为0.5（具体值应根据实际应用场景调整）
        double simplifyTolerance = 0.5;
        Geometry simplifiedLine = TopologyPreservingSimplifier.simplify(line, simplifyTolerance);

        System.out.println("Original Line: " + line);
        System.out.println("Simplified Line: " + simplifiedLine);
    }

    /**
     * 空间索引查询（STRtree）: 使用STRtree进行空间索引查询。
     * <p>
     * 使用空间索引可以高效地进行空间查询，如查找某个点周围最近的设施。
     */
    @Test
    public void STRtree_test_9() {
        GeometryFactory factory = new GeometryFactory();

        // 创建点集合，模拟设施位置
        STRtree index = new STRtree();

        // 假设设施坐标数组
        Coordinate[] facilitiesCoords = {new Coordinate(1, 1), new Coordinate(3, 3), new Coordinate(5, 7), new Coordinate(8, 2), new Coordinate(6, 6)};
        for (Coordinate coord : facilitiesCoords) {
            Point facility = factory.createPoint(coord);
            // 使用每个点的边界框插入到STRtree中
            index.insert(facility.getEnvelopeInternal(), facility);
        }

        // 查询点坐标
        // Coordinate queryPointCoord = new Coordinate(5, 5); // 无法查询
        Coordinate queryPointCoord = new Coordinate(6, 6); // 可以查询
        Point queryPoint = factory.createPoint(queryPointCoord);

        // 执行查询，查找包含查询点边界框内的所有设施点
        List<Geometry> nearestFacilities = index.query(queryPoint.getEnvelopeInternal());

        System.out.println("Nearest Facilities to Query Point (" + queryPoint + "):");
        for (Geometry facility : nearestFacilities) {
            System.out.println(facility);
        }
    }

    @Test
    @Deprecated // todo  未达到效果
    public void strtreeNearestTest() {
        GeometryFactory factory = new GeometryFactory();

        // 创建点集合，模拟设施位置
        STRtree index = new STRtree();

        // 假设设施坐标数组
        Coordinate[] facilitiesCoords = {
                new Coordinate(1, 1),
                new Coordinate(3, 3),
                new Coordinate(5, 7),
                new Coordinate(8, 2),
                new Coordinate(6, 6)};
        for (Coordinate coord : facilitiesCoords) {
            Point facility = factory.createPoint(coord);
            // 使用每个点的边界框插入到STRtree中
            index.insert(facility.getEnvelopeInternal(), facility);
        }

        // 查询点坐标
        Coordinate queryPointCoord = new Coordinate(5, 5);
        Point queryPoint = factory.createPoint(queryPointCoord);

        // 执行查询，获取可能接近的设施点
        List<Geometry> potentialMatches = index.query(queryPoint.getEnvelopeInternal());

        // 计算实际距离，找到最近的设施点
        Geometry nearestFacility = potentialMatches.stream().min(Comparator.comparingDouble(f -> queryPoint.distance((Point) f))).orElse(null);

        if (nearestFacility != null) {
            // 离查询点最近的设施
            System.out.println("Nearest Facility to Query Point (" + queryPoint + ") is: " + nearestFacility);
        } else {
            // 在查询点附近找不到设施。
            System.out.println("No facility found near the query point.");
        }
    }
}
