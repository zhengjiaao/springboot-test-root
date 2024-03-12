package com.zja.mvc.filter.service;

import com.zja.mvc.filter.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:24
 */
@Slf4j
@Validated
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO findById(String id) {

        if ("1".equals(id)) {
            return UserDTO.builder().id("1").name("李四1").age(21).build();
        }

        return null;
    }

    @Override
    public PageData<UserDTO> pageList(UserPageSearchRequest request) {
        int page = request.getPage();
        int size = request.getSize();

        List<UserDTO> dtoList = new ArrayList<>();
        dtoList.add(UserDTO.builder().id("1").name("李四1").age(21).build());
        dtoList.add(UserDTO.builder().id("2").name("李四2").age(22).build());
        dtoList.add(UserDTO.builder().id("3").name("李四3").age(23).build());
        dtoList.add(UserDTO.builder().id("4").name("李四4").age(24).build());
        dtoList.add(UserDTO.builder().id("5").name("李四5").age(25).build());
        dtoList.add(UserDTO.builder().id("6").name("李四6").age(26).build());
        dtoList.add(UserDTO.builder().id("7").name("李四7").age(27).build());

        return PageData.of(dtoList, page, size, 7);
    }

    @Override
    public UserDTO add(UserRequest request) {
        return UserDTO.builder().id(request.getId()).name(request.getName()).build();
    }

    @Override
    public UserDTO update(String id, UserUpdateRequest request) {
        UserDTO dto = findById(id);
        if (null != dto) {
            dto.setName(request.getName());
        }
        return dto;
    }

    @Override
    public void deleteById(String id) {

    }

}