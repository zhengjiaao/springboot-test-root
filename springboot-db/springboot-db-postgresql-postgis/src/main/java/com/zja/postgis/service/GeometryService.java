package com.zja.postgis.service;

import com.zja.postgis.model.dto.GeometryDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.GeometryPageRequest;
import com.zja.postgis.model.request.GeometryRequest;
import com.zja.postgis.model.request.GeometryUpdateRequest;

import java.util.List;

/**
 * 几何对象 服务层
 * @author: zhengja
 * @since: 2024/07/15 14:30
 */
public interface GeometryService {

    /**
     * 查询几何对象
     *
     * @param id 几何对象id
     */
    GeometryDTO findById(String id);

    /**
     * 分页查询几何对象
     */
    PageData<GeometryDTO> pageList(GeometryPageRequest request);

    /**
     * 新增几何对象
     */
    GeometryDTO add(GeometryRequest request);

    /**
     * 批量添加几何对象
     */
    List<GeometryDTO> addBatch(List<GeometryRequest> GeometryRequests);

    /**
     * 更新几何对象
     *
     * @param id 几何对象id
     */
    GeometryDTO update(String id, GeometryUpdateRequest request);

    /**
     * 删除几何对象
     *
     * @param id 几何对象id
     */
    boolean deleteById(String id);

    /**
     * 批量删除几何对象
     *
     * @param ids 几何对象ids
     */
    void deleteBatch(List<String> ids);

}