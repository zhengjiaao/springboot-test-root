/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-07 17:42
 * @Since:
 */
package com.zja.pool.hikaricp.mapper;

import com.zja.pool.hikaricp.entity.User;
import com.zja.pool.hikaricp.model.dto.UserDTO;
import com.zja.pool.hikaricp.model.request.UserRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/08/07 17:42
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserRequest request);

    UserDTO map(User entity);

    User map(UserDTO dto);

    List<UserDTO> mapList(List<User> entityList);

}
