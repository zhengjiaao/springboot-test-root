package com.zja.postgis.dao;

import com.zja.postgis.model.PointEntity;
import com.zja.postgis.util.GeometryUtil;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 10:02
 */
// @DataJpaTest
@SpringBootTest
public class PointEntityRepoTest {

    @Autowired
    private PointEntityRepo repo;

    @Autowired
    private EntityManager em;

    private final GeometryFactory factory = new GeometryFactory();

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final Point POINT_SHANGHAI_NORTH_GATE = GEOMETRY_FACTORY.createPoint(new Coordinate(121.60203030443612704, 31.19239503727569129));
    private static final Point POINT_SHANGHAI_SOUTH_GATE = GEOMETRY_FACTORY.createPoint(new Coordinate(121.60339372392198243, 31.18984639080450094));
    private static final Point POINT_SHANGHAI_WEST_GATE = GEOMETRY_FACTORY.createPoint(new Coordinate(121.6011718551302323, 31.19042647617959929));
    private static final Point POINT_QUERY = GEOMETRY_FACTORY.createPoint(new Coordinate(121.6011718551302323, 31.19042647617959929));
    private static final String POINT_QUERY_ID = "1721030303699";

    @BeforeAll
    public static void initData() {
        POINT_SHANGHAI_SOUTH_GATE.setSRID(4326);
        POINT_SHANGHAI_WEST_GATE.setSRID(4326);
        POINT_QUERY.setSRID(4326);
    }

    @Test
    @Disabled
    public void test() {
        Point point = factory.createPoint(new Coordinate(100, 200));
        System.out.println("point= 经度:" + point.getX() + " 维度:" + point.getY() + " 坐标系统:" + point.getSRID());
        // point.setSRID(3857); // column SRID (4326), 其他类型保存会报错

        Point point2 = factory.createPoint(new Coordinate(100, 200));
        System.out.println("point2= 经度:" + point2.getX() + " 维度:" + point2.getY() + " 坐标系统:" + point2.getSRID());
        // point2.setSRID(3857); // column SRID (4326), 其他类型保存会报错
        point2.setSRID(4326); // column SRID (4326), 其他类型保存会报错

        PointEntity pointEntity = new PointEntity();
        pointEntity.setName("测试-点");
        pointEntity.setPoint(point);
        pointEntity.setPoint2(point2);
        repo.save(pointEntity);

        // 设置坐标系统，否则无法查询正确的坐标
        point.setSRID(4326);

        List<PointEntity> result = repo.findByPoint(point);
        System.out.println("result.size=" + result.size());
        for (PointEntity entity : result) {
            System.out.println("---------------------------");
            System.out.println(entity);
            System.out.println(entity.getPoint());
            System.out.println(entity.getPoint().getSRID());
            System.out.println(entity.getPoint().getDimension());
            System.out.println(entity.getPoint2());
            System.out.println(entity.getPoint2().getSRID());
            System.out.println(entity.getPoint2().getDimension());
        }
    }

    @Test
    public void test2() {
        List<PointEntity> pointEntityList = repo.findByName("测试-点");
        for (PointEntity entity : pointEntityList) {
            System.out.println("---------------------------");
            System.out.println(entity);
            System.out.println(entity.getPoint());
            System.out.println(entity.getPoint().getSRID());
            System.out.println(entity.getPoint().getDimension());
            System.out.println(entity.getPoint2());
            System.out.println(entity.getPoint2().getSRID());
            System.out.println(entity.getPoint2().getDimension());
        }
    }

    // 创建测试数据

    @Test
    @Disabled
    public void test3() {
        // 假设数据库已存在以下3条数据：
        // Point 默认坐标系为：4326

        // 上海润和总部园_北门_点图层
        // Point (121.60203030443612704 31.19239503727569129)
        // 上海润和总部园_南门_点图层
        // Point (121.60339372392198243 31.18984639080450094)
        // 上海润和总部园_西门_点图层
        // Point (121.6011718551302323 31.19042647617959929)

        // Point point = factory.createPoint(new Coordinate(121.6011718551302323, 31.19042647617959929));

        Point north_gate = (Point) GeometryUtil.wktToGeometry("Point (121.60203030443612704 31.19239503727569129)");
        Point south_gate = (Point) GeometryUtil.wktToGeometry("Point (121.60339372392198243 31.18984639080450094)");
        Point west_gate = (Point) GeometryUtil.wktToGeometry("Point (121.6011718551302323 31.19042647617959929)");

        PointEntity entity1 = new PointEntity();
        entity1.setName("上海润和总部园_北门_点图层");
        entity1.setPoint(north_gate);

        PointEntity entity2 = new PointEntity();
        entity2.setName("上海润和总部园_南门_点图层");
        entity2.setPoint(south_gate);

        PointEntity entity3 = new PointEntity();
        entity3.setName("上海润和总部园_西门_点图层");
        entity3.setPoint(west_gate);

        List<PointEntity> entityList = Arrays.asList(entity1, entity2, entity3);
        repo.saveAll(entityList);
    }

    @Test
    @Disabled
    public void test4() {
        // 上海润和总部园_北门_点图层
        Point point_4326 = factory.createPoint(new Coordinate(121.60203030443612704, 31.19239503727569129));
        point_4326.setSRID(4326);

        // 上海润和总部园_北门_点图层
        Point point_3857 = factory.createPoint(new Coordinate(13536676.092918051, 3657760.5889953612));
        point_3857.setSRID(3857);

        // 查询100米范围内的点
        List<PointEntity> pointEntityList = repo.findWithin(point_4326, 364); // 很准,300(以内返回北门、西门)、364(返回北门、西门)、365(返回北门、西门、南门)、
        for (PointEntity entity : pointEntityList) {
            System.out.println("---------------------------");
            System.out.println(entity.getName());
            System.out.println(entity.getPoint());
            System.out.println(entity.getPoint().getSRID());
            System.out.println("距离=" + point_3857.distance(entity.getPoint())); //  没有太多意义，单位：度
        }
    }

    /**
     * 重投影(坐标系转换)
     */
    @Test
    public void test5() {

        // 重投影(坐标系转换)

        // 上海润和总部园_北门_点图层
        Point point_4326 = factory.createPoint(new Coordinate(121.60203030443612704, 31.19239503727569129));
        point_4326.setSRID(4326);
        List<PointEntity> pointEntityList = repo.transform(point_4326);

        // List<PointEntity> pointEntityList = repo.transformPoints();
        for (PointEntity entity : pointEntityList) {
            System.out.println("---------------------------");
            System.out.println(entity.getName());
            System.out.println(entity.getPoint());
            System.out.println(entity.getPoint().getSRID());
        }
    }


    @Test
    void shouldFindByPoint() {
        List<PointEntity> entities = repo.findByPoint(POINT_QUERY);
        assertThat(entities).isNotEmpty();
    }

    @Test
    void shouldFindByName() {
        List<PointEntity> entities = repo.findByName("上海润和总部园_西门_点图层");
        assertThat(entities).isNotEmpty();
    }

    @Test
    void shouldGetPointAsText() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        String pointText = repo.getPointAsText(entity.getId());
        assertThat(pointText).isNotNull();
    }

    @Test
    void shouldGetPointAsText4326() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        String pointText4326 = repo.getPointAsText4326(entity.getId());
        assertThat(pointText4326).isNotNull();
    }

    @Test
    void shouldGetPointAsText3857() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        String pointText3857 = repo.getPointAsText3857(entity.getId());
        assertThat(pointText3857).isNotNull();
    }

    @Test
    void shouldGetGeometryType() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        String geometryType = repo.getGeometryType(entity.getId());
        assertThat(geometryType).isEqualTo("ST_Point");
    }

    @Test
    void shouldCheckIfEmpty() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Boolean isEmpty = repo.checkIfEmpty(entity.getId());
        assertThat(isEmpty).isFalse();
    }

    @Test
    void shouldCheckIfSimple() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Boolean isSimple = repo.checkIfSimple(entity.getId());
        assertThat(isSimple).isTrue();
    }

    @Test
    void shouldCheckIfValid() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Boolean isValid = repo.checkIfValid(entity.getId());
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldCheckIf3D() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Boolean is3D = repo.checkIf3D(entity.getId());
        assertThat(is3D).isFalse();
    }

    @Test
    void shouldFindPointsEquals() {
        List<PointEntity> entities = repo.findPointsEquals(POINT_SHANGHAI_WEST_GATE);
        assertThat(entities).isNotEmpty();
    }

    @Test
    void shouldCheckIfWithinPolygon() {
        // Assuming you have a Polygon object to test with.
        // Polygon polygon = null; // Replace with an actual polygon.
        // MultiPolygon (((121.60047541707274377 31.19211162001495197, 121.60347808153410654 31.1925347960716941, 121.60378870199562584 31.18981856335916092, 121.60201701491881465 31.18952331597569483, 121.60141878291882733 31.18973983081363244, 121.60047541707274377 31.19211162001495197)))
        Polygon polygon = factory.createPolygon(new Coordinate[]{new Coordinate(121.60047541707274377, 31.19211162001495197), new Coordinate(121.60347808153410654, 31.1925347960716941), new Coordinate(121.60378870199562584, 31.18981856335916092), new Coordinate(121.60201701491881465, 31.18952331597569483), new Coordinate(121.60141878291882733, 31.18973983081363244), new Coordinate(121.60047541707274377, 31.19211162001495197)});
        polygon.setSRID(4326);

        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Boolean withinPolygon = repo.checkIfWithinPolygon(entity.getId(), polygon);
        assertThat(withinPolygon).isNotNull();
    }

    @Test
    void shouldCheckIfWithinBuffer() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Boolean withinBuffer = repo.checkIfWithinBuffer(entity.getId(), POINT_SHANGHAI_SOUTH_GATE, 1000.0, 3857);
        assertThat(withinBuffer).isNotNull();
    }

    @Test
    void shouldFindNearestPoint() {
        PointEntity nearestPoint = repo.findNearestPoint(POINT_SHANGHAI_WEST_GATE);
        assertThat(nearestPoint).isNotNull();
    }

    @Test
    void shouldFindKNearestPoints() {
        List<PointEntity> nearestPoints = repo.findKNearestPoints(POINT_SHANGHAI_WEST_GATE, 2);
        assertThat(nearestPoints).hasSize(2);
    }

    @Test
    void shouldFindWithin() {
        List<PointEntity> entities = repo.findWithin(POINT_SHANGHAI_WEST_GATE, 1000);
        assertThat(entities).isNotEmpty();
    }

    @Test
    void shouldTransformPoints() {
        List<PointEntity> transformedPoints = repo.transformPoints();
        assertThat(transformedPoints).isNotEmpty();
    }

    @Test
    void shouldTransform() {
        List<PointEntity> transformedPoint = repo.transform(POINT_SHANGHAI_WEST_GATE);
        assertThat(transformedPoint).isNotEmpty();
    }

    @Test
    void shouldCalculateDistanceTo() {
        PointEntity entity = em.find(PointEntity.class, POINT_QUERY_ID);
        Double distance = repo.calculateDistanceTo(entity.getId(), POINT_SHANGHAI_SOUTH_GATE);
        assertThat(distance).isGreaterThan(0);
    }

    @Test
    void shouldFindPointsWithinDistance() {
        List<PointEntity> entities = repo.findPointsWithinDistance(POINT_SHANGHAI_WEST_GATE, 260.0);
        assertThat(entities).isNotEmpty();
    }

}
