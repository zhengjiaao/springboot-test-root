package com.zja.dao;

import com.alibaba.fastjson.JSON;
import com.zja.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author: zhengja
 * @Date: 2024-07-01 15:31
 */
@SpringBootTest
public class ProjectDaoTest {

    @Autowired
    ProjectRepo repo;

    @Test
    public void add_test() {
        // repo.deleteAll(); // 感到疑惑是添加清理数据时，会导致下放插入数据保存失败，创建时间是NULL异常

        for (int i = 1; i < 10; i++) {
            Project p = new Project();
            p.setName("名称-" + i);

            p.setConfigJson(JSON.parseObject("{\"key\":\"value\"}")); // json 字段
            p.setConfigText("大文本字段"); // 大文本字段或json文本
            repo.save(p);
        }
    }

    @Test
    @Transactional
    public void findByName_test() {
        Optional<Project> optional = repo.findByName("名称-1");
        optional.ifPresent(project -> {
            System.out.println(project.getId());
            System.out.println(project.getName());
            System.out.println(project.getCreateTime());
            System.out.println(project.getLastModifiedDate());
            System.out.println("-------------特殊字段-------------");
            System.out.println(project.getConfigJson());
            System.out.println(project.getConfigText());
            System.out.println("--------------------------");
        });
    }

    @Test
    public void findAll_test() {
        List<Project> projectList = repo.findAll();
        projectList.forEach(project -> {
            System.out.println(project.getId());
            System.out.println(project.getName());
            System.out.println(project.getCreateTime());
            System.out.println(project.getLastModifiedDate());
            System.out.println("-------------特殊字段-------------");
            System.out.println(project.getConfigJson());
            System.out.println(project.getConfigText());
            System.out.println("--------------------------");
        });
    }

    @Test
    public void deleteAll_test() {
        repo.deleteAll();
    }

}
