package com.zja.postgis.mapper;

import com.zja.postgis.model.MultiLineStringEntity;
import com.zja.postgis.model.dto.MultiLineStringDTO;
import com.zja.postgis.model.request.MultiLineStringRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * MultiLineString 属性映射
 * @author: zhengja
 * @since: 2024/07/15 15:18
 */
@Mapper(componentModel = "spring")
public interface MultiLineStringMapper {

    MultiLineStringEntity map(MultiLineStringRequest request);

    MultiLineStringDTO map(MultiLineStringEntity entity);

    MultiLineStringEntity map(MultiLineStringDTO dto);

    List<MultiLineStringDTO> mapList(List<MultiLineStringEntity> entityList);

    // Set、List、Map
    List<MultiLineStringDTO> mapList(Collection<MultiLineStringEntity> MultiLineStrings);
}
