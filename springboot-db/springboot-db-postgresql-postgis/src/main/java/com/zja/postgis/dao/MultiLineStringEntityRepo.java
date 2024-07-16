package com.zja.postgis.dao;

import com.zja.postgis.model.MultiLineStringEntity;
import com.zja.postgis.model.MultiPointEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MultiLineStringEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 11:12
 */
@Repository
public interface MultiLineStringEntityRepo extends JpaRepository<MultiLineStringEntity, String>, CrudRepository<MultiLineStringEntity, String>, JpaSpecificationExecutor<MultiLineStringEntity> {


    List<MultiLineStringEntity> findByMultiLineString(MultiLineString multiLineString);

    List<MultiLineStringEntity> findByName(String name);

    /**
     * 计算MultiLineString实体的总长度。
     *
     * @param id 实体的ID。
     * @return 总长度。
     */
    @Query(value = "SELECT SUM(ST_Length(ST_GeometryN(multiLineString, n))) AS total_length FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id", nativeQuery = true)
    Double calculateTotalLength(@Param("id") Long id);

    /**
     * 检查MultiLineString实体是否为简单几何。
     *
     * @param id 实体的ID。
     * @return 是否为简单几何。
     */
    @Query(value = "SELECT ST_IsSimple(multiLineString) AS is_simple FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfSimple(@Param("id") Long id);

    /**
     * 检查MultiLineString实体中的每条线是否闭合。
     *
     * @param id 实体的ID。
     * @return 是否闭合。
     */
    @Query(value = "SELECT array_agg(ST_IsClosed(ST_GeometryN(multiLineString, n))) AS is_closed FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id", nativeQuery = true)
    List<Boolean> checkIfClosed(@Param("id") Long id);

    /**
     * 检查MultiLineString实体是否有效。
     *
     * @param id 实体的ID。
     * @return 是否有效。
     */
    @Query(value = "SELECT ST_IsValid(multiLineString) AS is_valid FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfValid(@Param("id") Long id);

    /**
     * 检查MultiLineString实体是否为空。
     *
     * @param id 实体的ID。
     * @return 是否为空。
     */
    @Query(value = "SELECT ST_IsEmpty(multiLineString) AS is_empty FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfEmpty(@Param("id") Long id);

    /**
     * 获取MultiLineString实体中的最长线。
     *
     * @param id 实体的ID。
     * @return 最长线的几何对象。
     */
    @Query(value = "WITH lengths AS (SELECT ST_Length(ST_GeometryN(multiLineString, n)) AS length, ST_GeometryN(multiLineString, n) AS geom FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id) SELECT ST_AsText((SELECT geom FROM lengths WHERE length = (SELECT MAX(length) FROM lengths))) AS longest_line", nativeQuery = true)
    String findLongestLine(@Param("id") Long id);

    /**
     * 获取MultiLineString实体中的最短线。
     *
     * @param id 实体的ID。
     * @return 最短线的几何对象。
     */
    @Query(value = "WITH lengths AS (SELECT ST_Length(ST_GeometryN(multiLineString, n)) AS length, ST_GeometryN(multiLineString, n) AS geom FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id) SELECT ST_AsText((SELECT geom FROM lengths WHERE length = (SELECT MIN(length) FROM lengths))) AS shortest_line", nativeQuery = true)
    String findShortestLine(@Param("id") Long id);

    /**
     * 检查MultiLineString实体是否与另一MultiLineString相交。
     *
     * @param id 实体的ID。
     * @param otherMultiLineString 另一MultiLineString实体。
     * @return 是否相交。
     */
    @Query(value = "SELECT ST_Intersects(multiLineString, :otherMultiLineString) AS intersects FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfIntersects(@Param("id") Long id, @Param("otherMultiLineString") MultiLineString otherMultiLineString);

    /**
     * 检查MultiLineString实体是否与另一MultiLineString相切。
     *
     * @param id 实体的ID。
     * @param otherMultiLineString 另一MultiLineString实体。
     * @return 是否相切。
     */
    @Query(value = "SELECT ST_Touches(multiLineString, :otherMultiLineString) AS touches FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfTouches(@Param("id") Long id, @Param("otherMultiLineString") MultiLineString otherMultiLineString);

    /**
     * 检查MultiLineString实体是否与另一MultiLineString重叠。
     *
     * @param id 实体的ID。
     * @param otherMultiLineString 另一MultiLineString实体。
     * @return 是否重叠。
     */
    @Query(value = "SELECT ST_Overlaps(multiLineString, :otherMultiLineString) AS overlaps FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfOverlaps(@Param("id") Long id, @Param("otherMultiLineString") MultiLineString otherMultiLineString);

    /**
     * 计算MultiLineString实体中线的平均长度。
     *
     * @param id 实体的ID。
     * @return 平均长度。
     */
    @Query(value = "SELECT AVG(ST_Length(ST_GeometryN(multiLineString, n))) AS average_length FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id", nativeQuery = true)
    Double calculateAverageLength(@Param("id") Long id);

    /**
     * 检查MultiLineString实体是否包含给定点。
     *
     * @param id 实体的ID。
     * @param point 给定点。
     * @return 是否包含点。
     */
    @Query(value = "SELECT ST_Contains(multiLineString, :point) AS contains_point FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfContainsPoint(@Param("id") Long id, @Param("point") Point point);

    /**
     * 检查MultiLineString实体是否完全位于另一几何体内部。
     *
     * @param id 实体的ID。
     * @param containingGeometry 含有该多线字符串的几何体。
     * @return 是否完全位于内部。
     */
    @Query(value = "SELECT ST_Within(multiLineString, :containingGeometry) AS within FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfWithin(@Param("id") Long id, @Param("containingGeometry") Geometry containingGeometry);

    /**
     * 检查MultiLineString实体是否与另一几何体相交但不相切。
     *
     * @param id 实体的ID。
     * @param intersectingGeometry 另一几何体。
     * @return 是否相交但不相切。
     */
    @Query(value = "SELECT ST_Crosses(multiLineString, :intersectingGeometry) AS crosses FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfCrosses(@Param("id") Long id, @Param("intersectingGeometry") Geometry intersectingGeometry);

    /**
     * 检查MultiLineString实体是否与另一几何体相离。
     *
     * @param id 实体的ID。
     * @param disjointGeometry 另一几何体。
     * @return 是否相离。
     */
    @Query(value = "SELECT ST_Disjoint(multiLineString, :disjointGeometry) AS disjoint FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfDisjoint(@Param("id") Long id, @Param("disjointGeometry") Geometry disjointGeometry);

    /**
     * 计算MultiLineString实体与另一几何体的最近距离。
     *
     * @param id 实体的ID。
     * @param geometry 另一几何体。
     * @return 最近距离。
     */
    @Query(value = "SELECT ST_Distance(multiLineString, :geometry) AS min_distance FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    Double calculateMinDistance(@Param("id") Long id, @Param("geometry") Geometry geometry);

    /**
     * 计算MultiLineString实体与另一几何体的最远距离。
     *
     * @param id 实体的ID。
     * @param geometry 另一几何体。
     * @return 最远距离。
     */
    @Query(value = "SELECT MAX(ST_Distance(ST_GeometryN(multiLineString, n), :geometry)) AS max_distance FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id", nativeQuery = true)
    Double calculateMaxDistance(@Param("id") Long id, @Param("geometry") Geometry geometry);

    /**
     * 计算MultiLineString实体与另一几何体的平均距离。
     *
     * @param id 实体的ID。
     * @param geometry 另一几何体。
     * @return 平均距离。
     */
    @Query(value = "SELECT AVG(ST_Distance(ST_GeometryN(multiLineString, n), :geometry)) AS avg_distance FROM test_multi_linestring CROSS JOIN generate_series(1, ST_NumGeometries(multiLineString)) AS n WHERE id = :id", nativeQuery = true)
    Double calculateAvgDistance(@Param("id") Long id, @Param("geometry") Geometry geometry);

    /**
     * 计算MultiLineString实体的凸包。
     *
     * @param id 实体的ID。
     * @return 凸包的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_ConvexHull(multiLineString)) AS convex_hull FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    String calculateConvexHull(@Param("id") Long id);

    /**
     * 计算多线段的边界框。
     * 使用地理信息系统（GIS）函数ST_Envelope和ST_Collect对多线段进行处理，
     * 并将结果转换为文本格式返回。这有助于确定多线段的最小外包矩形。
     */
    @Query(value = "SELECT ST_AsText(ST_Envelope(ST_Collect(multiLineString))) AS bounding_box FROM test_multi_linestring", nativeQuery = true)
    String calculateBoundingBox();

    /**
     * 计算多线段的质心。
     * 通过GIS函数ST_Centroid和ST_Collect对多线段进行处理，计算其质心，
     * 并以文本格式返回。质心代表了多线段的几何中心。
     */
    @Query(value = "SELECT ST_AsText(ST_Centroid(ST_Collect(multiLineString))) AS centroid FROM test_multi_linestring", nativeQuery = true)
    String calculateCentroid();

    /**
     * 计算多线段与另一多线段的差集。
     * 使用GIS函数ST_Difference计算当前多线段与指定多线段的差集，
     * 并以文本格式返回。这有助于提取两个多线段不相交的部分。
     * @param id 当前多线段的ID，用于确定操作的对象。
     * @param otherMultiLineString 另一个多线段，用于进行差集计算。
     * @return 差集的结果，以文本格式表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Difference(multiLineString, :otherMultiLineString)) AS difference FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    String calculateDifference(@Param("id") Long id, @Param("otherMultiLineString") MultiLineString otherMultiLineString);

    /**
     * 计算多线段与另一多线段的交集。
     * 使用GIS函数ST_Intersection计算当前多线段与其他多线段的交集，
     * 并以文本格式返回。这有助于确定两个多线段共有的几何部分。
     * 注意：此方法的注释与下一个方法重复，可能需要根据实际方法逻辑进行区分。
     */
    @Query(value = "SELECT ST_AsText(ST_Intersection(multiLineString, :otherMultiLineString)) AS intersection FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    String calculateIntersection();

    /**
     * 计算多线段与另一多线段的交集。
     * 使用GIS函数ST_Intersection计算当前多线段与指定多线段的交集，
     * 并以文本格式返回。这有助于确定两个多线段共有的几何部分。
     * @param id 当前多线段的ID，用于确定操作的对象。
     * @param otherMultiLineString 另一个多线段，用于进行交集计算。
     * @return 交集的结果，以文本格式表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Intersection(multiLineString, :otherMultiLineString)) AS intersection FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    String calculateIntersection(@Param("id") Long id, @Param("otherMultiLineString") MultiLineString otherMultiLineString);

    /**
     * 计算多线段与另一多线段的并集。
     * 使用GIS函数ST_Union计算当前多线段与指定多线段的并集，
     * 并以文本格式返回。这有助于合并两个多线段的几何形状。
     * @param id 当前多线段的ID，用于确定操作的对象。
     * @param otherMultiLineString 另一个多线段，用于进行并集计算。
     * @return 并集的结果，以文本格式表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Union(multiLineString, :otherMultiLineString)) AS union FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    String calculateUnion(@Param("id") Long id, @Param("otherMultiLineString") MultiLineString otherMultiLineString);

    /**
     * 计算所有多线段的并集。
     * 使用GIS函数ST_Union对所有多线段进行合并，计算它们的并集，
     * 并以文本格式返回。这有助于创建所有多线段的联合几何形状。
     */
    @Query(value = "SELECT ST_AsText(ST_Union(multiLineString)) AS union FROM test_multi_linestring", nativeQuery = true)
    String calculateUnion();

    /**
     * 计算多线段的缓冲区。
     * 使用GIS函数ST_Buffer为当前多线段计算缓冲区，缓冲区的大小由指定的参数确定。
     * 这有助于创建多线段周围的一个区域，该区域与多线段的距离等于指定的缓冲区大小。
     * @param id 当前多线段的ID，用于确定操作的对象。
     * @param bufferSize 缓冲区的大小，用于确定缓冲区的扩展距离。
     * @return 缓冲区的结果，以文本格式表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Buffer(multiLineString, :bufferSize)) AS buffer FROM test_multi_linestring WHERE id = :id", nativeQuery = true)
    String calculateBuffer(@Param("id") Long id, @Param("bufferSize") Double bufferSize);
}