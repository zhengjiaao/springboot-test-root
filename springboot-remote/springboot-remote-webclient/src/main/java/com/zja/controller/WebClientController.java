/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-07 13:38
 * @Since:
 */
package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author: zhengja
 * @since: 2023/09/07 13:38
 */
@Validated
@RestController
@RequestMapping("/rest/")
@Api(tags = {"WebClient 远程调用"})
public class WebClientController {

    //get

    @GetMapping(value = "/get")
    @ApiOperation(value = "get-无参数", notes = "返回字符串")
    public Object get() {
        WebClient client = WebClient.create("http://127.0.0.1:19000");

        Mono<String> result = client.get()
                .uri("/get", 256)
                .acceptCharset(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);

        //这是异步的，不会马上打印结果
        result.subscribe(System.err::println);

        //这是同步的，会一直等待，直到返回结果
        return result.block();
    }

    @GetMapping(value = "/get/{path}")
    @ApiOperation(value = "get-路径参数")
    public Object getPath1(@PathVariable("path") String path) {
        /*Mono<String> resp = WebClient.create()
                .get()
                //多个参数也可以直接放到map中,参数名与placeholder对应上即可
                .uri("http://127.0.0.1:19000/get/{path}","test") //使用占位符
                .retrieve()
                .bodyToMono(String.class);*/

        Mono<String> resp = WebClient.create("http://127.0.0.1:19000")
                .get()
                //多个参数也可以直接放到map中,参数名与placeholder对应上即可
                .uri("/get/{path}","test") //使用占位符
                .retrieve()
                .bodyToMono(String.class);

        return resp.block();
    }

}