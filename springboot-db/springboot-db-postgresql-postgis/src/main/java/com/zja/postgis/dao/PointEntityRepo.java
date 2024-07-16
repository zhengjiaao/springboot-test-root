package com.zja.postgis.dao;

import com.zja.postgis.model.PointEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PointEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 9:50
 */
@Repository
public interface PointEntityRepo extends JpaRepository<PointEntity, String>, CrudRepository<PointEntity, String>, JpaSpecificationExecutor<PointEntity> {

    List<PointEntity> findByPoint(Point point);

    List<PointEntity> findByName(String name);

    /**
     * 获取点实体的点文本表示。
     *
     * @param id 实体的ID。
     * @return 点的文本表示。
     */
    @Query(value = "SELECT ST_AsText(point) AS point_text FROM test_point WHERE id = :id", nativeQuery = true)
    String getPointAsText(@Param("id") String id);

    /**
     * 获取点实体的点文本表示，并转换为WGS84坐标系。
     *
     * @param id 实体的ID。
     * @return 转换后的点文本表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Transform(point, 4326)) AS point_text FROM test_point WHERE id = :id", nativeQuery = true)
    String getPointAsText4326(@Param("id") String id);

    /**
     * 获取点实体的点文本表示，并转换为Web Mercator坐标系。
     *
     * @param id 实体的ID。
     * @return 转换后的点文本表示。
     */
    @Query(value = "SELECT ST_AsText(ST_Transform(ST_SetSRID(point, 4326), 3857)) AS point_text FROM test_point WHERE id = :id", nativeQuery = true)
    String getPointAsText3857(@Param("id") String id);

    /**
     * 获取点实体的几何体类型。
     *
     * @param id 实体的ID。
     * @return 几何体的类型。
     */
    @Query(value = "SELECT ST_GeometryType(point) AS geometry_type FROM test_point WHERE id = :id", nativeQuery = true)
    String getGeometryType(@Param("id") String id);

    /**
     * 检查点实体的几何体是否为空。
     *
     * @param id 实体的ID。
     * @return 几何体是否为空。
     */
    @Query(value = "SELECT ST_IsEmpty(point) AS is_empty FROM test_point WHERE id = :id", nativeQuery = true)
    Boolean checkIfEmpty(@Param("id") String id);

    /**
     * 检查点实体的几何体是否为简单几何体。
     *
     * @param id 实体的ID。
     * @return 几何体是否为简单几何体。
     */
    @Query(value = "SELECT ST_IsSimple(point) AS is_simple FROM test_point WHERE id = :id", nativeQuery = true)
    Boolean checkIfSimple(@Param("id") String id);

    /**
     * 检查点实体的几何体是否有效。
     *
     * @param id 实体的ID。
     * @return 几何体是否有效。
     */
    @Query(value = "SELECT ST_IsValid(point) AS is_valid FROM test_point WHERE id = :id", nativeQuery = true)
    Boolean checkIfValid(@Param("id") String id);

    /**
     * 检查点实体的几何体是否为三维。
     *
     * @param id 实体的ID。
     * @return 几何体是否为三维。
     */
    @Query(value = "SELECT CASE WHEN ST_GeometryType(point) LIKE '%Z%' THEN TRUE ELSE FALSE END AS is_3d FROM test_point WHERE id = :id", nativeQuery = true)
    Boolean checkIf3D(@Param("id") String id);


    // 以下是需要原生 SQL 语法，执行Postgis 点【空间分析】 函数演示

    /**
     * 判断点是否与点相等
     *
     * @param point 查询点
     * @return 查询点是否与点相等
     */
    @Query(value = "SELECT p.* FROM test_point AS p WHERE ST_Equals(p.point, :point)", nativeQuery = true)
    List<PointEntity> findPointsEquals(@Param("point") Point point);

    /**
     * 检查点是否在给定的多边形内。
     *
     * @param id 实体的ID。
     * @param polygon 多边形。
     * @return 点是否在多边形内。
     */
    @Query(value = "SELECT ST_Within(point, :polygon) AS is_within FROM test_point WHERE id = :id", nativeQuery = true)
    Boolean checkIfWithinPolygon(@Param("id") String id, @Param("polygon") Polygon polygon);

    /**
     * 检查点是否在另一个点的缓冲区内，并转换为目标坐标系。
     *
     * @param id 实体的ID。
     * @param targetPoint 目标点。
     * @param bufferSize 缓冲区大小。单位：米
     * @param targetSrid 目标坐标系的SRID。
     * @return 点是否在缓冲区内。
     */
    @Query(value = "SELECT ST_Within(point, ST_Transform(ST_Buffer(ST_Transform(:targetPoint, :targetSrid), :bufferSize), ST_SRID(point))) AS is_within_buffer FROM test_point WHERE id = :id", nativeQuery = true)
    Boolean checkIfWithinBuffer(@Param("id") String id, @Param("targetPoint") Point targetPoint, @Param("bufferSize") Double bufferSize, @Param("targetSrid") Integer targetSrid);

    /**
     * 查询距离给定点最近的点实体。
     *
     * @param point 查询点。
     * @return 最近的点实体。
     */
    @Query(value = "SELECT p.* FROM test_point AS p ORDER BY ST_Distance(p.point, :point) LIMIT 1", nativeQuery = true)
    PointEntity findNearestPoint(@Param("point") Point point);

    /**
     * 查询距离给定点最近的k个点实体。
     *
     * @param point 查询点。
     * @param k 返回的点数量。
     * @return 最近的k个点实体列表。
     */
    @Query(value = "SELECT p.* FROM test_point AS p ORDER BY ST_Distance(p.point, :point) LIMIT :k", nativeQuery = true)
    List<PointEntity> findKNearestPoints(@Param("point") Point point, @Param("k") int k);


    // 以下是需要原生 SQL 语法，执行Postgis 点【空间关系】 函数演示

    /**
     * 查询距离内的所有点：从WGS84 EPSG:4326 转换到 EPSG:3857，实现坐标系单位：度-->米
     *
     * @param point 查询点
     * @param distance 距离 ，单位：米
     * @return 查询点距离内的所有点
     */
    @Query(value = "SELECT ST_Transform(p.point,3857) AS point,p.* FROM test_point AS p WHERE ST_DWithin(ST_Transform(:point,3857),ST_Transform(p.point,3857), :distance)", nativeQuery = true)
    List<PointEntity> findWithin(@Param("point") Point point, @Param("distance") Integer distance);

    /**
     * 重投影(坐标系转换) 从WGS84 EPSG:4326 转换到 EPSG:3857，实现坐标系单位：度-->米
     */
    @Query(value = "SELECT ST_Transform(p.point,3857) AS point,p.* FROM test_point AS p", nativeQuery = true)
    List<PointEntity> transformPoints();

    /**
     * 重投影(坐标系转换) 从WGS84 EPSG:4326 转换到 EPSG:3857，实现坐标系单位：度-->米
     */
    @Query(value = "SELECT ST_Transform(p.point,3857) AS point,p.* FROM test_point AS p WHERE p.point = :point", nativeQuery = true)
    List<PointEntity> transform(@Param("point") Point point);

    /**
     * 计算点到点的距离。
     *
     * @param id 查询点。
     * @param targetPoint 目标点。
     * @return 查询点到目标点的距离。
     */
    @Query(value = "SELECT ST_Distance(point, :targetPoint) AS distance FROM test_point WHERE id = :id", nativeQuery = true)
    Double calculateDistanceTo(@Param("id") String id, @Param("targetPoint") Point targetPoint);

    /**
     * 查询与指定点在给定距离内的所有点实体。
     *
     * @param point 查询点。
     * @param distance 距离。 单位：米
     * @return 与指定点在给定距离内的所有点实体列表。
     */
    @Query(value = "SELECT p.* FROM test_point AS p WHERE ST_DWithin(ST_Transform(p.point, 3857), ST_Transform(:point, 3857), :distance)", nativeQuery = true)
    List<PointEntity> findPointsWithinDistance(@Param("point") Point point, @Param("distance") Double distance);
}