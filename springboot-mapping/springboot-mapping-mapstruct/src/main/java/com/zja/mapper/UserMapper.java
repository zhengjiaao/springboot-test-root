/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 15:35
 * @Since:
 */
package com.zja.mapper;

import com.zja.entity.User;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

//@Mapper
@Mapper(componentModel = "spring") // 使用 spring bean 管理
public interface UserMapper {

//    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User map(UserRequest request);

    //    @Mapping(target = "name", source = "username")
    @Mappings({
            @Mapping(target = "name", source = "username")
    })
    UserDTO map(User user);

    /**
     * 转换为 DTO集合
     * @see UserMapper#map(User user) 此时 mapList 的实现为循环调用 map(User user) 并继承了@Mappings 的属性映射
     * @param userList
     */
    List<UserDTO> mapList(List<User> userList);
}
