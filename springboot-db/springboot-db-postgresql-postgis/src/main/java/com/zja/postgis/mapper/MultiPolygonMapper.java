package com.zja.postgis.mapper;

import com.zja.postgis.model.MultiPolygonEntity;
import com.zja.postgis.model.dto.MultiPolygonDTO;
import com.zja.postgis.model.request.MultiPolygonRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * MultiPolygon 属性映射
 * @author: zhengja
 * @since: 2024/07/15 15:20
 */
@Mapper(componentModel = "spring")
public interface MultiPolygonMapper {

    MultiPolygonEntity map(MultiPolygonRequest request);

    MultiPolygonDTO map(MultiPolygonEntity entity);

    MultiPolygonEntity map(MultiPolygonDTO dto);

    List<MultiPolygonDTO> mapList(List<MultiPolygonEntity> entityList);

    // Set、List、Map
    List<MultiPolygonDTO> mapList(Collection<MultiPolygonEntity> MultiPolygons);
}
