/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 15:44
 * @Since:
 */
package com.zja.controller;

import com.zja.model.UserDTO;
import com.zja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * http://127.0.0.1:8080/user/get
     */
    @GetMapping("/get")
    public UserDTO getUserDTO(){
        return userService.getUserDTO();
    }
}
