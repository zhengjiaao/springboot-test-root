package com.zja.file.resources.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.zja.file.resources.model.UserDTO;
import com.zja.file.resources.util.ResourcesFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 演示读取资源文件（包含jar部署时，读取资源文件）
 *
 * @author: zhengja
 * @since: 2024/04/18 16:48
 */
@Validated
@RestController
@RequestMapping("/rest/user")
@Api(tags = {"用户管理页面"})
public class UserController {

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个详情")
    public UserDTO queryById(@PathVariable("id") String id) throws IOException {
        return ResourcesFileUtil.readJSONObjectFromFile("/mock/User.json", UserDTO.class);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询列表")
    public List<UserDTO> pageList() throws IOException {
        return ResourcesFileUtil.readJSONArrayFromFile("/mock/UserList.json", List.class);
    }

}