package com.zja.mapper;

import com.zja.entity.Product;
import com.zja.model.dto.ProductDTO;
import com.zja.model.request.ProductRequest;
import com.zja.model.request.ProductUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 产品映射器
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Product toEntity(ProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

    @Mappings({
            @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);
}