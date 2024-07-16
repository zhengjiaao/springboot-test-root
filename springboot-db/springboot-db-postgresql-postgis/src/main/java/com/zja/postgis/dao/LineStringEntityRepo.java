package com.zja.postgis.dao;

import com.zja.postgis.model.LineStringEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LineStringEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 11:05
 */
@Repository
public interface LineStringEntityRepo extends JpaRepository<LineStringEntity, String>, CrudRepository<LineStringEntity, String>, JpaSpecificationExecutor<LineStringEntity> {

    List<LineStringEntity> findByLineString(LineString lineString);

    List<LineStringEntity> findByName(String name);

    /**
     * 将线串的第一个坐标作为点返回: 计算线串的起始点。
     * @param id 实体的ID。
     * @return 返回线串的起始点。
     */
    @Query(value = "SELECT ST_AsText(ST_StartPoint(line_string)) AS start_point FROM test_linestring WHERE id = :id", nativeQuery = true)
    String calculateStartPoint(@Param("id") String id);

    /**
     * 将线串的最后一个坐标作为点返回: 计算线串的结束点。
     * @param id 实体的ID。
     * @return 返回线串的结束点。
     */
    @Query(value = "SELECT ST_AsText(ST_EndPoint(line_string)) AS end_point FROM test_linestring WHERE id = :id", nativeQuery = true)
    String calculateEndPoint(@Param("id") String id);

    /**
     * 计算线串的坐标数量。
     * @param id 实体的ID。
     * @return 返回线串的坐标数量
     */
    @Query(value = "SELECT ST_NPoints(line_string) AS n_points FROM test_linestring WHERE id = :id", nativeQuery = true)
    Integer calculateNPoints(@Param("id") String id);

    /**
     * 计算线到另一条线的距离。
     *
     * @param id 实体的ID。
     * @param targetLineString 目标线。
     * @return 线到目标线的距离。
     */
    @Query(value = "SELECT ST_Distance(line_string, :targetLineString) AS distance FROM test_linestring WHERE id = :id", nativeQuery = true)
    Double calculateDistanceTo(@Param("id") String id, @Param("targetLineString") LineString targetLineString);

    /**
     * 计算线到另一条线的球面距离。
     *
     * @param id 实体的ID。
     * @param targetLineString 目标线。
     * @return 线到目标线的球面距离。
     */
    @Query(value = "SELECT ST_Distance_Sphere(line_string, :targetLineString) AS sphere_distance FROM test_linestring WHERE id = :id", nativeQuery = true)
    Double calculateSphereDistanceTo(@Param("id") String id, @Param("targetLineString") LineString targetLineString);

    /**
     * 获取线的几何类型。
     *
     * @param id 实体的ID。
     * @return 线的几何类型。
     */
    @Query(value = "SELECT ST_GeometryType(line_string) AS geometry_type FROM test_linestring WHERE id = :id", nativeQuery = true)
    String getGeometryType(@Param("id") String id);

    /**
     * 检查线是否为三维。
     *
     * @param id 实体的ID。
     * @return 线是否为三维。
     */
    @Query(value = "SELECT ST_Is3D(line_string) AS is_3d FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIf3D(@Param("id") String id);

    /**
     * 检查线是否为集合。
     *
     * @param id 实体的ID。
     * @return 线是否为集合。
     */
    @Query(value = "SELECT ST_IsCollection(line_string) AS is_collection FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfCollection(@Param("id") String id);

    /**
     * 检查线是否为空。
     *
     * @param id 实体的ID。
     * @return 线是否为空。
     */
    @Query(value = "SELECT ST_IsEmpty(line_string) AS is_empty FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfEmpty(@Param("id") String id);

    /**
     * 检查线是否为带测量值。
     *
     * @param id 实体的ID。
     * @return 线是否为带测量值。
     */
    @Query(value = "SELECT ST_IsMeasured(line_string) AS is_measured FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfMeasured(@Param("id") String id);

    /**
     * 检查线是否有效。
     *
     * @param id 实体的ID。
     * @return 线是否有效。
     */
    @Query(value = "SELECT ST_IsValid(line_string) AS is_valid FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfValid(@Param("id") String id);

    // 以下是需要原生 SQL 语法，执行Postgis 线【空间分析】 函数演示

    /**
     * 计算线实体的长度。
     *
     * @param id 实体的ID。
     * @return 线的长度。
     */
    @Query(value = "SELECT ST_Length(line_string) AS length FROM test_linestring WHERE id = :id", nativeQuery = true)
    Double calculateLength(@Param("id") String id);

    /**
     * 计算线实体的中心点。
     *
     * @param id 实体的ID。
     * @return 线的中心点。
     */
    @Query(value = "SELECT ST_AsText(ST_PointOnSurface(line_string)) AS center_point FROM test_linestring WHERE id = :id", nativeQuery = true)
    String calculateCenterPoint(@Param("id") String id);

    /**
     * 检查线实体是否闭合。
     *
     * @param id 实体的ID。
     * @return 线是否闭合。
     */
    @Query(value = "SELECT ST_IsClosed(line_string) AS is_closed FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfClosed(@Param("id") String id);

    /**
     * 检查线实体是否为简单线。
     *
     * @param id 实体的ID。
     * @return 线是否为简单线。
     */
    @Query(value = "SELECT ST_IsSimple(line_string) AS is_simple FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfSimple(@Param("id") String id);

    /**
     * 检查线实体是否为环。
     *
     * @param id 实体的ID。
     * @return 线是否为环。
     */
    @Query(value = "SELECT ST_IsRing(line_string) AS is_ring FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfRing(@Param("id") String id);

    /**
     * 检查线实体是否为多段线。
     *
     * @param id 实体的ID。
     * @return 线是否为多段线。
     */
    @Query(value = "SELECT ST_IsMulti(line_string) AS is_multi FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfMulti(@Param("id") String id);

    /**
     * 创建线实体的缓冲区。
     *
     * @param id 实体的ID。
     * @param bufferSize 缓冲区大小。
     * @return 缓冲区的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_Buffer(line_string, :bufferSize)) AS buffer FROM test_linestring WHERE id = :id", nativeQuery = true)
    String createBuffer(@Param("id") String id, @Param("bufferSize") Double bufferSize);

    /**
     * 计算线实体与另一线实体之间的最短路径。
     *
     * @param id 实体的ID。
     * @param targetLineString 另一线实体。
     * @return 最短路径的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_ShortestLine(line_string, :targetLineString)) AS shortest_path FROM test_linestring WHERE id = :id", nativeQuery = true)
    String calculateShortestPath(@Param("id") String id, @Param("targetLineString") LineString targetLineString);

    /**
     * 检查线实体是否完全包含在另一线内。
     *
     * @param id 实体的ID。
     * @param containingLineString 包含线实体。
     * @return 线是否被完全包含。
     */
    @Query(value = "SELECT ST_Contains(:containingLineString, line_string) AS is_contained FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfContainedIn(@Param("id") String id, @Param("containingLineString") LineString containingLineString);

    /**
     * 检查线实体是否与另一线相交但不相切。
     *
     * @param id 实体的ID。
     * @param intersectingLineString 相交线实体。
     * @return 线是否与另一线相交但不相切。
     */
    @Query(value = "SELECT ST_Crosses(line_string, :intersectingLineString) AS crosses FROM test_linestring WHERE id = :id", nativeQuery = true)
    Boolean checkIfCrosses(@Param("id") String id, @Param("intersectingLineString") LineString intersectingLineString);

    // 以下是需要原生 SQL 语法，执行Postgis 线【空间关系】 函数演示

    /**
     * 查询与指定线在给定距离内的所有线实体。
     *
     * @param lineString 查询线。
     * @param distance 距离。
     * @return 与指定线在给定距离内的所有线实体列表。
     */
    @Query(value = "SELECT l.* FROM test_linestring AS l WHERE ST_DWithin(l.line_string, :lineString, :distance)", nativeQuery = true)
    List<LineStringEntity> findLineStringsWithinDistance(@Param("lineString") LineString lineString, @Param("distance") Double distance);

    /**
     * 查询与指定线相交的所有线实体。
     *
     * @param lineString 相交检查中的线。
     * @return 与指定线相交的所有线实体列表。
     */
    @Query(value = "SELECT l.* FROM test_linestring AS l WHERE ST_Intersects(l.line_string, :lineString)", nativeQuery = true)
    List<LineStringEntity> findLineStringsIntersecting(@Param("lineString") LineString lineString);

    /**
     * 查询与指定线重叠的所有线实体。
     *
     * @param lineString 重叠检查中的线。
     * @return 与指定线重叠的所有线实体列表。
     */
    @Query(value = "SELECT l.* FROM test_linestring AS l WHERE ST_Overlaps(l.line_string, :lineString)", nativeQuery = true)
    List<LineStringEntity> findLineStringsOverlapping(@Param("lineString") LineString lineString);

    /**
     * 查询与指定线相切的所有线实体。
     *
     * @param lineString 相切检查中的线。
     * @return 与指定线相切的所有线实体列表。
     */
    @Query(value = "SELECT l.* FROM test_linestring AS l WHERE ST_Touches(l.line_string, :lineString)", nativeQuery = true)
    List<LineStringEntity> findLineStringsTouching(@Param("lineString") LineString lineString);

    /**
     * 将线转换到指定SRID坐标系。
     *
     * @param lineString 线。
     * @return 转换后的线实体列表。
     */
    @Query(value = "SELECT ST_Transform(l.line_string,3857) AS line_string,l.* FROM test_linestring AS l WHERE l.line_string = :lineString", nativeQuery = true)
    List<LineStringEntity> transform(@Param("lineString") LineString lineString);

    /**
     * 将所有线转换到指定SRID坐标系。
     *
     * @return 转换后的线实体列表。
     */
    @Query(value = "SELECT ST_Transform(l.line_string,3857) AS line_string,l.* FROM test_linestring AS l", nativeQuery = true)
    List<LineStringEntity> transformLineStrings();
}