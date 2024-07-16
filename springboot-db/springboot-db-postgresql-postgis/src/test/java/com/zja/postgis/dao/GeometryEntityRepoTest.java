package com.zja.postgis.dao;

import com.zja.postgis.model.GeometryEntity;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 11:04
 */
@SpringBootTest
public class GeometryEntityRepoTest {

    @Autowired
    private GeometryEntityRepo repo;

    private final GeometryFactory factory = new GeometryFactory();

    @Test
    public void test() {
        // 创建点对象
        Point point = factory.createPoint(new Coordinate(100, 200));
        System.out.println("Point: " + point);

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

        GeometryEntity entity1 = new GeometryEntity();
        entity1.setGeometry(point); // point、line、polygon
        // entity1.setGeometry(line);
        // entity1.setGeometry(polygon);
        entity1.setName("测试-几何对象");
        entity1.setType("几何对象");

        // 保存数据
        repo.save(entity1);

        // 设置坐标系统，否则无法查询正确的坐标
        point.setSRID(4326);
        line.setSRID(4326);
        polygon.setSRID(4326);

        // 查询数据
        List<GeometryEntity> result = repo.findByGeometry(point);
        System.out.println("result.size=" + result.size());
        for (GeometryEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getGeometry());
            System.out.println(entity2.getGeometry().getSRID());
            System.out.println(entity2.getGeometry().getDimension());
        }
    }

    @Test
    public void test2() {
        List<GeometryEntity> result = repo.findByName("测试-几何对象");
        System.out.println("result.size=" + result.size());
        for (GeometryEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getGeometry());
            System.out.println(entity2.getGeometry().getSRID());
            System.out.println(entity2.getGeometry().getDimension());
        }
    }

}
