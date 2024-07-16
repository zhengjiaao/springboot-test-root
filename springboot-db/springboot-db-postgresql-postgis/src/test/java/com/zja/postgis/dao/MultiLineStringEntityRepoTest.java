package com.zja.postgis.dao;

import com.zja.postgis.model.MultiLineStringEntity;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 13:30
 */
@SpringBootTest
public class MultiLineStringEntityRepoTest {

    @Autowired
    private MultiLineStringEntityRepo repo;

    private final GeometryFactory factory = new GeometryFactory();

    @Test
    public void test() {
        // 创建线对象
        Coordinate[] lineCoords = new Coordinate[]{new Coordinate(0, 0), new Coordinate(10, 10), new Coordinate(20, 20)};
        LineString line = factory.createLineString(lineCoords);
        System.out.println("LineString: " + line);

        // 创建多线String
        LineString[] multiLineStrings = new LineString[]{line};
        MultiLineString multiLineString = factory.createMultiLineString(multiLineStrings);
        System.out.println("MultiLineString: " + multiLineString);

        System.out.println("---------------");

        MultiLineStringEntity entity1 = new MultiLineStringEntity();
        entity1.setMultiLineString(multiLineString);
        entity1.setName("测试-多线集合");
        entity1.setType("几何对象");

        // 保存数据
        repo.save(entity1);

        // 设置坐标系统，否则无法查询正确的坐标
        multiLineString.setSRID(4326);

        // 查询数据
        List<MultiLineStringEntity> result = repo.findByMultiLineString(multiLineString);
        System.out.println("result.size=" + result.size());
        for (MultiLineStringEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getMultiLineString());
            System.out.println(entity2.getMultiLineString().getSRID());
            System.out.println(entity2.getMultiLineString().getDimension());
        }
    }

    @Test
    public void test2() {
        List<MultiLineStringEntity> result = repo.findByName("测试-多线集合");
        System.out.println("result.size=" + result.size());
        for (MultiLineStringEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getMultiLineString());
            System.out.println(entity2.getMultiLineString().getSRID());
            System.out.println(entity2.getMultiLineString().getDimension());
        }
    }
}
