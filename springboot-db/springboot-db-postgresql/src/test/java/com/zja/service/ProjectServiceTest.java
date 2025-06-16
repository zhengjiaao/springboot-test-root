package com.zja.service;

import com.zja.model.base.PageData;
import com.zja.model.dto.ProjectDTO;
import com.zja.model.dto.ProjectPageDTO;
import com.zja.model.request.ProjectPageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: zhengja
 * @Date: 2025-05-28 16:36
 */
@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void pageList_test() {
        ProjectPageRequest request = new ProjectPageRequest();

        LocalDateTime dateTimeStart = LocalDateTime.parse("2025-05-09 00:00:00",INPUT_FORMATTER);
        LocalDateTime dateTimeEnd = LocalDateTime.parse("2025-05-11 00:00:00",INPUT_FORMATTER);
        // request.setCreateTimeStart(LocalDateTime.of(2025, 5, 1, 0, 0, 0));
        // request.setCreateTimeEnt(LocalDateTime.of(2025, 6, 1, 0, 0, 0));

        request.setCreateTimeStart(dateTimeStart);
        request.setCreateTimeEnt(dateTimeEnd);

        PageData<ProjectPageDTO> pageData = projectService.pageList(request);
        System.out.println(pageData);
    }
}
