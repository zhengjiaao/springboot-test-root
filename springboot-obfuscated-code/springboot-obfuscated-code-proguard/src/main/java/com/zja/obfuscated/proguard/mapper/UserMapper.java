package com.zja.obfuscated.proguard.mapper;

import com.zja.obfuscated.proguard.model.dto.UserDTO;
import com.zja.obfuscated.proguard.model.entity.User;
import com.zja.obfuscated.proguard.model.request.UserRequest;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:32
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserRequest request);

    UserDTO map(User entity);

    User map(UserDTO dto);

    List<UserDTO> mapList(List<User> entityList);

    List<UserDTO> mapList(Collection<User> entitySet);

}
