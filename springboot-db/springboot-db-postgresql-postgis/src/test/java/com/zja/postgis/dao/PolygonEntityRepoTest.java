package com.zja.postgis.dao;

import com.zja.postgis.model.PolygonEntity;
import com.zja.postgis.util.GeometryUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 11:04
 */
// @DataJpaTest
@SpringBootTest
public class PolygonEntityRepoTest {

    @Autowired
    private PolygonEntityRepo repo;

    @Autowired
    private EntityManager em;

    private final GeometryFactory factory = new GeometryFactory();

    // 上海润和总部园
    private static final Polygon POLYGON_QUERY = (Polygon) GeometryUtil.wktToGeometry("POLYGON ((121.60047541707274377 31.19211162001495197, 121.60347808153410654 31.1925347960716941, 121.60378870199562584 31.18981856335916092, 121.60201701491881465 31.18952331597569483, 121.60141878291882733 31.18973983081363244, 121.60047541707274377 31.19211162001495197))");
    private static final String POLYGON_QUERY_ID = "1721115052317";

    @BeforeAll
    public static void initData() {
        POLYGON_QUERY.setSRID(4326);
    }

    @Test
    @Disabled
    public void test() {
        // 创建多边形
        Coordinate[] polygonExterior = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        LinearRing exteriorRing = factory.createLinearRing(polygonExterior);
        Polygon polygon = factory.createPolygon(exteriorRing, null);
        System.out.println("Polygon: " + polygon);

        System.out.println("---------------");

        PolygonEntity entity1 = new PolygonEntity();
        entity1.setName("测试-多边形");
        entity1.setPolygon(polygon);

        // 保存数据
        repo.save(entity1);

        // 设置坐标系统，否则无法查询正确的坐标
        polygon.setSRID(4326);

        // 查询数据
        List<PolygonEntity> result = repo.findByPolygon(polygon);
        System.out.println("result.size=" + result.size());
        for (PolygonEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getPolygon());
            System.out.println(entity2.getPolygon().getSRID());
            System.out.println(entity2.getPolygon().getDimension());
        }
    }

    @Test
    @Disabled
    public void test2() {
        List<PolygonEntity> result = repo.findByName("测试-多边形");
        System.out.println("result.size=" + result.size());
        for (PolygonEntity entity2 : result) {
            System.out.println("---------------------------");
            System.out.println(entity2);
            System.out.println(entity2.getPolygon());
            System.out.println(entity2.getPolygon().getSRID());
            System.out.println(entity2.getPolygon().getDimension());
        }
    }


    // 以下是测试数据

    @Test
    @Disabled
    public void addTestData() {
        // 上海润和总部园
        Polygon shanghaiRunHe = (Polygon) GeometryUtil.wktToGeometry("POLYGON ((121.60047541707274377 31.19211162001495197, 121.60347808153410654 31.1925347960716941, 121.60378870199562584 31.18981856335916092, 121.60201701491881465 31.18952331597569483, 121.60141878291882733 31.18973983081363244, 121.60047541707274377 31.19211162001495197))");
        // 银联商务28栋（与上海润和总部园相交）
        Polygon yinLianShangWu = (Polygon) GeometryUtil.wktToGeometry("POLYGON ((121.60225285638037462 31.19246098643056797, 121.60277055714956873 31.19256924055112989, 121.60286259284187338 31.19242162126511886, 121.6023333876111252 31.19231336697566803, 121.60225285638037462 31.19246098643056797))");
        // 上海数慧59栋（在上海润和总部园内部）
        Polygon shanghaiShuHui = (Polygon) GeometryUtil.wktToGeometry("POLYGON ((121.60342055922639304 31.18996618670552934, 121.60369666630330698 31.19001539443647886, 121.60374268414948062 31.18986777116687037, 121.60343206368796132 31.18984808804686537, 121.60342055922639304 31.18996618670552934))");
        // 上海测试中心（与上海润和总部园相离）
        Polygon shanghaiCeShiZhongXin = (Polygon) GeometryUtil.wktToGeometry("POLYGON ((121.60412808361101611 31.19289400216984376, 121.60808561838005915 31.19358288676251334, 121.60808561838005915 31.19076826952970194, 121.6046572888417785 31.1901187305881642, 121.60412808361101611 31.19289400216984376))");

        PolygonEntity entity1 = new PolygonEntity();
        entity1.setName("上海润和总部园");
        entity1.setPolygon(shanghaiRunHe);

        PolygonEntity entity2 = new PolygonEntity();
        entity2.setName("银联商务28栋");
        entity2.setPolygon(yinLianShangWu);

        PolygonEntity entity3 = new PolygonEntity();
        entity3.setName("上海数慧59栋");
        entity3.setPolygon(shanghaiShuHui);

        PolygonEntity entity4 = new PolygonEntity();
        entity4.setName("上海测试中心");
        entity4.setPolygon(shanghaiCeShiZhongXin);

        List<PolygonEntity> entityList = Arrays.asList(entity1, entity2, entity3, entity4);
        repo.saveAll(entityList);
    }

    @Test
    public void shouldFindPolygonEntitiesByPolygon() {
        List<PolygonEntity> result = repo.findByPolygon(POLYGON_QUERY);
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldFindPolygonEntityByName() {
        List<PolygonEntity> result = repo.findByName("上海润和总部园");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("上海润和总部园");
    }

    @Test
    public void shouldCalculateNrings() {
        Integer nrings = repo.calculateNrings(POLYGON_QUERY_ID);
        assertThat(nrings).isGreaterThan(0);
    }

    // 其他方法的测试类似上面的模式，这里只展示部分
    @Test
    public void shouldCalculateExteriorRing() {
        String exteriorRing = repo.calculateExteriorRing(POLYGON_QUERY_ID);
        assertThat(exteriorRing).isNotNull();
    }

    @Test
    public void shouldGetPolygonAsText() {
        String polygonText = repo.getPolygonAsText(POLYGON_QUERY_ID);
        assertThat(polygonText).startsWith("POLYGON");
    }

    @Test
    public void shouldCheckIfPolygonIs3D() {
        Boolean is3D = repo.checkIf3D(POLYGON_QUERY_ID);
        assertThat(is3D).isFalse(); // 假设多边形不是3D的
    }

    @Test
    public void shouldCalculatePerimeter() {
        Double perimeter = repo.calculatePerimeter(POLYGON_QUERY_ID);
        assertThat(perimeter).isGreaterThan(0);
    }

    @Test
    public void shouldCalculateConvexHull() {
        String convexHull = repo.calculateConvexHull(POLYGON_QUERY_ID);
        assertThat(convexHull).isNotNull();
    }

    // 更多测试方法...

    @Test
    public void shouldFindNearestPolygon() {
        // 假设有一个点, 需要找到离它最近的多边形
        // Point (121.6011718551302323 31.19042647617959929) 西门
        Point point = factory.createPoint(new Coordinate(121.6011718551302323, 31.19042647617959929));
        point.setSRID(4326);

        PolygonEntity nearestPolygon = repo.findNearestPolygon(point);
        assertThat(nearestPolygon).isNotNull();
    }

}
