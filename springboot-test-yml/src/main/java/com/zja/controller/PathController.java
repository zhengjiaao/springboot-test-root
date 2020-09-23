package com.zja.controller;

import com.zja.util.ContextPathUtil;
import com.zja.util.FileUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-27 14:35
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RestController
@Api(tags = {"PathController"}, description = "项目路径测试")
public class PathController {

    @GetMapping("path")
    public Object get(HttpServletRequest httpServletRequest) throws Exception {
        Map map = new HashMap();
        map.put("getContextPath",ContextPathUtil.getContextPath("/a",httpServletRequest));
        map.put("getBaseURL",ContextPathUtil.getBaseURL(httpServletRequest));
        //map.put("getURLByFilePath",ContextPathUtil.getURLByFilePath("b",httpServletRequest));
        map.put("getModuleDirectory", FileUtils.getModuleDirectory(httpServletRequest));
        return map;
    }
}
