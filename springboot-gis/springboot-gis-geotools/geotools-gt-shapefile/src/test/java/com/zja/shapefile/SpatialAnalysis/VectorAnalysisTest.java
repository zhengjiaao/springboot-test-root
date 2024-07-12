package com.zja.shapefile.SpatialAnalysis;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.index.strtree.ItemBoundable;
import org.locationtech.jts.index.strtree.ItemDistance;
import org.locationtech.jts.index.strtree.STRtree;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 矢量分析
 * <p>
 * 定义：矢量分析是一种研究矢量及其性质、运算和变换的数学分支。它涉及矢量的表示、运算规则和几何性质等。
 * 研究内容：矢量分析主要关注矢量的代数和几何特性。它涉及矢量的加法、减法、数量乘法、点乘、叉乘、模长、方向、分解、投影等操作和概念。
 * <p>
 * 操作：矢量分析主要涉及矢量运算和几何运算。例如：矢量加法、矢量减法、点乘、叉乘、模长、方向、投影等。
 *
 * @Author: zhengja
 * @Date: 2024-07-09 11:11
 */
public class VectorAnalysisTest {

    private static final String SHP_PATH = "D:\\temp\\shp\\";

    /**
     * 最近邻分析（Nearest Neighbor Analysis, NNA）: NNA的核心在于计算点之间的距离，特别是每个点到其最近邻点的距离，以此来推断整体的分布模式。
     * <p>
     * 关键指标：
     * 1.最近邻距离：每个点到其最近的另一个点的距离。
     * 2.平均最近邻距离：所有点的最近邻距离的平均值。
     * 3.最近邻指数（R）：观测到的平均最近邻距离与期望的平均最近邻距离之比。这个期望值是基于假设数据集中的点随机分布的情况下计算出来的。
     * 分析步骤:
     * 1.计算最近邻距离：对于数据集中每一个点，找出离它最近的其他点，并记录下这两点之间的距离。
     * 2.计算平均最近邻距离：将所有点的最近邻距离加总后除以点的数量。
     * 3.计算最近邻指数（R）：将观测到的平均最近邻距离与随机分布下的期望平均最近邻距离进行比较。如果R小于1，表示点倾向于聚集；如果R等于1，表示点接近随机分布；如果R大于1，表示点倾向于均匀分布。
     */
    @Test
    public void NearestNeighbor_test1() throws IOException, FactoryException, TransformException {
        // URL inputURL = new URL("path/to/your/shapefile.shp"); // 替换为你的Shapefile路径
        // URL inputURL = Paths.get(SHP_PATH, "test", "上海润和总部园_北门_南门_西门_点图层.shp").toUri().toURL();
        URL inputURL = Paths.get(SHP_PATH, "test", "上海润和总部园_北门_南门_西门_点集图层.shp").toUri().toURL();

        // 打开Shapefile数据存储
        ShapefileDataStore inputStore = new ShapefileDataStore(inputURL);
        inputStore.setCharset(StandardCharsets.UTF_8);

        // 获取特征源
        SimpleFeatureSource featureSource = inputStore.getFeatureSource();
        SimpleFeatureCollection features = featureSource.getFeatures();

        // 获取坐标参考系统
        CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();

        // 将点转换到适合最近邻分析的CRS
        CoordinateReferenceSystem analysisCrs = CRS.decode("EPSG:3857"); // 例如，使用WGS84 EPSG:3857 单位：米
        MathTransform transform = CRS.findMathTransform(crs, analysisCrs, true);

        // 读取点数据并转换坐标
        List<Point> points = new ArrayList<>();
        try (SimpleFeatureIterator featureIterator = features.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();

                if (geom == null) {
                    continue;
                }

                if (geom instanceof MultiPoint) { // 处理MultiPoint
                    for (int i = 0; i < geom.getNumGeometries(); i++) {
                        Point point = (Point) geom.getGeometryN(i);
                        Point transformedPoint = (Point) JTS.transform(point, transform);
                        points.add(transformedPoint);
                    }
                } else if (geom instanceof Point) { // 处理Point
                    Point point = (Point) feature.getDefaultGeometry();
                    Point transformedPoint = (Point) JTS.transform(point, transform);
                    points.add(transformedPoint);
                } else {
                    // Handle other geometry types if necessary
                    System.out.println("Unsupported geometry type: " + geom.getGeometryType());
                }
            }
        }

        // 计算最近邻距离的平均值
        double meanNNDistance = calculateMeanNearestNeighborDistance(points);
        System.out.println("平均最近邻距离: " + meanNNDistance);
    }

    /**
     * 计算最近邻距离的平均值
     *
     * @param points 点列表
     * @return 最近邻距离的平均值
     */
    private static double calculateMeanNearestNeighborDistance(List<Point> points) {
        STRtree index = new STRtree();
        for (Point point : points) {
            System.out.println(point);
            index.insert(point.getEnvelopeInternal(), point);
        }

        double sumNNDistances = 0.0;
        int n = points.size();

        System.out.println("n " + n);
        for (Point point : points) {
            Point nearest = findNearestNeighbor(point, index);
            if (nearest != null) {
                sumNNDistances += point.distance(nearest);
            }
        }
        return sumNNDistances / n;
    }

    /**
     * 查找最近的邻居
     *
     * @param referencePoint 查询点
     * @param index          STRtree索引
     * @return 最近的邻居
     */
    private static Point findNearestNeighbor(Point referencePoint, STRtree index) {

        ItemDistance pointDistance = new ItemDistance() {
            @Override
            public double distance(ItemBoundable itemBoundable1, ItemBoundable itemBoundable2) {
                if (itemBoundable1 != null && itemBoundable2 != null) {
                    Object item1 = itemBoundable1.getItem();
                    Object item2 = itemBoundable2.getItem();
                    if (item1 instanceof Point && item2 instanceof Point) {
                        Point point1 = (Point) item1;
                        Point point2 = (Point) item2;
                        // 跳过与自身的比较
                        if (point1.equals(point2)) {
                            return Double.MAX_VALUE;
                        }
                        return point1.distance(point2);
                    } else {
                        throw new IllegalArgumentException("Both arguments must be instances of Point.");
                    }
                }

                return 0;
            }
        };

        // 使用 nearestNeighbour 方法查找最近的邻居
        Object nearestNeighbor = index.nearestNeighbour(referencePoint.getEnvelopeInternal(), referencePoint, pointDistance);

        // 输出最近的邻居
        if (nearestNeighbor instanceof Point) {
            Point closestPoint = (Point) nearestNeighbor;
            double distance = referencePoint.distance(closestPoint);
            System.out.println("Closest point to " + referencePoint + " is " + closestPoint + " with distance ：" + distance);
            return closestPoint;
        } else {
            System.out.println("No neighbors found.");
        }

        return null;
    }

    /**
     * 查找最近邻距离
     */
    @Test
    public void NearestNeighbor_test2() {
        GeometryFactory geometryFactory = new GeometryFactory();

        // 创建点集合
        Point[] points = new Point[]{geometryFactory.createPoint(new Coordinate(13536676.09291805, 3657760.5889953603)), geometryFactory.createPoint(new Coordinate(13536580.530778447, 3657504.4182177642)), geometryFactory.createPoint(new Coordinate(13536827.868080953, 3657428.9321579095))};

        // 构建 STRtree 索引
        STRtree index = new STRtree();
        for (Point point : points) {
            index.insert(point.getEnvelopeInternal(), point);
        }
        index.build();

        // 定义参考点
        Point referencePoint = points[0]; // 以第一个点作为参考点

        // 创建 ItemDistance 实例
        ItemDistance pointDistance = new ItemDistance() {
            @Override
            public double distance(ItemBoundable itemBoundable1, ItemBoundable itemBoundable2) {
                if (itemBoundable1 != null && itemBoundable2 != null) {
                    Object item1 = itemBoundable1.getItem();
                    Object item2 = itemBoundable2.getItem();
                    if (item1 instanceof Point && item2 instanceof Point) {
                        Point point1 = (Point) item1;
                        Point point2 = (Point) item2;
                        // 跳过与自身的比较
                        if (point1.equals(point2)) {
                            return Double.MAX_VALUE;
                        }
                        return point1.distance(point2);
                    } else {
                        throw new IllegalArgumentException("Both arguments must be instances of Point.");
                    }
                }

                return 0;
            }
        };

        // 使用 nearestNeighbour 方法查找最近的邻居
        Object nearestNeighbor = index.nearestNeighbour(referencePoint.getEnvelopeInternal(), referencePoint, pointDistance);

        // 输出最近的邻居
        if (nearestNeighbor instanceof Point) {
            Point closestPoint = (Point) nearestNeighbor;
            double distance = referencePoint.distance(closestPoint);

            System.out.println("Closest point to " + referencePoint + " is " + closestPoint + " with distance ：" + distance);
        } else {
            System.out.println("No neighbors found.");
        }
    }

    /**
     * 平均最近邻距离
     */
    @Test
    public void averageNearestNeighborDistance() {
        GeometryFactory geometryFactory = new GeometryFactory();

        // 创建点集合
        List<Point> points = Arrays.asList(geometryFactory.createPoint(new Coordinate(13536676.09291805, 3657760.5889953603)), geometryFactory.createPoint(new Coordinate(13536580.530778447, 3657504.4182177642)), geometryFactory.createPoint(new Coordinate(13536827.868080953, 3657428.9321579095)));

        // 构建 STRtree 索引
        STRtree index = new STRtree();
        for (Point point : points) {
            index.insert(point.getEnvelopeInternal(), point);
        }
        index.build();

        // 创建 ItemDistance 实例
        ItemDistance pointDistance = new ItemDistance() {
            @Override
            public double distance(ItemBoundable itemBoundable1, ItemBoundable itemBoundable2) {
                if (itemBoundable1 != null && itemBoundable2 != null) {
                    Object item1 = itemBoundable1.getItem();
                    Object item2 = itemBoundable2.getItem();
                    if (item1 instanceof Point && item2 instanceof Point) {
                        Point point1 = (Point) item1;
                        Point point2 = (Point) item2;
                        // 跳过与自身的比较
                        if (point1.equals(point2)) {
                            return Double.MAX_VALUE;
                        }
                        return point1.distance(point2);
                    } else {
                        throw new IllegalArgumentException("Both arguments must be instances of Point.");
                    }
                }

                return 0;
            }
        };

        // 计算平均最近邻距离
        double totalDistance = 0;
        int count = 0;
        for (Point referencePoint : points) {
            Object nearestNeighbor = index.nearestNeighbour(referencePoint.getEnvelopeInternal(), referencePoint, pointDistance);
            // 输出最近的邻居的点和距离
            if (nearestNeighbor instanceof Point) {
                Point closestPoint = (Point) nearestNeighbor;
                double distance = referencePoint.distance(closestPoint);
                System.out.println("Distance from " + referencePoint + " to " + closestPoint + ": " + distance);
                totalDistance += distance;
                count++;
            }
        }

        double averageDistance = totalDistance / count;
        System.out.println("Average nearest neighbor distance: " + averageDistance);
    }

}
