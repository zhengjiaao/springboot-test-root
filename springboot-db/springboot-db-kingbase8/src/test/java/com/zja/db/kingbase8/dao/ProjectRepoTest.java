package com.zja.db.kingbase8.dao;

import com.zja.db.kingbase8.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

/**
 * @Author: zhengja
 * @Date: 2025-05-26 15:41
 */
@SpringBootTest
public class ProjectRepoTest {

    @Autowired
    ProjectRepo repo;

    @Test
    public void add_test() {
        // repo.deleteAll(); // 感到疑惑是添加清理数据时，会导致下放插入数据保存失败，创建时间是NULL异常

        for (int i = 1; i < 10; i++) {
            Project p = new Project();
            p.setName("名称-" + i);
            repo.save(p);
        }
    }

    @Test
    public void findByName_test() {
        Optional<Project> optional = repo.findByName("名称-1");
        optional.ifPresent(project -> {
            System.out.println(project.getId());
            System.out.println(project.getName());
            System.out.println(project.getCreateTime());
            System.out.println(project.getLastModifiedDate());
        });
    }
}
