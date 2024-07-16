package com.zja.postgis.dao;

import com.zja.postgis.model.LineStringEntity;
import com.zja.postgis.model.PolygonEntity;
import org.locationtech.jts.geom.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PolygonEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 11:05
 */
@Repository
public interface PolygonEntityRepo extends JpaRepository<PolygonEntity, String>, CrudRepository<PolygonEntity, String>, JpaSpecificationExecutor<PolygonEntity> {

    List<PolygonEntity> findByPolygon(Polygon polygon);

    List<PolygonEntity> findByName(String name);

    /**
     * 计算多边形中环的数量。
     * @param id 实体的ID。
     * @return 返回多边形中环的数量（通常为1个，其他是孔）
     */
    @Query(value = "SELECT ST_NRings(polygon) AS nrings FROM test_polygon WHERE id = :id", nativeQuery = true)
    Integer calculateNrings(@Param("id") String id);

    /**
     *  返回多边形最外面的环。
     * @param id 实体的ID
     * @return 以线串的形式返回多边形最外面的环
     */
    @Query(value = "SELECT ST_AsText(ST_ExteriorRing(polygon)) AS exterior_ring FROM test_polygon WHERE id = :id", nativeQuery = true)
    String calculateExteriorRing(@Param("id") String id);

    /**
     * 返回多边形指定的内部环。
     * @param id 实体的ID
     * @param n 环的索引。
     * @return 以线串形式返回指定的内部环
     */
    @Query(value = "SELECT ST_AsText(ST_InteriorRingN(polygon, :n)) AS interior_ring FROM test_polygon WHERE id = :id", nativeQuery = true)
    String calculateInteriorRing(@Param("id") String id, @Param("n") Integer n);

    /**
     *  返回多边形文本形式。
     * @param id 实体的ID
     * @return 多边形文本形式。
     */
    @Query(value = "SELECT ST_AsText(polygon) AS polygon_text FROM test_polygon WHERE id = :id", nativeQuery = true)
    String getPolygonAsText(@Param("id") String id);

    /**
     *  返回多边形文本形式。 3857
     * @param id 实体的ID
     * @return 多边形文本形式。
     */
    @Query(value = "SELECT ST_AsText(ST_Transform(ST_SetSRID(polygon, 4326), 3857)) AS polygon_text FROM test_polygon WHERE id = :id", nativeQuery = true)
    String getPolygonAsText3857(@Param("id") String id);

    /**
     *  返回多边形文本形式。 4326
     * @param id 实体的ID
     * @return 多边形文本形式。
     */
    @Query(value = "SELECT ST_AsText(ST_Transform(polygon, 4326)) AS polygon_text FROM test_polygon WHERE id = :id", nativeQuery = true)
    String getPolygonAsText4326(@Param("id") String id);

    /**
     * 检查多边形是否为三维。
     *
     * @param id 实体的ID。
     * @return 多边形是否为三维。
     */
    // @Query(value = "SELECT ST_Is3D(polygon) AS is_3d FROM test_polygon WHERE id = :id", nativeQuery = true)
    // Boolean checkIf3D(@Param("id") String id);

    @Query(value = "SELECT CASE WHEN ST_GeometryType(polygon) LIKE '%Z%' THEN TRUE ELSE FALSE END AS is_3d FROM test_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkIf3D(@Param("id") String id);

    /**
     * 检查多边形是否为空。
     *
     * @param id 实体的ID。
     * @return 多边形是否为空。
     */
    @Query(value = "SELECT ST_IsEmpty(polygon) AS is_empty FROM test_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkIfEmpty(@Param("id") String id);

    /**
     * 检查多边形是否为简单几何体。
     *
     * @param id 实体的ID。
     * @return 多边形是否为简单几何体。
     */
    @Query(value = "SELECT ST_IsSimple(polygon) AS is_simple FROM test_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkIfSimple(@Param("id") String id);

    /**
     * 检查多边形是否有效。
     *
     * @param id 实体的ID。
     * @return 多边形是否有效。
     */
    @Query(value = "SELECT ST_IsValid(polygon) AS is_valid FROM test_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkIfValid(@Param("id") String id);

    // 以下是需要原生 SQL 语法，执行Postgis 多边形【空间关系】 函数演示

    /**
     * 计算多边形的周长。
     *
     * @param id 实体的ID。
     * @return 多边形的周长。返回所有环的长度
     */
    @Query(value = "SELECT ST_Perimeter(polygon) AS perimeter FROM test_polygon WHERE id = :id", nativeQuery = true)
    Double calculatePerimeter(@Param("id") String id);

    /**
     * 计算多边形的凸包。
     *
     * @param id 实体的ID。
     * @return 凸包的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_ConvexHull(polygon)) AS convex_hull FROM test_polygon WHERE id = :id", nativeQuery = true)
    String calculateConvexHull(@Param("id") String id);

    /**
     * 创建一个围绕多边形的缓冲区。
     *
     * @param id 实体的ID。
     * @param bufferDistance 缓冲区距离。
     * @return 缓冲区的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_Buffer(polygon, :bufferDistance)) AS buffer FROM test_polygon WHERE id = :id", nativeQuery = true)
    String createBuffer(@Param("id") String id, @Param("bufferDistance") Double bufferDistance);

    /**
     * 计算多边形的最小外接矩形。
     *
     * @param id 实体的ID。
     * @return 最小外接矩形的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_Envelope(polygon)) AS bounding_box FROM test_polygon WHERE id = :id", nativeQuery = true)
    String calculateBoundingBox(@Param("id") String id);

    /**
     * 计算多边形的质心。
     *
     * @param id 实体的ID。
     * @return 质心的几何对象。
     */
    @Query(value = "SELECT ST_AsText(ST_Centroid(polygon)) AS centroid FROM test_polygon WHERE id = :id", nativeQuery = true)
    String calculateCentroid(@Param("id") String id);

    /**
     * 检查多边形是否自相交。
     *
     * @param id 实体的ID。
     * @return 多边形是否自相交。
     */
    @Query(value = "SELECT NOT ST_IsSimple(polygon) AS is_self_intersecting FROM test_polygon WHERE id = :id", nativeQuery = true)
    Boolean checkIfSelfIntersecting(@Param("id") String id);

    /**
     * 计算多边形的边界线。
     *
     * @param id 实体的ID。
     * @return 边界线的几何对象。
     */
    @Query(value = "SELECT ST_Boundary(polygon) AS boundary FROM test_polygon WHERE id = :id", nativeQuery = true)
    Geometry calculateBoundary(@Param("id") String id);

    /**
     * 计算两个多边形的交集。
     *
     * @param id 实体的ID。
     * @param otherPolygon 另一个多边形。
     * @return 交集的几何对象。
     */
    @Query(value = "SELECT ST_Intersection(polygon, :otherPolygon) AS intersection FROM test_polygon WHERE id = :id", nativeQuery = true)
    Geometry calculateIntersection(@Param("id") String id, @Param("otherPolygon") Polygon otherPolygon);

    /**
     * 计算两个多边形的并集。
     *
     * @param id 实体的ID。
     * @param otherPolygon 另一个多边形。
     * @return 并集的几何对象。
     */
    @Query(value = "SELECT ST_Union(polygon, :otherPolygon) AS union_geom FROM test_polygon WHERE id = :id", nativeQuery = true)
    Geometry calculateUnion(@Param("id") String id, @Param("otherPolygon") Polygon otherPolygon);

    /**
     * 计算一个多边形减去另一个多边形的结果。
     *
     * @param id 实体的ID。
     * @param otherPolygon 另一个多边形。
     * @return 差集的几何对象。
     */
    @Query(value = "SELECT ST_Difference(polygon, :otherPolygon) AS difference FROM test_polygon WHERE id = :id", nativeQuery = true)
    Geometry calculateDifference(@Param("id") String id, @Param("otherPolygon") Polygon otherPolygon);

    /**
     * 计算两个多边形的对称差集。
     *
     * @param id 实体的ID。
     * @param otherPolygon 另一个多边形。
     * @return 对称差集的几何对象。
     */
    @Query(value = "SELECT ST_SymDifference(polygon, :otherPolygon) AS sym_difference FROM test_polygon WHERE id = :id", nativeQuery = true)
    Geometry calculateSymDifference(@Param("id") String id, @Param("otherPolygon") Polygon otherPolygon);

    /**
     * 查询距离给定点最近的多边形实体。
     *
     * @param point 查询点。
     * @return 最近的多边形实体。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p ORDER BY ST_Distance(p.polygon, :point) LIMIT 1", nativeQuery = true)
    PolygonEntity findNearestPolygon(@Param("point") Point point);

    /**
     * 计算多边形质心与指定点之间的距离。
     *
     * @param id 实体的ID。
     * @param point 指定点。
     * @return 中心偏移量。
     */
    @Query(value = "SELECT ST_Distance(ST_Centroid(polygon), :point) AS centroid_offset FROM test_polygon WHERE id = :id", nativeQuery = true)
    Double calculateCentroidOffset(@Param("id") String id, @Param("point") Point point);

    /**
     * 计算多边形上距离给定点最远的点。
     *
     * @param id 实体的ID。
     * @param point 查询点。
     * @return 最远点的几何对象。
     */
    @Query(value = "SELECT ST_FarthestPoint(polygon, :point) AS farthest_point FROM test_polygon WHERE id = :id", nativeQuery = true)
    Point calculateFarthestPoint(@Param("id") String id, @Param("point") Point point);

    /**
     * 计算从一个多边形到另一个多边形的最短路径。
     *
     * @param id 实体的ID。
     * @param targetPolygon 目标多边形。
     * @return 最短路径的几何对象。
     */
    @Query(value = "SELECT ST_ShortestLine(polygon, :targetPolygon) AS shortest_path FROM test_polygon WHERE id = :id", nativeQuery = true)
    LineString calculateShortestPath(@Param("id") String id, @Param("targetPolygon") Polygon targetPolygon);


    // 以下是需要原生 SQL 语法，执行Postgis 多边形【空间关系】 函数演示

    /**
     * 计算多边形的面积。
     *
     * @param id 实体的ID。
     * @return 多边形的面积。
     */
    @Query(value = "SELECT ST_Area(polygon) AS area FROM test_polygon WHERE id = :id", nativeQuery = true)
    Double calculateArea(@Param("id") String id);

    /**
     * 计算多边形到另一个多边形的距离。
     *
     * @param id 实体的ID。
     * @param targetPolygon 目标多边形。
     * @return 多边形到目标多边形的距离。
     */
    @Query(value = "SELECT ST_Distance(polygon, :targetPolygon) AS distance FROM test_polygon WHERE id = :id", nativeQuery = true)
    Double calculateDistanceTo(@Param("id") String id, @Param("targetPolygon") Polygon targetPolygon);

    /**
     * 计算多边形到另一个多边形的球面距离。
     *
     * @param id 实体的ID。
     * @param targetPolygon 目标多边形。
     * @return 多边形到目标多边形的球面距离。
     */
    @Query(value = "SELECT ST_Distance_Sphere(polygon, :targetPolygon) AS sphere_distance FROM test_polygon WHERE id = :id", nativeQuery = true)
    Double calculateSphereDistanceTo(@Param("id") String id, @Param("targetPolygon") Polygon targetPolygon);

    /**
     * 查询与指定多边形在给定距离内的所有多边形实体。
     *
     * @param polygon 查询多边形。
     * @param distance 距离。
     * @return 与指定多边形在给定距离内的所有多边形实体列表。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p WHERE ST_DWithin(p.polygon, :polygon, :distance)", nativeQuery = true)
    List<PolygonEntity> findPolygonsWithinDistance(@Param("polygon") Polygon polygon, @Param("distance") Double distance);

    /**
     * 查询被指定多边形包含的所有多边形实体。
     *
     * @param polygon 包含检查中的多边形。
     * @return 被指定多边形包含的所有多边形实体列表。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p WHERE ST_Contains(p.polygon, :polygon)", nativeQuery = true)
    List<PolygonEntity> findPolygonsContainedIn(@Param("polygon") Polygon polygon);

    /**
     * 查询与指定多边形相交的所有多边形实体。
     *
     * @param polygon 相交检查中的多边形。
     * @return 与指定多边形相交的所有多边形实体列表。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p WHERE ST_Intersects(p.polygon, :polygon)", nativeQuery = true)
    List<PolygonEntity> findPolygonsIntersecting(@Param("polygon") Polygon polygon);

    /**
     * 查询与指定多边形重叠的所有多边形实体。
     *
     * @param polygon 重叠检查中的多边形。
     * @return 与指定多边形重叠的所有多边形实体列表。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p WHERE ST_Overlaps(p.polygon, :polygon)", nativeQuery = true)
    List<PolygonEntity> findPolygonsOverlapping(@Param("polygon") Polygon polygon);

    /**
     * 查询与指定多边形相切的所有多边形实体。
     *
     * @param polygon 相切检查中的多边形。
     * @return 与指定多边形相切的所有多边形实体列表。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p WHERE ST_Touches(p.polygon, :polygon)", nativeQuery = true)
    List<PolygonEntity> findPolygonsTouching(@Param("polygon") Polygon polygon);

    /**
     * 查询位于指定多边形内部的所有多边形实体。
     *
     * @param polygon 内部检查中的多边形。
     * @return 位于指定多边形内部的所有多边形实体列表。
     */
    @Query(value = "SELECT p.* FROM test_polygon AS p WHERE ST_Within(p.polygon, :polygon)", nativeQuery = true)
    List<PolygonEntity> findPolygonsWithin(@Param("polygon") Polygon polygon);

    /**
     * 获取多边形的几何类型。
     *
     * @param id 实体的ID。
     * @return 多边形的几何类型。
     */
    @Query(value = "SELECT ST_GeometryType(polygon) AS geometry_type FROM test_polygon WHERE id = :id", nativeQuery = true)
    String getGeometryType(@Param("id") String id);

    /**
     * 将多边形转换到指定SRID坐标系。
     *
     * @param polygon 多边形。
     * @return 转换后的多边形实体列表。
     */
    @Query(value = "SELECT ST_Transform(p.polygon,3857) AS polygon,p.* FROM test_polygon AS p WHERE p.polygon = :polygon", nativeQuery = true)
    List<PolygonEntity> transform(@Param("polygon") Polygon polygon);

    /**
     * 将所有多边形转换到指定SRID坐标系。
     *
     * @return 转换后的多边形实体列表。
     */
    @Query(value = "SELECT ST_Transform(p.polygon,3857) AS polygon,p.* FROM test_polygon AS p", nativeQuery = true)
    List<PolygonEntity> transformPolygons();

}