package com.zja.controller;

import com.zja.service.DataToShpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/29 11:16
 */
@RequestMapping("rest/guotu/shp")
@RestController
@Api(tags = {"GuotuDataToShpController"},description = "国土数据写入shp文件")
public class GuotuDataToShpController {

    @Autowired
    DataToShpService dataToShpService;

    @ApiOperation(value = "获取数据",notes = "获取的是国土数据",httpMethod = "GET")
    @RequestMapping(value = "v1/getData",method = RequestMethod.GET)
    public Object getData(){
        return dataToShpService.getData();
    }

    @ApiOperation(value = "获取数据-count(*)",notes = "获取的是国土数据",httpMethod = "GET")
    @RequestMapping(value = "v1/getCount",method = RequestMethod.GET)
    public Object getCount(){
        return dataToShpService.getCount();
    }

    @ApiOperation(value = "将获取的数据写一个shp文件",notes = "内部操作获取数据",httpMethod = "GET")
    @RequestMapping(value = "v1/writeDataToShp",method = RequestMethod.GET)
    public Object writeDataToShp(){
        return null;
    }


}
