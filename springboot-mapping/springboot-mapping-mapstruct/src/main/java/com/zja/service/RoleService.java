/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 13:38
 * @Since:
 */
package com.zja.service;

import com.zja.model.dto.RoleDTO;
import com.zja.model.request.RolePageSearchRequest;
import com.zja.model.request.RoleRequest;
import com.zja.model.request.RoleUpdateRequest;

import java.util.List;

/**
 * 角色服务
 * @author: zhengja
 * @since: 2023/07/17 13:38
 */
public interface RoleService {

    /**
     * 查询角色
     */
    RoleDTO findById(String id);

    /**
     * 分页查询角色
     */
    List<RoleDTO> pageList(RolePageSearchRequest pageSearchRequest);

    /**
     * 新增角色
     */
    RoleDTO save(RoleRequest request);

    /**
     * 更新角色
     *
     * @param id
     */
    RoleDTO update(String id, RoleUpdateRequest updateRequest);

    /**
     * 删除角色
     *
     * @param id
     */
    void deleteById(String id);
}