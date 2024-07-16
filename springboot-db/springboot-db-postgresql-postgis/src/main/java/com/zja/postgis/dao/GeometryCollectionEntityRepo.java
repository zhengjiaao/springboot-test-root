package com.zja.postgis.dao;

import com.zja.postgis.model.GeometryCollectionEntity;
import org.locationtech.jts.geom.GeometryCollection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * GeometryCollectionEntity SQL
 * @author: zhengja
 * @since: 2024/07/15 11:05
 */
@Repository
public interface GeometryCollectionEntityRepo extends JpaRepository<GeometryCollectionEntity, String>, CrudRepository<GeometryCollectionEntity, String>, JpaSpecificationExecutor<GeometryCollectionEntity> {

    // @Query(value = "SELECT * FROM test_geometry_collection WHERE ST_Contains(geometryCollection, ST_GeomFromText(:geometry))", nativeQuery = true)
    // Optional<GeometryCollectionEntity> findByGeometry(String geometry);

    List<GeometryCollectionEntity> findByGeometryCollection(GeometryCollection geometryCollection);

    // Optional<GeometryCollectionEntity> findByName(String name);
    List<GeometryCollectionEntity> findByName(String name);

    /**
     * 根据给定的几何图形查询与之相交的GeometryCollection实体列表。
     * 使用Spatial SQL函数ST_Intersects进行查询。
     *
     * @param geometry 查询条件中的几何图形。
     * @return 与给定几何图形相交的GeometryCollection实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry_collection AS g WHERE ST_Intersects(g.geometryCollection, :geometry)", nativeQuery = true)
    List<GeometryCollectionEntity> findGeometryCollectionsIntersecting(@Param("geometry") GeometryCollection geometry);

    /**
     * 根据给定的几何图形查询与之重叠的GeometryCollection实体列表。
     * 使用Spatial SQL函数ST_Overlaps进行查询。
     *
     * @param geometry 查询条件中的几何图形。
     * @return 与给定几何图形重叠的GeometryCollection实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry_collection AS g WHERE ST_Overlaps(g.geometryCollection, :geometry)", nativeQuery = true)
    List<GeometryCollectionEntity> findGeometryCollectionsOverlapping(@Param("geometry") GeometryCollection geometry);

    /**
     * 根据给定的几何图形查询与之接触的GeometryCollection实体列表。
     * 使用Spatial SQL函数ST_Touches进行查询。
     *
     * @param geometry 查询条件中的几何图形。
     * @return 与给定几何图形接触的GeometryCollection实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry_collection AS g WHERE ST_Touches(g.geometryCollection, :geometry)", nativeQuery = true)
    List<GeometryCollectionEntity> findGeometryCollectionsTouching(@Param("geometry") GeometryCollection geometry);

    /**
     * 根据给定的几何图形和距离查询在给定距离内的GeometryCollection实体列表。
     * 使用Spatial SQL函数ST_DWithin进行查询。
     *
     * @param geometry 查询条件中的几何图形。
     * @param distance 查询条件中的距离。
     * @return 在给定距离内的GeometryCollection实体列表。
     */
    @Query(value = "SELECT g.* FROM test_geometry_collection AS g WHERE ST_DWithin(g.geometryCollection, :geometry, :distance)", nativeQuery = true)
    List<GeometryCollectionEntity> findGeometryCollectionsWithinDistance(@Param("geometry") GeometryCollection geometry, @Param("distance") Double distance);

    /**
     * 根据实体ID查询对应的GeometryCollection的几何类型。
     * 使用Spatial SQL函数ST_GeometryType进行查询。
     *
     * @param id 查询条件中的实体ID。
     * @return 对应实体ID的GeometryCollection的几何类型。
     */
    @Query(value = "SELECT ST_GeometryType(geometryCollection) AS geometry_type FROM test_geometry_collection WHERE id = :id", nativeQuery = true)
    String getGeometryType(@Param("id") Long id);

    /**
     * 将GeometryCollection实体的几何属性转换为EPSG:3857坐标系。
     * 使用Spatial SQL函数ST_Transform进行转换。
     *
     * @param geometry 查询条件中的几何图形。
     * @return 经过坐标系转换的GeometryCollection实体列表。
     */
    @Query(value = "SELECT ST_Transform(g.geometryCollection,3857) AS geometryCollection,g.* FROM test_geometry_collection AS g WHERE g.geometryCollection = :geometry", nativeQuery = true)
    List<GeometryCollectionEntity> transform(@Param("geometry") GeometryCollection geometry);

    /**
     * 将所有GeometryCollection实体的几何属性转换为EPSG:3857坐标系。
     * 使用Spatial SQL函数ST_Transform进行转换。
     *
     * @return 经过坐标系转换的所有GeometryCollection实体列表。
     */
    @Query(value = "SELECT ST_Transform(g.geometryCollection,3857) AS geometryCollection,g.* FROM test_geometry_collection AS g", nativeQuery = true)
    List<GeometryCollectionEntity> transformGeometries();

    /**
     * 根据给定的几何图形查询对应的GeometryCollection的几何类型。
     * 使用Spatial SQL函数ST_GeometryType进行查询。
     *
     * @param geometry 查询条件中的几何图形。
     * @return 对应几何图形的GeometryCollection的几何类型列表。
     */
    @Query(value = "SELECT ST_GeometryType(g.geometryCollection) AS geometry_type,g.* FROM test_geometry_collection AS g WHERE g.geometryCollection = :geometry", nativeQuery = true)
    List<GeometryCollectionEntity> getGeometryType(@Param("geometry") GeometryCollection geometry);

    /**
     * 查询所有GeometryCollection实体的几何类型。
     * 使用Spatial SQL函数ST_GeometryType进行查询。
     *
     * @return 所有GeometryCollection实体的几何类型列表。
     */
    @Query(value = "SELECT ST_GeometryType(g.geometryCollection) AS geometry_type,g.* FROM test_geometry_collection AS g", nativeQuery = true)
    List<GeometryCollectionEntity> getGeometryType();
}