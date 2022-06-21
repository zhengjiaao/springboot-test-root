/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-10 16:32
 * @Since:
 */
package com.zja.flywaydb.service;

import com.zja.flywaydb.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PersonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //添加数据
    public int save(Person person) {
        String sql = "INSERT INTO person VALUES (?, ?, ?);";
        int i = jdbcTemplate.update(sql, person.getId(), person.getFirstname(), person.getLastname());
        System.out.println("影响的行数: " + i);
        return i;
    }

    //根据id查询数据
    public Person findById(Long id) {
        String sql = "select * from person where id='%s'";
        Person person = (Person) jdbcTemplate.queryForObject(String.format(sql, id), new BeanPropertyRowMapper(Person.class));
        return person;
    }

    //修改数据
    public int update(Person person) {
        String sql = "UPDATE person SET firstname=?, lastname=? WHERE id=?;";
        int i = jdbcTemplate.update(sql, person.getFirstname(), person.getLastname(), person.getId());
        System.out.println("影响的行数: " + i);
        return i;
    }

    //删除数据
    public int delete(Long id) {
        String sql = "DELETE FROM person WHERE id=?;";
        int i = jdbcTemplate.update(sql, id);
        System.out.println("影响的行数: " + i);
        return i;
    }

    /**
     * jdbc 批量处理
     * 注意：需要在jdbc URL中添加一个参数才支持批量 rewriteBatchedStatements=true
     */
    @Transactional(rollbackFor = Exception.class)
    public void jdbcSaveAllByBatch(List<Person> entityList) {
        if (ObjectUtils.isEmpty(entityList)) {
            return;
        }

        String sql = "INSERT INTO person (id,firstname,lastname) VALUES (?,?,?) ";

        List<Object[]> objectList = new ArrayList<>();
        for (Person entity : entityList) {
            //顺序与 sql 保持一致
            objectList.add(new Object[]{
                    entity.getId(),
                    entity.getFirstname(),
                    entity.getLastname()
            });
        }

        jdbcTemplate.batchUpdate(sql, objectList);
    }
}
