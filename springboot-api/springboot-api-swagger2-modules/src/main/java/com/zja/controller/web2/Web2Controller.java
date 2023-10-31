/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-31 15:14
 * @Since:
 */
package com.zja.controller.web2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Web2模块
 *
 * @author: zhengja
 * @since: 2023/10/31 15:14
 */
@Validated
@RestController
@RequestMapping("/rest/web2")
@Api(tags = {"Web2模块页面"})
public class Web2Controller {

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个Web2模块详情")
    public Object queryById(@PathVariable("id") String id) {
        return "成功-" + id;
    }

}