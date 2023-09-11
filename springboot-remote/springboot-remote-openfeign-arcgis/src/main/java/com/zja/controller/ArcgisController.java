/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-06 17:05
 * @Since:
 */
package com.zja.controller;

import com.zja.dto.arcgis.MapServerDTO;
import com.zja.remote.arcgis.ArcgisServiceRestImpl;
import com.zja.remote.arcgis.rest.ArcgisServiceFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhengja
 * @since: 2023/09/06 17:05
 */
@Validated
@RestController
@RequestMapping("/rest/arcgis")
@Api(tags = {"Arcgis 服务调用示例"})
public class ArcgisController {

    @Autowired
    ArcgisServiceRestImpl service;

    @Autowired
    ArcgisServiceFeign feign;

    @GetMapping("/query/v1")
    @ApiOperation("查询服务详情")
    public MapServerDTO v1() {
        return service.GetMapServer("ZJST/HB",0);
    }

    @GetMapping("/query/v2")
    @ApiOperation("查询服务详情")
    public MapServerDTO v2() {
        return feign.getMapServer("ZJST/HB",0,"code like '330702%'");
    }

}