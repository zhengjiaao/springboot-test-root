package com.zja.postgis.service;

import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.dto.PolygonDTO;
import com.zja.postgis.model.request.PolygonPageRequest;
import com.zja.postgis.model.request.PolygonRequest;
import com.zja.postgis.model.request.PolygonUpdateRequest;

import java.util.List;

/**
 * 多边形(面) 服务层
 * @author: zhengja
 * @since: 2024/07/15 13:40
 */
public interface PolygonService {

    /**
     * 查询多边形(面)
     *
     * @param id 多边形(面)id
     */
    PolygonDTO findById(String id);

    /**
     * 分页查询多边形(面)
     */
    PageData<PolygonDTO> pageList(PolygonPageRequest request);

    /**
     * 新增多边形(面)
     */
    PolygonDTO add(PolygonRequest request);

    /**
     * 批量添加多边形(面)
     */
    List<PolygonDTO> addBatch(List<PolygonRequest> PolygonRequests);

    /**
     * 更新多边形(面)
     *
     * @param id 多边形(面)id
     */
    PolygonDTO update(String id, PolygonUpdateRequest request);

    /**
     * 删除多边形(面)
     *
     * @param id 多边形(面)id
     */
    boolean deleteById(String id);

    /**
     * 批量删除多边形(面)
     *
     * @param ids 多边形(面)ids
     */
    void deleteBatch(List<String> ids);

}