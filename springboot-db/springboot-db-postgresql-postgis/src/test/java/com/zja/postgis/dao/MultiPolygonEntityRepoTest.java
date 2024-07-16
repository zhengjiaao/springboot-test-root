package com.zja.postgis.dao;

import com.zja.postgis.model.MultiPolygonEntity;
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
public class MultiPolygonEntityRepoTest {

    @Autowired
    private MultiPolygonEntityRepo repo;

    private final GeometryFactory factory = new GeometryFactory();

    @Test
    public void test() {
        // 创建多边形
        Coordinate[] polygonExterior = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        LinearRing exteriorRing = factory.createLinearRing(polygonExterior);
        Polygon polygon = factory.createPolygon(exteriorRing, null);
        System.out.println("Polygon: " + polygon);

        // 创建多边形集合
        Polygon[] polygons = new Polygon[]{polygon};
        MultiPolygon multiPolygon = factory.createMultiPolygon(polygons);
        System.out.println("MultiPolygon: " + multiPolygon);

        System.out.println("---------------");

        MultiPolygonEntity entity1 = new MultiPolygonEntity();
        entity1.setMultiPolygon(multiPolygon);
        entity1.setName("测试-多边形集合");
        entity1.setType("几何对象");

        // 保存数据
        repo.save(entity1);

        // 设置坐标系统，否则无法查询正确的坐标
        multiPolygon.setSRID(4326);

        // 查询数据
        List<MultiPolygonEntity> result = repo.findByMultiPolygon(multiPolygon);
        System.out.println("result.size=" + result.size());
        for (MultiPolygonEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getMultiPolygon());
            System.out.println(entity2.getMultiPolygon().getSRID());
            System.out.println(entity2.getMultiPolygon().getDimension());
        }
    }

    @Test
    public void test2() {
        List<MultiPolygonEntity> result = repo.findByName("测试-多边形集合");
        System.out.println("result.size=" + result.size());
        for (MultiPolygonEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getMultiPolygon());
            System.out.println(entity2.getMultiPolygon().getSRID());
            System.out.println(entity2.getMultiPolygon().getDimension());
        }
    }

}
