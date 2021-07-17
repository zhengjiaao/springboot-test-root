package com.zja.controller;

import com.zja.dto.UserDTO;
import com.zja.util.RestTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**HttpClient方式请求http接口
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 15:49
 */
@Api(tags = {"RestTemplateController"}, description = "Template调用远程服务器接口")
@RestController(value = "templateController")
@RequestMapping(value = "rest/template")
public class RestTemplateController {

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

    //文件上传
    @Value("${remote.url.file_upload}")
    private String file_upload_url;

    @Value("${remote.url.file_upload1}")
    private String file_upload1_url;

    @Value("${remote.url.file_upload2}")
    private String file_upload2_url;

    private static final RestTemplate restTemplate = new RestTemplate();


    @ApiOperation(value = "提供get方法测试-不传参数", notes = "不传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto", method = RequestMethod.GET)
    public Object getUserDTO() {
        System.out.println("remote:" + "进入调用方法");
        return RestTemplateUtils.get(getUrlUserdto, Object.class);
    }

    @ApiOperation(value = "提供get方法测试-传参数", notes = "传参数", httpMethod = "GET")
    @RequestMapping(value = "/get/userdto2", method = RequestMethod.GET)
    public Object getUserDTO(@ApiParam(value = "传参值：默认", defaultValue = "调用成功", required = true) @RequestParam String param) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("param", "param");
        ResponseEntity<Object> objectResponseEntity = RestTemplateUtils.get(getUrlUserdto2, Object.class, map);
        System.out.println("remote:param==" + objectResponseEntity);
        return objectResponseEntity;
    }

    @ApiOperation(value = "提供listobject方法测试", httpMethod = "GET")
    @RequestMapping(value = "/get/userdtolsit", method = RequestMethod.GET)
    public Object getUserDTOList() {
        return RestTemplateUtils.get(getUrlUserdtoList, Object.class);
    }

    @ApiOperation(value = "提供post方法测试-传json参数", notes = "传json参数", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto", method = RequestMethod.POST)
    public Object postUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        ResponseEntity<Object> objectResponseEntity = RestTemplateUtils.post(postUrlUserdto, userDto, Object.class);
        System.out.println("remote:objectResponseEntity" + objectResponseEntity);
        return objectResponseEntity;
    }

    @ApiOperation(value = "提供post方法测试-不传参", notes = "不传参", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto2", method = RequestMethod.POST)
    public Object postUserDTO2() {
        //final ResponseEntity<Object> post = restTemplate.postForEntity(postUrlUserdto2, null, Object.class);
        ResponseEntity<String> objectResponseEntity = RestTemplateUtils.post(postUrlUserdto2, String.class);
        System.out.println("remote:objectResponseEntity==" + objectResponseEntity);
        return objectResponseEntity;
    }

    @ApiOperation(value = "提供post方法测试-传多个参数", notes = "传多个参数", httpMethod = "POST")
    @RequestMapping(value = "/post/userdto3", method = RequestMethod.POST)
    public Object postUserDTO3(@ApiParam(value = "传参值：name", required = true) @RequestParam String name,
                               @ApiParam(value = "传参值：age", required = true) @RequestParam String age) {
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("name", name);
        requestEntity.add("age", age);
        ResponseEntity<String> objectResponseEntity = RestTemplateUtils.post(postUrlUserdto3, requestEntity, String.class);
        System.out.println("remote:objectResponseEntity==" + objectResponseEntity);
        return objectResponseEntity;
    }

    @ApiOperation(value = "提供put方法测试-传多个参数", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto2", method = RequestMethod.PUT)
    public Object putUserDTO2(@ApiParam(value = "传参值：name", required = true) @RequestParam String name,
                              @ApiParam(value = "传参值：age", required = true) @RequestParam String age) {
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("name", name);
        requestEntity.add("age", age);
        ResponseEntity<String> objectResponseEntity = RestTemplateUtils.put(putUrlUserdto2, requestEntity, String.class);
        System.out.println("remote:objectResponseEntity==" + objectResponseEntity);
        return objectResponseEntity;
    }

    @ApiOperation(value = "提供put方法测试-传json数据--未测试成功", httpMethod = "PUT")
    @RequestMapping(value = "/put/userdto", method = RequestMethod.PUT)
    public Object putUserDTO(@ApiParam(value = "传参值：userDto", required = true) @RequestBody UserDTO userDto) {
        System.out.println("进入put请求方法！");
        ResponseEntity<Object> objectResponseEntity = RestTemplateUtils.put(postUrlUserdto, userDto, Object.class);
        System.out.println("remote:objectResponseEntity" + objectResponseEntity);
        return objectResponseEntity;
    }

    @ApiOperation(value = "提供delete方法测试-不带参数", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto", method = RequestMethod.DELETE)
    public Object deleteUserDTO() {
        System.out.println("remote:" + "进入删除方法");
        return RestTemplateUtils.delete(deleteUrlUserdto, Object.class);
    }

    @ApiOperation(value = "提供delete方法测试-带参数", httpMethod = "DELETE")
    @RequestMapping(value = "/delete/userdto2", method = RequestMethod.DELETE)
    public Object deleteUserDTO2(@ApiParam(value = "传参值：name", required = true) @RequestParam String name) {
        System.out.println("remote:" + "进入删除方法");
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("name", name);
        ResponseEntity<String> delete = RestTemplateUtils.delete(deleteUrlUserdto2, requestEntity, String.class);
        System.out.println("remote:" + delete);
        return delete;
    }


    // #################### 上传文件测试
    @ApiOperation("上传单文件-远程文件")
    @PostMapping("/file/upload/v1")
    public Object uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("remote-上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
//        Object uploadedFile = RestTemplateUtils.uploadFile(file_upload_url, file.getOriginalFilename(), file.getInputStream());
        Object uploadedFile = RestTemplateUtils.uploadFile(file_upload_url, file);
        System.out.println(uploadedFile);
        return "成功！";
    }

    @ApiOperation("上传单文件-远程文件")
    @PostMapping("/file/upload/v1/{fileNewName}")
    public Object uploadFile(@RequestParam("file") MultipartFile file, String fileNewName) throws IOException {
        System.out.println("remote-上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        Map params = new HashMap();
        params.put("fileNewName", fileNewName);

        Object uploadedFile = RestTemplateUtils.uploadFile(file_upload1_url, fileNewName, file.getInputStream(), params);
        System.out.println(uploadedFile);
        return "成功！";
    }


    @ApiOperation("上传单文件-本地文件")
    @PostMapping("/file/upload/v2")
    public Object uploadFile() throws IOException {
        Object localFile = RestTemplateUtils.uploadFile(file_upload_url, "D:\\Temp\\zip\\质检包.zip");
        System.out.println(localFile);
        return "成功！";
    }

    @ApiOperation("上传多文件")
    @PostMapping("/file/upload/v3")
    public Object uploadFile3(@RequestParam("file") MultipartFile[] files) {
        if (files.length <= 0) {
            return "请选择要上传的文件！";
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("remote-上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        }
        return "成功！";
    }

}
