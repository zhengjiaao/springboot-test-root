package com.zja.obfuscated.proguard.service;

import com.zja.obfuscated.proguard.model.dto.PageData;
import com.zja.obfuscated.proguard.model.dto.UserDTO;
import com.zja.obfuscated.proguard.model.request.UserPageSearchRequest;
import com.zja.obfuscated.proguard.model.request.UserRequest;
import com.zja.obfuscated.proguard.model.request.UserUpdateRequest;

/**
 * 服务
 *
 * @author: zhengja
 * @since: 2024/01/26 15:32
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