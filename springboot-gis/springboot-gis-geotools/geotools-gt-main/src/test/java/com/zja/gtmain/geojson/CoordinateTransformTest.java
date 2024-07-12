package com.zja.gtmain.geojson;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @Author: zhengja
 * @Date: 2024-07-08 14:19
 */
public class CoordinateTransformTest {

    @Test
    public void CoordinateTransform_test1() throws FactoryException, TransformException {

        // 创建JTS的GeometryFactory
        GeometryFactory geometryFactory = new GeometryFactory();

        // 创建一个Point对象，例如WGS84下的坐标
        // 121.60203030443612704 31.19239503727569129  // 上海润和总部园_北门_点
        Point point = geometryFactory.createPoint(new Coordinate(121.60203030443612704, 31.19239503727569129));

        // 定义原始坐标系和目标坐标系
        // CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326"); // WGS84 单位：度  todo 此方式，有问题
        CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84; // WGS84 单位：度
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857"); // Web Mercator 单位：米

        // 获取坐标变换
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        // MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

        // 使用GeoTools的JTS工具类进行重投影
        // Geometry reprojectedPoint = JTS.transform(point, transform);
        Point reprojectedPoint = (Point) JTS.transform(point, transform);

        // 输出转换后的坐标
        System.out.println("Original coordinates: " + point.getCoordinate());
        System.out.println("Reprojected coordinates: " + reprojectedPoint.getCoordinate());
    }
}
