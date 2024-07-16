package com.zja.postgis.dao;

import com.zja.postgis.model.LineStringEntity;
import com.zja.postgis.util.GeometryUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 11:04
 */
@SpringBootTest
public class LineStringEntityRepoTest {

    @Autowired
    private LineStringEntityRepo repo;

    private final GeometryFactory factory = new GeometryFactory();

    @Test
    @Disabled
    public void test() {
        // 创建线对象
        Coordinate[] lineCoords = new Coordinate[]{new Coordinate(0, 0), new Coordinate(10, 10), new Coordinate(20, 20)};
        LineString line = factory.createLineString(lineCoords);
        System.out.println("LineString: " + line);

        System.out.println("---------------");

        LineStringEntity entity1 = new LineStringEntity();
        entity1.setLineString(line);
        entity1.setName("测试-线");
        entity1.setType("几何对象");

        // 保存数据
        repo.save(entity1);

        // 设置坐标系统，否则无法查询正确的坐标
        line.setSRID(4326);

        // 查询数据
        List<LineStringEntity> result = repo.findByLineString(line);
        System.out.println("result.size=" + result.size());
        for (LineStringEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getLineString());
            System.out.println(entity2.getLineString().getSRID());
            System.out.println(entity2.getLineString().getDimension());
        }
    }

    @Test
    @Disabled
    public void test2() {
        List<LineStringEntity> result = repo.findByName("测试-线");
        System.out.println("result.size=" + result.size());
        for (LineStringEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getLineString());
            System.out.println(entity2.getLineString().getSRID());
            System.out.println(entity2.getLineString().getDimension());
        }
    }

    // 添加测试数据

    private static final LineString LINESTRING_QUERY = (LineString) GeometryUtil.wktToGeometry("LineString (121.60083097342651115 31.19649009250624161, 121.60187888113050292 31.19412887735119355)");
    private static final String LINESTRING_QUERY_ID = "1579bf42-fa1f-4379-ad62-2f1bff507d8b";

    @Test
    @Disabled
    public void addTestData() throws InterruptedException {

        // 哈雷路
        // MultiLineString ((121.60083097342651115 31.19649009250624161, 121.60187888113050292 31.19412887735119355))
        // 蔡伦路(与哈雷路相交)
        // MultiLineString ((121.59909298016130208 31.19498154517621202, 121.60438363613039314 31.19598724606850126))
        // 张衡路(与哈雷路、蔡伦路不相交)
        // MultiLineString ((121.60021512987454173 31.19228771351460949, 121.60471833371292405 31.19324802885673265))

        LineString harley_road = (LineString) GeometryUtil.wktToGeometry("LineString (121.60083097342651115 31.19649009250624161, 121.60187888113050292 31.19412887735119355)");
        LineString cailun_road = (LineString) GeometryUtil.wktToGeometry("LineString (121.59909298016130208 31.19498154517621202, 121.60438363613039314 31.19598724606850126)");
        LineString zhangheng_road = (LineString) GeometryUtil.wktToGeometry("LineString (121.60021512987454173 31.19228771351460949, 121.60471833371292405 31.19324802885673265)");

        LineStringEntity entity1 = new LineStringEntity();
        entity1.setLineString(harley_road);
        entity1.setName("哈雷路");

        LineStringEntity entity2 = new LineStringEntity();
        entity2.setLineString(cailun_road);
        entity2.setName("蔡伦路");

        LineStringEntity entity3 = new LineStringEntity();
        entity3.setLineString(zhangheng_road);
        entity3.setName("张衡路");

        List<LineStringEntity> entityList = Arrays.asList(entity1, entity2, entity3);
        repo.saveAll(entityList);
    }

    @BeforeAll
    public static void initData() {
        LINESTRING_QUERY.setSRID(4326);
    }

    @Test
    void shouldFindEntitiesByLineString() {
        List<LineStringEntity> entities = repo.findByLineString(LINESTRING_QUERY);
        assertThat(entities).isNotEmpty(); // 或者更具体的断言，比如检查返回的实体是否正确
    }

    @Test
    void shouldFindEntitiesByName() {
        String name = "哈雷路";
        List<LineStringEntity> entities = repo.findByName(name);
        assertThat(entities).isNotEmpty();
    }

    @Test
    void shouldCalculateStartPoint() {
        String startPoint =  repo.calculateStartPoint(LINESTRING_QUERY_ID);
        assertThat(startPoint).isNotNull(); // 或者更详细的检查，比如比较坐标
    }

    @Test
    void shouldCheckIfValid() {
        Boolean isValid = repo.checkIfValid(LINESTRING_QUERY_ID);
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldCreateBuffer() {
        Double bufferSize = 100.0;
        String buffer = repo.createBuffer(LINESTRING_QUERY_ID, bufferSize);
        assertThat(buffer).isNotEmpty(); // 或者更详细的检查，比如解析WKT字符串并验证
    }

    @Test
    void shouldFindLineStringsWithinDistance() {
        Double distance = 1000.0;
        List<LineStringEntity> entities = repo.findLineStringsWithinDistance(LINESTRING_QUERY, distance);
        assertThat(entities).isNotEmpty();
    }

    @Test
    void shouldTransformLineString() {
        List<LineStringEntity> transformedEntities = repo.transform(LINESTRING_QUERY);
        assertThat(transformedEntities).isNotEmpty();
    }

}
