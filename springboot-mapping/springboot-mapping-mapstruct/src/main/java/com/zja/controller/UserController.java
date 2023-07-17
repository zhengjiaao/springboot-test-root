/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 15:44
 * @Since:
 */
package com.zja.controller;

import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserRequest;
import com.zja.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/rest/user")
@Api(tags = {"用户页面"})
public class UserController {

    @Autowired
    UserService userService;

    /**
     * http://localhost:8080/swagger-ui/index.html#/
     */
    @PostMapping("/save")
    public UserDTO userDTO(UserRequest request) {
        return userService.save(request);
    }

    @GetMapping("/get/list")
    public List<UserDTO> userDTOList() {
        return userService.userDTOList();
    }

}
