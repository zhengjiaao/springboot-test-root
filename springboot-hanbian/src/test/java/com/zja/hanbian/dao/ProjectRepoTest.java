package com.zja.hanbian.dao;

import com.zja.hanbian.entity.Project;
import com.zja.hanbian.汉编应用程序;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Author: zhengja
 * @Date: 2024-11-11 10:40
 */
@SpringBootTest(classes = 汉编应用程序.class)
public class ProjectRepoTest {

    @Autowired
    private ProjectRepo projectRepo;

    @Test
    public void test() {
        // 1. 插入数据
        Project project = new Project();
        project.setName("test");
        project.setRemarks("test");
        // project.setCreateTime(LocalDateTime.now());
        // project.setLastModifiedDate(LocalDateTime.now());
        projectRepo.save(project);

        // 2. 查询数据
        Optional<Project> optional = projectRepo.findByName("test");
        System.out.println(optional.get());

        // 3. 更新数据
        Project project1 = optional.get();
        project1.setRemarks("test1");
        projectRepo.save(project1);
    }

}
