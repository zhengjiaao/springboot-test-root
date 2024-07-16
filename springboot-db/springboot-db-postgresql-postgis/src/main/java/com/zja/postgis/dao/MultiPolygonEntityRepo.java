package com.zja.postgis.dao;

import com.zja.postgis.model.MultiPolygonEntity;
import com.zja.postgis.model.PolygonEntity;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MultiPolygonEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 11:13
 */
@Repository
public interface MultiPolygonEntityRepo extends JpaRepository<MultiPolygonEntity, String>, CrudRepository<MultiPolygonEntity, String>, JpaSpecificationExecutor<MultiPolygonEntity> {

    List<MultiPolygonEntity> findByMultiPolygon(MultiPolygon multipolygon);

    List<MultiPolygonEntity> findByName(String name);

    /**
     * 获取MultiPolygon集合中包含的几何图形的数量。
     * 返回一个表示集合中组成几何图形的数量的整数值。
     * @param id 当前MultiPolygon实体的ID。
     * @return 集合中组成几何图形的数量。
     */
    @Query(value = "SELECT ST_NumGeometries(multiPolygon) AS num_geometries FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Integer countGeometries(@Param("id") Long id);

    /**
     * 获取MultiPolygon集合中指定部分的几何图形。
     * 返回一个表示指定部分的几何图形的WKT字符串。
     * @param id 当前MultiPolygon实体的ID。
     * @param n 要获取的几何图形的索引。
     * @return 指定部分的几何图形，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_GeometryN(multiPolygon, :n) AS geometry FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Optional<MultiPolygon> getGeometry(@Param("id") Long id, @Param("n") Integer n);

    /**
     * 计算MultiPolygon集合的面积。
     * 返回一个表示所有多边形部分的总面积的数值。
     */
    @Query(value = "SELECT ST_Area(multiPolygon) AS area FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Double calculateArea(@Param("id") Long id);

    /**
     * 计算MultiPolygon集合的长度。
     * 返回一个表示所有线段部分的总长度的数值。
     */
    @Query(value = "SELECT ST_Length(multiPolygon) AS length FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Double calculateLength(@Param("id") Long id);

    /**
     * 计算MultiPolygon集合的边界盒。
     * 返回一个表示所有多边形外包矩形的WKT字符串。
     */
    @Query(value = "SELECT ST_AsText(ST_Envelope(ST_Collect(multiPolygon))) AS bounding_box FROM test_multi_polygon", nativeQuery = true)
    String calculateBoundingBox();

    /**
     * 计算MultiPolygon集合的质心。
     * 返回一个表示所有多边形平均位置的点的WKT字符串。
     */
    @Query(value = "SELECT ST_AsText(ST_Centroid(ST_Collect(multiPolygon))) AS centroid FROM test_multi_polygon", nativeQuery = true)
    String calculateCentroid();

    /**
     * 计算两个MultiPolygon集合之间的差集。
     * 返回一个表示第一个集合中不在第二个集合中的多边形的WKT字符串。
     * @param id 当前MultiPolygon实体的ID。
     * @param otherMultiPolygon 另一个要从中减去的MultiPolygon集合。
     * @return 差集的结果，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Difference(multiPolygon, :otherMultiPolygon)) AS difference FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    String calculateDifference(@Param("id") Long id, @Param("otherMultiPolygon") MultiPolygon otherMultiPolygon);

    /**
     * 计算两个MultiPolygon集合之间的交集。
     * 返回一个表示两个集合共有区域的WKT字符串。
     * @param id 当前MultiPolygon实体的ID。
     * @param otherMultiPolygon 另一个MultiPolygon集合，用于进行交集计算。
     * @return 交集的结果，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Intersection(multiPolygon, :otherMultiPolygon)) AS intersection FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    String calculateIntersection(@Param("id") Long id, @Param("otherMultiPolygon") MultiPolygon otherMultiPolygon);

    /**
     * 计算两个MultiPolygon集合的并集。
     * 返回一个表示两个集合所有区域的组合的WKT字符串。
     * @param id 当前MultiPolygon实体的ID。
     * @param otherMultiPolygon 另一个MultiPolygon集合，用于进行并集计算。
     * @return 并集的结果，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Union(multiPolygon, :otherMultiPolygon)) AS union FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    String calculateUnion(@Param("id") Long id, @Param("otherMultiPolygon") MultiPolygon otherMultiPolygon);

    /**
     * 检查一个MultiPolygon集合是否与另一个MultiPolygon集合相交。
     * 返回一个布尔值，指示两个集合是否有共同的区域。
     * @param id 当前MultiPolygon实体的ID。
     * @param otherMultiPolygon 另一个MultiPolygon集合，用于进行相交检查。
     * @return 布尔值，如果两个集合相交则为true，否则为false。
     */
    @Query(value = "SELECT ST_Intersects(multiPolygon, :otherMultiPolygon) AS intersects FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkIntersects(@Param("id") Long id, @Param("otherMultiPolygon") MultiPolygon otherMultiPolygon);

    /**
     * 检查一个MultiPolygon集合是否完全包含在另一个MultiPolygon集合内。
     * 返回一个布尔值，指示第一个集合是否完全位于第二个集合内。
     * @param id 当前MultiPolygon实体的ID。
     * @param otherMultiPolygon 另一个MultiPolygon集合，用于进行包含检查。
     * @return 布尔值，如果第一个集合完全位于第二个集合内则为true，否则为false。
     */
    @Query(value = "SELECT ST_Within(multiPolygon, :otherMultiPolygon) AS within FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkWithin(@Param("id") Long id, @Param("otherMultiPolygon") MultiPolygon otherMultiPolygon);

    /**
     * 检查一个点是否位于MultiPolygon集合内。
     * 返回一个布尔值，指示给定点是否位于MultiPolygon集合内的任一多边形内。
     * @param id 当前MultiPolygon实体的ID。
     * @param point 要检查的点。
     * @return 布尔值，如果点位于MultiPolygon集合内则为true，否则为false。
     */
    @Query(value = "SELECT ST_Contains(multiPolygon, :point) AS contains FROM test_multi_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkContainsPoint(@Param("id") Long id, @Param("point") Point point);

}