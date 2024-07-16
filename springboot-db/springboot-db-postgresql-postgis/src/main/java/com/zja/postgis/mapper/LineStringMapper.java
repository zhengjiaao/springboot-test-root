package com.zja.postgis.mapper;

import com.zja.postgis.model.LineStringEntity;
import com.zja.postgis.model.dto.LineStringDTO;
import com.zja.postgis.model.request.LineStringRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * LineString 属性映射
 * @author: zhengja
 * @since: 2024/07/15 13:43
 */
@Mapper(componentModel = "spring")
public interface LineStringMapper {

    LineStringEntity map(LineStringRequest request);

    LineStringDTO map(LineStringEntity entity);

    LineStringEntity map(LineStringDTO dto);

    List<LineStringDTO> mapList(List<LineStringEntity> entityList);

    // Set、List、Map
    List<LineStringDTO> mapList(Collection<LineStringEntity> LineStrings);
}
