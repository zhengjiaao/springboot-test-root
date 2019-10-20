package com.dist.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dist.dto.BigFileSplit;
import com.dist.dto.UserDTO;
import com.dist.util.HttpAsyncClient;
import com.dist.util.HttpClientResult;
import com.dist.util.HttpClientUtils;
import com.dist.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient方式请求http接口
 *
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 15:49
 */
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

    @Autowired
    private HttpAsyncClient httpAsyncClient;


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

        String doGet = HttpUtil.doGet(getUrlUserdto2, map);
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

    @ApiOperation(value = "按同步下载网络文件到本地", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/Synchronize/downloadNetworkFile", method = RequestMethod.GET)
    public void downloadNetworkFileBySynchronize(@ApiParam(value = "文件路径以'/'开头", required = true) @RequestParam("pathName") String pathName) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("pathName", pathName);
        int blockSize= 20*1024*1024;
        map.put("blockSize",Long.toString(blockSize));
        HttpClientResult httpClientResult = HttpClientUtils.doGet(getFileInfo, map);

        //BigFileSplit bigFileSplit = JSON.parseObject(httpClientResult.getContent(), BigFileSplit.class);

        JSONObject jsonObject = JSONObject.parseObject(httpClientResult.getContent());
        BigFileSplit bigFileSplit = JSON.parseObject(jsonObject.get("data").toString(), BigFileSplit.class);

        System.out.println("bigFileSplit : " + bigFileSplit);

        List<String> destFileNames = bigFileSplit.getDestFileNames();
        for (String filePathName : destFileNames) {
            String remotePath = downloadSplitFile + "?splitFileName=" + filePathName;
            System.out.println("remotePath" + remotePath);
            Map<String, String> map1 = new HashMap<>();
            map1.put("splitFileName", filePathName);
            String fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1);
            String filePath = "D:\\FileTest\\文件下载位置"+ File.separator+fileName;
           //同步下载-远程http接口获取文件 byte[]
            HttpClientResult httpClientResult2 = HttpClientUtils.doGetIsByte(downloadSplitFile, map1);
            byte[] bytes = httpClientResult2.getBytes();
            System.out.println("bytes.length : "+bytes.length);
            //建议-可以异步保存本地
            //fileUtil.downLoadFile(bytes,filePath);
        }
        //Thread.sleep(50000);
        System.out.println("请求结束！ ");
    }

    @ApiOperation(value = "按异步下载网络文件到本地", notes = "适合下载小型文件 大概100M 以内，如果文件大于100M，建议分片下载", httpMethod = "GET")
    @RequestMapping(value = "v1/Async/downloadNetworkFile", method = RequestMethod.GET)
    public void downloadNetworkFileByAsync(@ApiParam(value = "文件路径以'/'开头", required = true) @RequestParam("pathName") String pathName) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("pathName", pathName);
        int blockSize= 20*1024*1024;
        map.put("blockSize",Long.toString(blockSize));
        HttpClientResult httpClientResult = HttpClientUtils.doGet(getFileInfo, map);

        //BigFileSplit bigFileSplit = JSON.parseObject(httpClientResult.getContent(), BigFileSplit.class);

        JSONObject jsonObject = JSONObject.parseObject(httpClientResult.getContent());
        BigFileSplit bigFileSplit = JSON.parseObject(jsonObject.get("data").toString(), BigFileSplit.class);

        System.out.println("bigFileSplit : " + bigFileSplit);
        List<String> destFileNames = bigFileSplit.getDestFileNames();
        for (String filePathName : destFileNames) {
            String remotePath = downloadSplitFile + "?splitFileName=" + filePathName;
            System.out.println("remotePath ： " + remotePath);
            Map<String, String> map1 = new HashMap<>();
            map1.put("splitFileName", filePathName);
            String fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1);
            String filePath = "D:\\FileTest\\文件下载位置"+ File.separator+fileName;

            //异步的 httpAsyncClient 获取到文件流 - 建议再异步保存
            httpAsyncClient.httpAsyncClient(remotePath);

        }
        //测试 -让主线程不关闭
        //Thread.sleep(90000);
        System.out.println("请求结束！ ");
       /* while (true){
        }*/
    }


}
