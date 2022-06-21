/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-10 16:32
 * @Since:
 */
package com.zja.flywaydb.controller;

import com.zja.flywaydb.entity.Person;
import com.zja.flywaydb.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Person person) {
        return ResponseEntity.ok(personService.save(person));
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity queryById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Person person) {
        return ResponseEntity.ok(personService.update(person));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.ok(personService.delete(id));
    }
}
