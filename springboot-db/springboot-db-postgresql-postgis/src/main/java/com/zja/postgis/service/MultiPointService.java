package com.zja.postgis.service;

import com.zja.postgis.model.dto.MultiPointDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.MultiPointPageRequest;
import com.zja.postgis.model.request.MultiPointRequest;
import com.zja.postgis.model.request.MultiPointUpdateRequest;

import java.util.List;

/**
 * 点集合 服务层
 * @author: zhengja
 * @since: 2024/07/15 15:10
 */
public interface MultiPointService {

    /**
     * 查询点集合
     *
     * @param id 点集合id
     */
    MultiPointDTO findById(String id);

    /**
     * 分页查询点集合
     */
    PageData<MultiPointDTO> pageList(MultiPointPageRequest request);

    /**
     * 新增点集合
     */
    MultiPointDTO add(MultiPointRequest request);

    /**
     * 批量添加点集合
     */
    List<MultiPointDTO> addBatch(List<MultiPointRequest> MultiPointRequests);

    /**
     * 更新点集合
     *
     * @param id 点集合id
     */
    MultiPointDTO update(String id, MultiPointUpdateRequest request);

    /**
     * 删除点集合
     *
     * @param id 点集合id
     */
    boolean deleteById(String id);

    /**
     * 批量删除点集合
     *
     * @param ids 点集合ids
     */
    void deleteBatch(List<String> ids);

}