package com.zja.postgis.dao;

import com.zja.postgis.model.MultiPointEntity;
import com.zja.postgis.model.MultiPolygonEntity;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MultiPointEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 11:13
 */
@Repository
public interface MultiPointEntityRepo extends JpaRepository<MultiPointEntity, String>, CrudRepository<MultiPointEntity, String>, JpaSpecificationExecutor<MultiPointEntity> {

    List<MultiPointEntity> findByMultiPoint(MultiPoint multiPoint);

    List<MultiPointEntity> findByName(String name);

    /**
     * 计算MultiPoint集合的边界盒。
     * 返回一个表示所有点的外包矩形的WKT字符串。
     */
    @Query(value = "SELECT ST_AsText(ST_Envelope(ST_Collect(multiPoint))) AS bounding_box FROM test_multi_point", nativeQuery = true)
    String calculateBoundingBox();

    /**
     * 计算MultiPoint集合的质心。
     * 返回一个表示所有点平均位置的点的WKT字符串。
     */
    @Query(value = "SELECT ST_AsText(ST_Centroid(ST_Collect(multiPoint))) AS centroid FROM test_multi_point", nativeQuery = true)
    String calculateCentroid();

    /**
     * 计算两个MultiPoint集合之间的差集。
     * 返回一个表示第一个集合中不在第二个集合中的点的WKT字符串。
     * @param id 当前MultiPoint实体的ID。
     * @param otherMultiPoint 另一个要从中减去的MultiPoint集合。
     * @return 差集的结果，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Difference(multiPoint, :otherMultiPoint)) AS difference FROM test_multi_point WHERE id = :id", nativeQuery = true)
    String calculateDifference(@Param("id") Long id, @Param("otherMultiPoint") MultiPoint otherMultiPoint);

    /**
     * 计算两个MultiPoint集合之间的交集。
     * 返回一个表示两个集合共有点的WKT字符串。
     * @param id 当前MultiPoint实体的ID。
     * @param otherMultiPoint 另一个MultiPoint集合，用于进行交集计算。
     * @return 交集的结果，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Intersection(multiPoint, :otherMultiPoint)) AS intersection FROM test_multi_point WHERE id = :id", nativeQuery = true)
    String calculateIntersection(@Param("id") Long id, @Param("otherMultiPoint") MultiPoint otherMultiPoint);

    /**
     * 计算两个MultiPoint集合的并集。
     * 返回一个表示两个集合所有点的组合的WKT字符串。
     * @param id 当前MultiPoint实体的ID。
     * @param otherMultiPoint 另一个MultiPoint集合，用于进行并集计算。
     * @return 并集的结果，以WKT字符串表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Union(multiPoint, :otherMultiPoint)) AS union FROM test_multi_point WHERE id = :id", nativeQuery = true)
    String calculateUnion(@Param("id") Long id, @Param("otherMultiPoint") MultiPoint otherMultiPoint);

    /**
     * 检查一个MultiPoint集合是否与另一个MultiPoint集合相交。
     * 返回一个布尔值，指示两个集合是否有共同的点。
     * @param id 当前MultiPoint实体的ID。
     * @param otherMultiPoint 另一个MultiPoint集合，用于进行相交检查。
     * @return 布尔值，如果两个集合相交则为true，否则为false。
     */
    @Query(value = "SELECT ST_Intersects(multiPoint, :otherMultiPoint) AS intersects FROM test_multi_point WHERE id = :id", nativeQuery = true)
    Boolean checkIntersects(@Param("id") Long id, @Param("otherMultiPoint") MultiPoint otherMultiPoint);

    /**
     * 检查一个MultiPoint集合是否完全包含在另一个MultiPoint集合内。
     * 返回一个布尔值，指示第一个集合是否完全位于第二个集合内。
     * @param id 当前MultiPoint实体的ID。
     * @param otherMultiPoint 另一个MultiPoint集合，用于进行包含检查。
     * @return 布尔值，如果第一个集合完全位于第二个集合内则为true，否则为false。
     */
    @Query(value = "SELECT ST_Within(multiPoint, :otherMultiPoint) AS within FROM test_multi_point WHERE id = :id", nativeQuery = true)
    Boolean checkWithin(@Param("id") Long id, @Param("otherMultiPoint") MultiPoint otherMultiPoint);

}