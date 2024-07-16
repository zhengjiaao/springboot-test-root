package com.zja.postgis.mapper;

import com.zja.postgis.model.MultiPointEntity;
import com.zja.postgis.model.dto.MultiPointDTO;
import com.zja.postgis.model.request.MultiPointRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * MultiPoint 属性映射
 * @author: zhengja
 * @since: 2024/07/15 15:10
 */
@Mapper(componentModel = "spring")
public interface MultiPointMapper {

    MultiPointEntity map(MultiPointRequest request);

    MultiPointDTO map(MultiPointEntity entity);

    MultiPointEntity map(MultiPointDTO dto);

    List<MultiPointDTO> mapList(List<MultiPointEntity> entityList);

    // Set、List、Map
    List<MultiPointDTO> mapList(Collection<MultiPointEntity> MultiPoints);
}
