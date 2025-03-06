package com.zja.service;

import com.zja.model.base.PageData;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserPageRequest;
import com.zja.model.request.UserRequest;
import com.zja.model.request.UserUpdateRequest;

import java.util.List;

/**
 * 用户 服务层
 *
 * @author: zhengja
 * @since: 2025/03/05 14:02
 */
public interface UserService {

    /**
     * 查询用户
     *
     * @param id 用户id
     */
    UserDTO queryById(String id);

    /**
     * 查询用户列表
     */
    List<UserDTO> list();

    /**
     * 分页查询用户
     */
    PageData<UserDTO> pageList(UserPageRequest request);

    /**
     * 校验用户名称是否可用
     *
     * @param name 用户名称
     * @return Boolean
     */
    Boolean existName(String name);

    /**
     * 新增用户
     */
    UserDTO add(UserRequest request);

    /**
     * 批量添加用户
     */
    List<UserDTO> addBatch(List<UserRequest> requests);

    /**
     * 更新用户
     *
     * @param id 用户id
     */
    UserDTO update(String id, UserUpdateRequest request);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    boolean deleteById(String id);

    /**
     * 批量删除用户
     *
     * @param ids 用户ids
     */
    void deleteBatch(List<String> ids);

}