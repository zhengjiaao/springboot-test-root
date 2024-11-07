package com.zja.mvc.web.controller;

import com.zja.mvc.web.model.UserDTO;
import com.zja.mvc.web.util.ContextPathUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-11-07 13:34
 */
@Validated
@RestController
@RequestMapping("/rest/web")
@Api(tags = {"WEB管理页面"})
public class WebController {

    @GetMapping("/context/path")
    @ApiOperation("getContextPathURL")
    public String getContextPathURL() {
        return ContextPathUtil.getContextPathURL();
    }

    @GetMapping("/context/path/api")
    @ApiOperation("getAPIPathURL")
    public String getAPIPathURL(@RequestParam String path) {
        return ContextPathUtil.getPathURL(path);
    }

    @GetMapping("/context/path/temp/file")
    @ApiOperation("getTempFilePath")
    public String getTempFilePath(@RequestParam String fileName) throws IOException {
        return ContextPathUtil.getTempFilePath(fileName);
    }

}
