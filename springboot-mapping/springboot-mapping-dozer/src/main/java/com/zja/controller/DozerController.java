/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 17:13
 * @Since:
 */
package com.zja.controller;

import com.zja.model.UserDTO;
import com.zja.model.UserVO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dozer")
public class DozerController {

    @Autowired
    private Mapper mapper;

    /**
     * http://127.0.0.1:8080/dozer/query
     */
    @GetMapping("/query")
    public UserVO query() {
        UserDTO userDTO = new UserDTO("lisi", "123", 666L);
        //属性拷贝
        UserVO userVO = mapper.map(userDTO, UserVO.class);
        return userVO;
    }
}
