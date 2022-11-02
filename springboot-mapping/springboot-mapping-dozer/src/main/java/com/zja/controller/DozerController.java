/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 17:13
 * @Since:
 */
package com.zja.controller;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dozer")
public class DozerController {

    @Autowired
    private Mapper mapper;

    @GetMapping("{id}")
    public ResponseEntity queryById(@PathVariable Long id) {
        return ResponseEntity.ok(id);
    }
}
