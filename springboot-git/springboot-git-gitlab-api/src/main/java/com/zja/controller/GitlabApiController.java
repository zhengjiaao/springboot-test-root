package com.zja.controller;

import com.zja.service.GitlabApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zhengja
 * @since: 2023/11/27 16:11
 */
@Validated
@RestController
@RequestMapping("/rest/gitlab/api")
@Api(tags = {"页面"})
public class GitlabApiController {

    @Autowired
    GitlabApiService service;

    @GetMapping("/new/projectGroup")
    @ApiOperation("申请新建项目组")
    public Object newProjectGroup(@RequestParam String groupName) {
        return service.newProjectGroup(groupName);
    }

    @PostMapping("/history")
    @ApiOperation("查看历史记录")
    public Object add() {
        return null;
    }

}