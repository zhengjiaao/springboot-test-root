/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 15:30
 * @Since:
 */
package com.zja.service;

import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserRequest;

import java.util.List;

public interface UserService {

    UserDTO save(UserRequest request);

    List<UserDTO> userDTOList();

}
