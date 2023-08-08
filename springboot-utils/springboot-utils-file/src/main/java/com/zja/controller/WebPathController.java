/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 11:29
 * @Since:
 */
package com.zja.controller;

import com.zja.util.ContextPathUtil;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhengja
 * @since: 2023/08/08 11:29
 */
@Validated
@RestController
@RequestMapping("/rest")
@Api(tags = {"页面"})
public class WebPathController {

    @GetMapping("/get/ContextPath")
    public String get(HttpServletRequest request) throws Exception {
        return ContextPathUtil.getRootContextPath(request);
    }

}