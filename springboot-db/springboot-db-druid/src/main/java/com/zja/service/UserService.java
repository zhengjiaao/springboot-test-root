/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-07 17:34
 * @Since:
 */
package com.zja.service;

import com.zja.model.dto.PageData;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserPageSearchRequest;
import com.zja.model.request.UserRequest;
import com.zja.model.request.UserUpdateRequest;

/**
 * 用户服务
 * @author: zhengja
 * @since: 2023/08/07 17:34
 */
public interface UserService {

    /**
     * 查询用户
     */
    UserDTO findById(String id);

    /**
     * 分页查询用户
     */
    PageData<UserDTO> pageList(UserPageSearchRequest pageSearchRequest);

    /**
     * 新增用户
     */
    UserDTO save(UserRequest request);

    /**
     * 更新用户
     *
     * @param id
     */
    UserDTO update(String id, UserUpdateRequest updateRequest);

    /**
     * 删除用户
     *
     * @param id
     */
    void deleteById(String id);
}