package com.zja.postgis.service;

import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.dto.PointDTO;
import com.zja.postgis.model.request.PointPageRequest;
import com.zja.postgis.model.request.PointRequest;
import com.zja.postgis.model.request.PointUpdateRequest;

import java.util.List;

/**
 * 点 服务层
 * @author: zhengja
 * @since: 2024/07/15 13:38
 */
public interface PointService {

    /**
     * 查询点
     *
     * @param id 点id
     */
    PointDTO findById(String id);

    /**
     * 分页查询点
     */
    PageData<PointDTO> pageList(PointPageRequest request);

    /**
     * 新增点
     */
    PointDTO add(PointRequest request);

    /**
     * 批量添加点
     */
    List<PointDTO> addBatch(List<PointRequest> PointRequests);

    /**
     * 更新点
     *
     * @param id 点id
     */
    PointDTO update(String id, PointUpdateRequest request);

    /**
     * 删除点
     *
     * @param id 点id
     */
    boolean deleteById(String id);

    /**
     * 批量删除点
     *
     * @param ids 点ids
     */
    void deleteBatch(List<String> ids);

}