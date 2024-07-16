package com.zja.postgis.service;

import com.zja.postgis.model.dto.MultiLineStringDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.MultiLineStringPageRequest;
import com.zja.postgis.model.request.MultiLineStringRequest;
import com.zja.postgis.model.request.MultiLineStringUpdateRequest;

import java.util.List;

/**
 * 线集合 服务层
 * @author: zhengja
 * @since: 2024/07/15 15:17
 */
public interface MultiLineStringService {

    /**
     * 查询线集合
     *
     * @param id 线集合id
     */
    MultiLineStringDTO findById(String id);

    /**
     * 分页查询线集合
     */
    PageData<MultiLineStringDTO> pageList(MultiLineStringPageRequest request);

    /**
     * 新增线集合
     */
    MultiLineStringDTO add(MultiLineStringRequest request);

    /**
     * 批量添加线集合
     */
    List<MultiLineStringDTO> addBatch(List<MultiLineStringRequest> MultiLineStringRequests);

    /**
     * 更新线集合
     *
     * @param id 线集合id
     */
    MultiLineStringDTO update(String id, MultiLineStringUpdateRequest request);

    /**
     * 删除线集合
     *
     * @param id 线集合id
     */
    boolean deleteById(String id);

    /**
     * 批量删除线集合
     *
     * @param ids 线集合ids
     */
    void deleteBatch(List<String> ids);

}