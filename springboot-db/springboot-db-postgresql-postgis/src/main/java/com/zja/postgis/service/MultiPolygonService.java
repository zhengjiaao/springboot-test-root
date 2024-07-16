package com.zja.postgis.service;

import com.zja.postgis.model.dto.MultiPolygonDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.MultiPolygonPageRequest;
import com.zja.postgis.model.request.MultiPolygonRequest;
import com.zja.postgis.model.request.MultiPolygonUpdateRequest;

import java.util.List;

/**
 * 多边形集合(多面) 服务层
 * @author: zhengja
 * @since: 2024/07/15 15:18
 */
public interface MultiPolygonService {

    /**
     * 查询多边形集合(多面)
     *
     * @param id 多边形集合(多面)id
     */
    MultiPolygonDTO findById(String id);

    /**
     * 分页查询多边形集合(多面)
     */
    PageData<MultiPolygonDTO> pageList(MultiPolygonPageRequest request);

    /**
     * 新增多边形集合(多面)
     */
    MultiPolygonDTO add(MultiPolygonRequest request);

    /**
     * 批量添加多边形集合(多面)
     */
    List<MultiPolygonDTO> addBatch(List<MultiPolygonRequest> MultiPolygonRequests);

    /**
     * 更新多边形集合(多面)
     *
     * @param id 多边形集合(多面)id
     */
    MultiPolygonDTO update(String id, MultiPolygonUpdateRequest request);

    /**
     * 删除多边形集合(多面)
     *
     * @param id 多边形集合(多面)id
     */
    boolean deleteById(String id);

    /**
     * 批量删除多边形集合(多面)
     *
     * @param ids 多边形集合(多面)ids
     */
    void deleteBatch(List<String> ids);

}