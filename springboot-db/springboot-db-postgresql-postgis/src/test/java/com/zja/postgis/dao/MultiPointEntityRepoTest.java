package com.zja.postgis.dao;

import com.zja.postgis.model.MultiPointEntity;
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
public class MultiPointEntityRepoTest {

    @Autowired
    private MultiPointEntityRepo repo;

    private final GeometryFactory factory = new GeometryFactory();

    @Test
    public void test() {
        // 创建点对象
        Point point = factory.createPoint(new Coordinate(100, 200));
        System.out.println("Point: " + point);

        // 创建多点
        Point[] multiPoints = new Point[]{point};
        MultiPoint multiPoint = factory.createMultiPoint(multiPoints);
        System.out.println("MultiPoint: " + multiPoint);

        System.out.println("---------------");

        MultiPointEntity entity1 = new MultiPointEntity();
        entity1.setMultiPoint(multiPoint);
        entity1.setName("测试-多点集合");
        entity1.setType("几何对象");

        // 保存数据
        repo.save(entity1);

        // 设置坐标系统，否则无法查询正确的坐标
        multiPoint.setSRID(4326);

        // 查询数据
        List<MultiPointEntity> result = repo.findByMultiPoint(multiPoint);
        System.out.println("result.size=" + result.size());
        for (MultiPointEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getMultiPoint());
            System.out.println(entity2.getMultiPoint().getSRID());
            System.out.println(entity2.getMultiPoint().getDimension());
        }
    }

    @Test
    public void test2() {
        List<MultiPointEntity> result = repo.findByName("测试-多点集合");
        System.out.println("result.size=" + result.size());
        for (MultiPointEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getMultiPoint());
            System.out.println(entity2.getMultiPoint().getSRID());
            System.out.println(entity2.getMultiPoint().getDimension());
        }
    }

}
