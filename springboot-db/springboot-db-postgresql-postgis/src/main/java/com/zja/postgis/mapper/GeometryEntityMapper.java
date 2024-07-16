package com.zja.postgis.mapper;

import com.zja.postgis.model.GeometryEntity;
import com.zja.postgis.model.dto.GeometryDTO;
import com.zja.postgis.model.request.GeometryRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Geometry 属性映射
 * @author: zhengja
 * @since: 2024/07/15 14:31
 */
@Mapper(componentModel = "spring")
public interface GeometryEntityMapper {

    GeometryEntity map(GeometryRequest request);

    GeometryDTO map(GeometryEntity entity);

    GeometryEntity map(GeometryDTO dto);

    List<GeometryDTO> mapList(List<GeometryEntity> entityList);

    // Set、List、Map
    List<GeometryDTO> mapList(Collection<GeometryEntity> Geometrys);
}
