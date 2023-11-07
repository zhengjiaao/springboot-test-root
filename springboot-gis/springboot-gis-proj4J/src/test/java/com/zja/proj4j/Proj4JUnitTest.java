/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 16:19
 * @Since:
 */
package com.zja.proj4j;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.locationtech.proj4j.*;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/11/07 16:19
 */
public class Proj4JUnitTest {

    //proj4j 进行地理空间索引
    @Test
    public void testSpatialIndex() {
        // 创建空间索引
        SpatialIndex spatialIndex = new STRtree();

        // 创建几何对象
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point1 = geometryFactory.createPoint(new Coordinate(0, 0));
        Point point2 = geometryFactory.createPoint(new Coordinate(2, 2));
        Point point3 = geometryFactory.createPoint(new Coordinate(4, 4));

        // 将几何对象添加到空间索引
        spatialIndex.insert(point1.getEnvelopeInternal(), point1);
        spatialIndex.insert(point2.getEnvelopeInternal(), point2);
        spatialIndex.insert(point3.getEnvelopeInternal(), point3);

        // 查询空间索引，查找与给定几何对象相交的对象
        Geometry queryGeometry = geometryFactory.createPoint(new Coordinate(0, 0)); // 0 0，1 1,2 2
        List<Geometry> result = spatialIndex.query(queryGeometry.getEnvelopeInternal());

        // 验证查询结果
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(point1));
        Assert.assertTrue(result.contains(point2));
    }

    //proj4j 进行几何运算
    @Test
    public void testGeometryOperations() {
        // 创建几何对象
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(0, 0));
        LineString lineString = geometryFactory.createLineString(new Coordinate[]{new Coordinate(0, 0), new Coordinate(2, 2)});
        Polygon polygon = geometryFactory.createPolygon(new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 2), new Coordinate(2, 2), new Coordinate(2, 0), new Coordinate(0, 0)});

        // 进行几何运算
        Geometry buffer = BufferOp.bufferOp(point, 1);
        Geometry intersection = lineString.intersection(polygon);
        double distance = DistanceOp.distance(point, lineString);

        // 验证几何运算结果
        Assert.assertTrue(buffer instanceof Polygon);
        Assert.assertTrue(intersection.isEmpty());
        Assert.assertEquals(0.0, distance, 1e-6);
    }

    //proj4j 进行坐标系定义
    @Test
    public void testCoordinateSystemDefinition() {
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

        // 期望的目标坐标
        double expectedTargetX = -13629229.923688685;
        double expectedTargetY = 4568135.173718509;

        // 验证目标坐标的精度
        double epsilon = 1e-6;
        Assert.assertEquals(expectedTargetX, targetCoord.x, epsilon);
        Assert.assertEquals(expectedTargetY, targetCoord.y, epsilon);
    }

}
