/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-25 13:28
 * @Since:
 */
package com.zja.controller;

import com.zja.remote.RemoteUrlFegin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "RemoteUrlController", description = "调用远程接口")
@RestController
@RequestMapping("/rest/url")
public class RemoteUrlController {

    @Autowired
    RemoteUrlFegin remoteUrlFegin;

    @ApiOperation(value = "提供get方法测试-不传参数", notes = "不传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/cropInfo", method = RequestMethod.GET)
    public Object getCropInfo() {
        return remoteUrlFegin.getCropInfo();
    }
}
