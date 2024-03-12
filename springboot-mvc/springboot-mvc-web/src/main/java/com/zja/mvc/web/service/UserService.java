package com.zja.mvc.web.service;

import com.zja.mvc.web.model.*;

/**
 * 服务
 *
 * @author: zhengja
 * @since: 2024/03/11 15:20
 */
public interface UserService {

    /**
     * 查询
     */
    UserDTO findById(String id);

    /**
     * 分页查询
     */
    PageData<UserDTO> pageList(UserPageSearchRequest request);

    /**
     * 新增
     */
    UserDTO add(UserRequest request);

    /**
     * 更新
     *
     * @param id
     */
    UserDTO update(String id, UserUpdateRequest request);

    /**
     * 删除
     *
     * @param id
     */
    void deleteById(String id);
}