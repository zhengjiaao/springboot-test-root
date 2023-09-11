# springboot-remote-webclient



## 引入依赖

```xml
        <!--WebClient-->
        <!--注：starter-web不注释掉的话，默认还是mvc，而不是starter-webflux-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
```

## 简单示例

```java
    //get

    @GetMapping(value = "/get")
    @ApiOperation(value = "get-无参数", notes = "返回字符串")
    public String get() {
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
    public String getPath1(@PathVariable("path") String path) {
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
```