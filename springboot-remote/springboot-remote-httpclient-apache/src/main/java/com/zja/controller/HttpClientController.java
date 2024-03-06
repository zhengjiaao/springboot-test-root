package com.zja.controller;

import com.alibaba.fastjson.JSON;
import com.zja.dto.UserDTO;
import com.zja.util.HttpClientResult;
import com.zja.util.HttpClientUtils;
import com.zja.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient方式请求http接口
 *
 * @author: zhengja
 * @since: 2019/6/27 15:49
 */
@Deprecated
@Api(tags = {"HttpClientController"}, description = "HttpClient调用远程服务器接口")
@RestController
@RequestMapping(value = "rest/httpclient")
@EnableAsync //springboot 自带注解 controller上加上@EnableAsync,异步执行的方法上加上@Async注解
public class HttpClientController {

    @Value("${spring.server_url.get_url_userdto}")
    private String getUrlUserdto;

    @Value("${spring.server_url.get_url_userdto2}")
    private String getUrlUserdto2;

    @Value("${spring.server_url.get_url_userdtolist}")
    private String getUrlUserdtoList;

    @Value("${spring.server_url.post_url_userdto}")
    private String postUrlUserdto;

    @Value("${spring.server_url.post_url_userdto2}")
    private String postUrlUserdto2;

    @Value("${spring.server_url.post_url_userdto3}")
    private String postUrlUserdto3;

    @Value("${spring.server_url.put_url_userdto}")
    private String putUrlUserdto;

    @Value("${spring.server_url.put_url_userdto2}")
    private String putUrlUserdto2;

    @Value("${spring.server_url.delete_url_userdto}")
    private String deleteUrlUserdto;

    @Value("${spring.server_url.delete_url_userdto2}")
    private String deleteUrlUserdto2;

    //测试下载网络文件
    //@Value("${spring.web_url.file_url_getFileInfo}")
    @Value("${spring.web_url.file_url_getFileInfo2}")
    private String getFileInfo;

    //@Value("${spring.web_url.file_url_downloadSplitFile}")
    @Value("${spring.web_url.file_url_downloadSplitFile2}")
    private String downloadSplitFile;


    @ApiOperation(value = "提供get方法测试-不传参数", notes = "不传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto", method = RequestMethod.GET)
    public Object getUserDTO() throws Exception {
        System.out.println("remote:" + "进入调用方法");
        return HttpClientUtils.doGet(getUrlUserdto);
    }

    @ApiOperation(value = "提供get方法测试-传参数", notes = "传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto2", method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：默认", defaultValue = "调用成功", required = true) @RequestParam String param) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("param", "param");
        HttpClientResult result = HttpClientUtils.doGet(getUrlUserdto2, map);
        System.out.println("remote:param==" + result);

        String doGet = HttpUtils.doGet(getUrlUserdto2, map);
        System.out.println("remote:doGet==" + doGet);
        return result;
    }

    @ApiOperation(value = "提供listobject方法测试", httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit", method = RequestMethod.GET)
    public Object getUserDTOList() throws Exception {
        return HttpClientUtils.doGet(getUrlUserdtoList);
    }

    @ApiOperation(value = "提供post方法测试-传json参数", notes = "传json参数", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto", method = RequestMethod.POST)
    public Object postUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) throws Exception {
        HttpClientResult doPostJsons = HttpClientUtils.doPostJson(postUrlUserdto, JSON.toJSONString(userDto));
        UserDTO userDTOs = JSON.parseObject(doPostJsons.getContent(), UserDTO.class);
        System.out.println("remote:doPostJsons=" + doPostJsons);
        System.out.println("remote:userDTOs" + userDTOs);

        return doPostJsons;
    }

    @ApiOperation(value = "提供post方法测试-不传参", notes = "不传参", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto2", method = RequestMethod.POST)
    public Object postUserDTO2() throws Exception {
        HttpClientResult httpClientResult = HttpClientUtils.doPost(postUrlUserdto2);
        System.out.println("remote:httpClientResult==" + httpClientResult);
        return httpClientResult;
    }

    @ApiOperation(value = "提供post方法测试-传多个参数", notes = "传多个参数", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto3", method = RequestMethod.POST)
    public Object postUserDTO3(@ApiParam(value = "传参值：name", required = true) @RequestParam String name,
                               @ApiParam(value = "传参值：age", required = true) @RequestParam String age) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("age", age);
        HttpClientResult httpClientResult = HttpClientUtils.doPost(postUrlUserdto3, params);
        System.out.println("remote:httpClientResult==" + httpClientResult);
        return httpClientResult;
    }

    @ApiOperation(value = "提供put方法测试-传多个参数", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto2", method = RequestMethod.PUT)
    public Object putUserDTO2(@ApiParam(value = "传参值：name", required = true) @RequestParam String name,
                              @ApiParam(value = "传参值：age", required = true) @RequestParam String age) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("age", age);
        HttpClientResult httpClientResult = HttpClientUtils.doPut(putUrlUserdto2, params);
        System.out.println("remote:httpClientResult==" + httpClientResult);
        return httpClientResult;
    }

    @ApiOperation(value = "提供put方法测试-传json数据", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto", method = RequestMethod.PUT)
    public Object putUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) throws Exception {
        HttpClientResult doPutJson = HttpClientUtils.doPutJson(putUrlUserdto, JSON.toJSONString(userDto));
        UserDTO userDTOs = JSON.parseObject(doPutJson.getContent(), UserDTO.class);
        System.out.println("remote:doPutJson=" + doPutJson);
        System.out.println("remote:userDTOs" + userDTOs);
        return doPutJson;
    }

    @ApiOperation(value = "提供delete方法测试-不带参数", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto", method = RequestMethod.DELETE)
    public Object deleteUserDTO() throws Exception {
        System.out.println("remote:" + "进入删除方法");
        return HttpClientUtils.doDelete(deleteUrlUserdto);
    }

    @ApiOperation(value = "提供delete方法测试-带参数", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto2", method = RequestMethod.DELETE)
    public Object deleteUserDTO2(@ApiParam(value = "传参值：name", required = true) @RequestParam String name) throws Exception {
        System.out.println("remote:" + "进入删除方法");
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return HttpClientUtils.doDelete(deleteUrlUserdto2, params);
    }

}
