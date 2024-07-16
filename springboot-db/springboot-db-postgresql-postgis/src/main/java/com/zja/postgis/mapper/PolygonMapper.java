package com.zja.postgis.mapper;

import com.zja.postgis.model.PolygonEntity;
import com.zja.postgis.model.dto.PolygonDTO;
import com.zja.postgis.model.request.PolygonRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Polygon 属性映射
 * @author: zhengja
 * @since: 2024/07/15 13:54
 */
@Mapper(componentModel = "spring")
public interface PolygonMapper {

    PolygonEntity map(PolygonRequest request);

    PolygonDTO map(PolygonEntity entity);

    PolygonEntity map(PolygonDTO dto);

    List<PolygonDTO> mapList(List<PolygonEntity> entityList);

    // Set、List、Map
    List<PolygonDTO> mapList(Collection<PolygonEntity> Polygons);
}
