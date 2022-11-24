package com.zja.controller;

import com.zja.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value = "rest/template")
@Api(tags = {"RestTemplateController"}, description = "RestTemplate 调用远程REST接口")
public class RestTemplateController {

    @Autowired
    RestTemplate restTemplate;

    //get

    @GetMapping(value = "/get")
    @ApiOperation(value = "get-无参数", notes = "返回字符串")
    public Object get() {
        return restTemplate.getForObject("/get", String.class);
    }

    @GetMapping(value = "/get/{path}")
    @ApiOperation(value = "get-路径参数")
    public Object getPath1(@PathVariable("path") String path) {
        return restTemplate.getForObject("/get/" + path, String.class);
    }

    @GetMapping(value = "/get/param")
    @ApiOperation(value = "get-拼接参数")
    public Object getPath2(@RequestParam("path") String path) {
        return restTemplate.getForObject("/get/param?path=" + path, String.class);
    }

    @GetMapping(value = "/get/object/v1")
    @ApiOperation(value = "get-无参数", notes = "返回对象")
    public Object getObject() {
        return restTemplate.getForObject("/get/object/v1", UserDTO.class);
    }

    @GetMapping(value = "/get/object/list/v1")
    @ApiOperation(value = "get-无参数", notes = "返回List")
    public Object getObjectList() {
        return restTemplate.getForObject("/get/object/list/v1", List.class);
    }

    //post

    @PostMapping(value = "/post")
    @ApiOperation(value = "post-无参数", notes = "返回字符串")
    public Object post() {
        return restTemplate.postForObject("/post", HttpEntity.EMPTY, String.class);
    }

    @PostMapping(value = "/post/param")
    @ApiOperation(value = "post-多个参数", notes = "返回字符串")
    public Object postObject(@ApiParam(value = "传参值：name") @RequestParam String name,
                             @ApiParam(value = "传参值：age") @RequestParam String age) {
        return restTemplate.postForObject("/post/param?name=" + name + "&age=" + age, HttpEntity.EMPTY, String.class);
    }

    @PostMapping(value = "/post/object/v1")
    @ApiOperation(value = "post-无参数", notes = "返回对象")
    public Object postObject() {
        return restTemplate.postForObject("/post/object/v1", HttpEntity.EMPTY, UserDTO.class);
    }

    @PostMapping(value = "/post/object/v2")
    @ApiOperation(value = "post-对象参数", notes = "返回对象")
    public Object postObject(@RequestBody UserDTO userDto) {

        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //请求参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", userDto.getName());
        map.put("id", userDto.getId());
        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForObject("/post/object/v2", request, UserDTO.class);*/

        return restTemplate.postForObject("/post/object/v2", userDto, UserDTO.class);
    }

    //put

    @PutMapping(value = "/put")
    @ApiOperation(value = "put-无参数", notes = "返回字符串")
    public Object put() {
        //此 put方法，没有返回值，一般不用此方式
        //restTemplate.put("/put", HttpEntity.EMPTY);

        ResponseEntity<String> exchange = restTemplate.exchange("/put", HttpMethod.PUT, HttpEntity.EMPTY, String.class);
        return exchange.getBody();
    }

    @PutMapping(value = "/put/path/{path}")
    @ApiOperation(value = "put-路径参数", notes = "返回字符串")
    public Object putPath(@PathVariable("path") String path) {
        ResponseEntity<String> exchange = restTemplate.exchange("/put/path/" + path, HttpMethod.PUT, HttpEntity.EMPTY, String.class);
        return exchange.getBody();
    }

    @PutMapping(value = "/put/param")
    @ApiOperation(value = "put-拼接参数", notes = "返回字符串")
    public Object putParam(@RequestParam String param) {
        ResponseEntity<String> exchange = restTemplate.exchange("/put/param?param=" + param, HttpMethod.PUT, HttpEntity.EMPTY, String.class);
        return exchange.getBody();
    }

    @PutMapping(value = "/put/object/v1")
    @ApiOperation(value = "put-无参数", notes = "返回对象")
    public Object putObject() {
        ResponseEntity<UserDTO> exchange = restTemplate.exchange("/put/object/v1", HttpMethod.PUT, HttpEntity.EMPTY, UserDTO.class);
        return exchange.getBody();
    }

    @PutMapping(value = "/put/object/v2")
    @ApiOperation(value = "put-对象参数", notes = "返回对象")
    public UserDTO putObject(@RequestBody UserDTO userDto) {
        ResponseEntity<UserDTO> exchange = restTemplate.exchange("/put/object/v2", HttpMethod.PUT, new HttpEntity(userDto), UserDTO.class);
        return exchange.getBody();
    }

    //delete

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "delete-无参数", notes = "返回字符串")
    public Object delete() {
        //不推荐此方式，没有返回值
        //restTemplate.delete("/delete");

        ResponseEntity<String> exchange = restTemplate.exchange("/delete", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        return exchange.getBody();
    }

    @DeleteMapping(value = "/delete/path/{path}")
    @ApiOperation(value = "delete-路径参数")
    public Object deletePath1(@PathVariable("path") String path) {
        ResponseEntity<String> exchange = restTemplate.exchange("/delete/path/" + path, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        return exchange.getBody();
    }

    @DeleteMapping(value = "/delete/param")
    @ApiOperation(value = "delete-拼接参数")
    public Object deletePath2(@RequestParam("param") String param) {
        ResponseEntity<String> exchange = restTemplate.exchange("/delete/param?param=" + param, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        return exchange.getBody();
    }

    @DeleteMapping(value = "/delete/object/v1")
    @ApiOperation(value = "delete-无参数", notes = "返回对象")
    public Object deleteObject() {
        ResponseEntity<UserDTO> exchange = restTemplate.exchange("/delete/object/v1", HttpMethod.DELETE, HttpEntity.EMPTY, UserDTO.class);
        return exchange.getBody();
    }

}
