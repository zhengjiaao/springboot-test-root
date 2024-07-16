package com.zja.postgis.mapper;

import com.zja.postgis.model.PointEntity;
import com.zja.postgis.model.dto.PointDTO;
import com.zja.postgis.model.request.PointRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Point 属性映射
 * @author: zhengja
 * @since: 2024/07/15 13:54
 */
@Mapper(componentModel = "spring")
public interface PointMapper {

    PointEntity map(PointRequest request);

    PointDTO map(PointEntity entity);

    PointEntity map(PointDTO dto);

    List<PointDTO> mapList(List<PointEntity> entityList);

    // Set、List、Map
    List<PointDTO> mapList(Collection<PointEntity> Points);
}
