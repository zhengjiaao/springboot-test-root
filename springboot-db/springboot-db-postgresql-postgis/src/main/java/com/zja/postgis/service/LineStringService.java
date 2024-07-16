package com.zja.postgis.service;

import com.zja.postgis.model.dto.LineStringDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.LineStringPageRequest;
import com.zja.postgis.model.request.LineStringRequest;
import com.zja.postgis.model.request.LineStringUpdateRequest;

import java.util.List;

/**
 * 线 服务层
 * @author: zhengja
 * @since: 2024/07/15 13:41
 */
public interface LineStringService {

    /**
     * 查询线
     *
     * @param id 线id
     */
    LineStringDTO findById(String id);

    /**
     * 分页查询线
     */
    PageData<LineStringDTO> pageList(LineStringPageRequest request);

    /**
     * 新增线
     */
    LineStringDTO add(LineStringRequest request);

    /**
     * 批量添加线
     */
    List<LineStringDTO> addBatch(List<LineStringRequest> LineStringRequests);

    /**
     * 更新线
     *
     * @param id 线id
     */
    LineStringDTO update(String id, LineStringUpdateRequest request);

    /**
     * 删除线
     *
     * @param id 线id
     */
    boolean deleteById(String id);

    /**
     * 批量删除线
     *
     * @param ids 线ids
     */
    void deleteBatch(List<String> ids);

}