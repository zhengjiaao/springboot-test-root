// package com.zja.hanbian.dao;
//
// import com.zja.hanbian.entity.项目;
// import com.zja.hanbian.汉编应用程序;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import java.util.Optional;
//
// /**
//  * @Author: zhengja
//  * @Date: 2024-11-11 10:40
//  */
// @SpringBootTest(classes = 汉编应用程序.class)
// public class 项目测试 {
//
//     @Autowired
//     private 项目Repo 项目Repo;
//
//     @Test
//     public void test() {
//         // 1. 插入数据
//         项目 project = new 项目();
//         project.setName("test");
//         project.setRemarks("test");
//         // project.setCreateTime(LocalDateTime.now());
//         // project.setLastModifiedDate(LocalDateTime.now());
//         项目Repo.save(project);
//
//         // 2. 查询数据
//         Optional<项目> optional = 项目Repo.findByName("test");
//         System.out.println(optional.get());
//
//         // 3. 更新数据
//         项目 project1 = optional.get();
//         project1.setRemarks("test1");
//         项目Repo.save(project1);
//     }
//
// }
