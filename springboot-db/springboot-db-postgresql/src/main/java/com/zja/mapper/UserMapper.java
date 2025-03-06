package com.zja.mapper;

import com.zja.entity.User;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * User 属性映射
 *
 * @author: zhengja
 * @since: 2025/03/05 14:03
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserRequest request);

    UserDTO map(User entity);

    User map(UserDTO dto);

    List<UserDTO> mapList(List<User> entityList);

    // Set、List、Map
    List<UserDTO> mapList(Collection<User> Users);
}
