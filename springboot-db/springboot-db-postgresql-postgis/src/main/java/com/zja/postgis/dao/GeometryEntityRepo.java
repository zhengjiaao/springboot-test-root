package com.zja.postgis.dao;

import com.zja.postgis.model.GeometryEntity;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * GeometryEntity SQL
 *
 * 几何计算的Repository接口。
 * 提供了针对几何数据的多种空间操作方法，包括中心点计算、距离计算、长度计算、最短路径计算、球面距离计算、
 * 几何属性检查（如是否为3D、是否闭合、是否为集合等）、包含关系检查、相交关系检查、重叠关系检查、接触关系检查、
 * 距离范围内几何体检索、几何体类型获取、几何体转换等。
 * @author: zhengja
 * @since: 2024/07/15 11:05
 */
@Repository
public interface GeometryEntityRepo extends JpaRepository<GeometryEntity, String>, CrudRepository<GeometryEntity, String>, JpaSpecificationExecutor<GeometryEntity> {

    List<GeometryEntity> findByGeometry(Geometry geometry);

    List<GeometryEntity> findByName(String name);

    /**
     * 计算给定几何对象的表面中心点。
     *
     * @param id 几何对象的ID。
     * @return 中心点的Geometry对象。
     */
    @Query(value = "SELECT ST_PointOnSurface(geometry) AS center_point FROM test_geometry WHERE id = :id", nativeQuery = true)
    Geometry calculateCenterPoint(@Param("id") Long id);

    /**
     * 计算给定几何对象到目标几何对象的距离。
     *
     * @param id 几何对象的ID。
     * @param targetGeometry 目标几何对象。
     * @return 距离值。
     */
    @Query(value = "SELECT ST_Distance(geometry, :targetGeometry) AS distance FROM test_geometry WHERE id = :id", nativeQuery = true)
    Double calculateDistanceTo(@Param("id") Long id, @Param("targetGeometry") Geometry targetGeometry);

    /**
     * 计算给定几何对象的长度。
     *
     * @param id 几何对象的ID。
     * @return 长度值。
     */
    @Query(value = "SELECT ST_Length(geometry) AS length FROM test_geometry WHERE id = :id", nativeQuery = true)
    Double calculateLength(@Param("id") Long id);

    /**
     * 计算给定几何对象到目标几何对象的最短路径。
     *
     * @param id 几何对象的ID。
     * @param targetGeometry 目标几何对象。
     * @return 最短路径的Geometry对象。
     */
    @Query(value = "SELECT ST_ShortestLine(geometry, :targetGeometry) AS shortest_path FROM test_geometry WHERE id = :id", nativeQuery = true)
    Geometry calculateShortestPath(@Param("id") Long id, @Param("targetGeometry") Geometry targetGeometry);

    /**
     * 计算给定几何对象到目标几何对象的球面距离。
     *
     * @param id 几何对象的ID。
     * @param targetGeometry 目标几何对象。
     * @return 球面距离值。
     */
    @Query(value = "SELECT ST_Distance_Sphere(geometry, :targetGeometry) AS sphere_distance FROM test_geometry WHERE id = :id", nativeQuery = true)
    Double calculateSphereDistanceTo(@Param("id") Long id, @Param("targetGeometry") Geometry targetGeometry);

    /**
     * 检查给定几何对象是否为3D对象。
     *
     * @param id 几何对象的ID。
     * @return 是否为3D对象的布尔值。
     */
    @Query(value = "SELECT ST_Is3D(geometry) AS is_3d FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIf3D(@Param("id") Long id);

    /**
     * 检查给定几何对象是否闭合。
     *
     * @param id 几何对象的ID。
     * @return 是否闭合的布尔值。
     */
    @Query(value = "SELECT ST_IsClosed(geometry) AS is_closed FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfClosed(@Param("id") Long id);

    /**
     * 检查给定几何对象是否为集合。
     *
     * @param id 几何对象的ID。
     * @return 是否为集合的布尔值。
     */
    @Query(value = "SELECT ST_IsCollection(geometry) AS is_collection FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfCollection(@Param("id") Long id);

    /**
     * 检查给定几何对象是否被包含在指定的几何对象中。
     *
     * @param id 几何对象的ID。
     * @param containingGeometry 包含几何对象。
     * @return 是否被包含的布尔值。
     */
    @Query(value = "SELECT ST_Contains(:containingGeometry, geometry) AS is_contained FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfContainedIn(@Param("id") Long id, @Param("containingGeometry") Geometry containingGeometry);

    /**
     * 检查两个几何对象是否相互交叉。
     *
     * @param id 几何对象的ID。
     * @param intersectingGeometry 交叉几何对象。
     * @return 是否交叉的布尔值。
     */
    @Query(value = "SELECT ST_Crosses(geometry, :intersectingGeometry) AS crosses FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfCrosses(@Param("id") Long id, @Param("intersectingGeometry") Geometry intersectingGeometry);

    /**
     * 检查给定几何对象是否为空。
     *
     * @param id 几何对象的ID。
     * @return 是否为空的布尔值。
     */
    @Query(value = "SELECT ST_IsEmpty(geometry) AS is_empty FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfEmpty(@Param("id") Long id);

    /**
     * 检查给定几何对象是否具有测量值。
     *
     * @param id 几何对象的ID。
     * @return 是否具有测量值的布尔值。
     */
    @Query(value = "SELECT ST_IsMeasured(geometry) AS is_measured FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfMeasured(@Param("id") Long id);

    /**
     * 检查给定几何对象是否为多重几何对象。
     *
     * @param id 几何对象的ID。
     * @return 是否为多重几何对象的布尔值。
     */
    @Query(value = "SELECT ST_IsMulti(geometry) AS is_multi FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfMulti(@Param("id") Long id);

    /**
     * 检查给定几何对象是否为环。
     *
     * @param id 几何对象的ID。
     * @return 是否为环的布尔值。
     */
    @Query(value = "SELECT ST_IsRing(geometry) AS is_ring FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfRing(@Param("id") Long id);

    /**
     * 检查给定几何对象是否简单，即没有自相交。
     *
     * @param id 几何对象的ID。
     * @return 是否简单的布尔值。
     */
    @Query(value = "SELECT ST_IsSimple(geometry) AS is_simple FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfSimple(@Param("id") Long id);

    /**
     * 检查给定几何对象是否有效。
     *
     * @param id 几何对象的ID。
     * @return 是否有效的布尔值。
     */
    @Query(value = "SELECT ST_IsValid(geometry) AS is_valid FROM test_geometry WHERE id = :id", nativeQuery = true)
    Boolean checkIfValid(@Param("id") Long id);

    /**
     * 为给定几何对象创建缓冲区。
     *
     * @param id 几何对象的ID。
     * @param bufferSize 缓冲区的大小。
     * @return 缓冲区的Geometry对象。
     */
    @Query(value = "SELECT ST_Buffer(geometry, :bufferSize) AS buffer FROM test_geometry WHERE id = :id", nativeQuery = true)
    Geometry createBuffer(@Param("id") Long id, @Param("bufferSize") Double bufferSize);

    /**
     * 查找与指定几何对象相交的几何实体。
     *
     * @param geometry 目标几何对象。
     * @return 相交的几何实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry AS g WHERE ST_Intersects(g.geometry, :geometry)", nativeQuery = true)
    List<GeometryEntity> findGeometriesIntersecting(@Param("geometry") Geometry geometry);

    /**
     * 查找与指定几何对象重叠的几何实体。
     *
     * @param geometry 目标几何对象。
     * @return 重叠的几何实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry AS g WHERE ST_Overlaps(g.geometry, :geometry)", nativeQuery = true)
    List<GeometryEntity> findGeometriesOverlapping(@Param("geometry") Geometry geometry);

    /**
     * 查找与指定几何对象接触的几何实体。
     *
     * @param geometry 目标几何对象。
     * @return 接触的几何实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry AS g WHERE ST_Touches(g.geometry, :geometry)", nativeQuery = true)
    List<GeometryEntity> findGeometriesTouching(@Param("geometry") Geometry geometry);

    /**
     * 查找与指定几何对象在指定距离范围内的几何实体。
     *
     * @param geometry 目标几何对象。
     * @param distance 距离范围。
     * @return 距离范围内的几何实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry AS g WHERE ST_DWithin(g.geometry, :geometry, :distance)", nativeQuery = true)
    List<GeometryEntity> findGeometriesWithinDistance(@Param("geometry") Geometry geometry, @Param("distance") Double distance);

    /**
     * 获取给定几何对象的几何类型。
     *
     * @param id 几何对象的ID。
     * @return 几何类型的字符串表示。
     */
    @Query(value = "SELECT ST_GeometryType(geometry) AS geometry_type FROM test_geometry WHERE id = :id", nativeQuery = true)
    String getGeometryType(@Param("id") Long id);

    /**
     * 将给定几何对象转换到指定的坐标参考系统（CRS）。
     *
     * @param geometry 待转换的几何对象。
     * @return 转换后的GeometryEntity列表，包含转换后的几何体及其属性。
     */
    @Query(value = "SELECT ST_Transform(g.geometry,3857) AS geometry,g.* FROM test_geometry AS g WHERE g.geometry = :geometry", nativeQuery = true)
    List<GeometryEntity> transform(@Param("geometry") Geometry geometry);

    /**
     * 批量转换所有几何对象到指定的坐标参考系统（CRS）。
     *
     * @return 转换后的GeometryEntity列表，包含所有转换后的几何体及其属性。
     */
    @Query(value = "SELECT ST_Transform(g.geometry,3857) AS geometry,g.* FROM test_geometry AS g", nativeQuery = true)
    List<GeometryEntity> transformGeometries();

}