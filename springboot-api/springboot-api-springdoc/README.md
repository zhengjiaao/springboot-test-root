# springboot-api-springdoc

- [官网 https://springdoc.org/](https://springdoc.org/)
- [github https://github.com/springdoc](https://github.com/springdoc)
- [参考：1](https://blog.csdn.net/weixin_43103956/article/details/136155025)

## 快速开始

### 依赖

* springboot2.x 对应 springdoc-openapi-ui 1.8.0
* springboot3.x 对应 springdoc-openapi-starter-webmvc-ui 2.6.0

2.x

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.8.0</version>
</dependency>
```

3.x

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### 配置

```yaml
springdoc:
  swagger-ui:
    enabled: true   # 默认是 true，启用 swagger-ui
    path: /swagger-ui.html  # swagger-ui custom path. 访问：http://server:port/context-path/swagger-ui.html
    display-request-duration: true # 默认false，显示请求持续时间
    default-models-expand-depth: 0 # 默认1，默认情况下，所有模型将展开，展开深度为1，0表示不展开
    doc-expansion: 0 # 默认1，默认情况下，所有API将折叠，折叠深度为1，0表示不折叠

  api-docs:
    enabled: true   # 默认是 true，启用 springdoc-openapi端点
    path: /api-docs # /api-docs endpoint custom path. 访问：http://server:port/context-path/v3/api-docs

  # 配置分组
  #  group-configs:
  #    - group: 'default'
  #      paths-to-match: '/**'
  #      packages-to-scan: com.zja.controller
  #    - group: 'group-xml'
  #      paths-to-match: '/xml/**'
  #      packages-to-scan: com.zja.controller
  # 选择要包含在文档中的 Rest 控制器
  #  packagesToScan: com.package1, com.package2  # 对于要包含的包列表
  #  pathsToMatch: /v1, /api/balance/** # 对于要包含的路径列表
  # 排除在文档中的 Rest 控制器,也可以使用 @Hidden 限制申城
  #  packages-to-exclude: com.package1, com.package2 # 对于要排除的包列表
  #  paths-to-exclude: /v1, /api/balance/** # 对于要排除的路径列表
  # 以下是默认配置(可选的)
  default-flat-param-object: false # 默认false 扁平化参数对象，true就不需要添加额外的@ParameterObject和@Parameter注解
  auto-tag-classes: true # 自动 tag
  show-actuator: false # 显示 actuator
  model-and-view-allowed: false # 默认 false，允许 RestControllers 与 ModelAndView 返回出现在 OpenAPI 描述中
  writer-with-default-pretty-printer: false # 默认false，规范的漂亮打印输出
  # ... ... 还有很多其他配置，具体看官方文档
```

### 代码实例

Controller 层

```java
// @Api("提供远程-Rest测试接口")
@Tag(name = "提供远程-Rest测试接口", description = "测试示例")
@RestController
@RequestMapping
public class WebRestController {

    @GetMapping(value = "/get")
    // @ApiOperation(value = "get-无参数", notes = "返回字符串")
    @Operation(summary = "get-无参数", description = "返回字符串")
    public String get() {

        return "get 请求成功！";
    }

    @GetMapping(value = "/get/param/v2")
    @Operation(summary = "get-拼接参数", description = "对象属性参数，相当于 @RequestParam(required = false) 属性名")
    // public Object getPath3(UserDTO userDTO) {
    public Object getPath3(@ParameterObject UserDTO userDTO) {
        return "get 请求成功: " + userDTO;
    }

    // @PostMapping(value = "/post/upload/v1")
    @PostMapping(value = "/post/upload/v1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @Operation(summary = "post-上传单文件", description = "返回 true")
    @Operation(summary = "post-上传单文件", description = "返回 true")
    // public Object postFile(@ApiParam("选择文件") @RequestPart(value = "file") MultipartFile file) {
    public Object postFile(@Parameter(description = "选择文件") @RequestPart("file") MultipartFile file) {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        return true;
    }
}
```

传输对象

```java
// @ApiModel(value = "用户信息")
@Schema(description = "用户信息")
@Data
public class UserDTO implements Serializable {

    // @ApiModelProperty(value = "用户id")
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String id;
    // @ApiModelProperty(value = "用户名")
    @Schema(description = "用户名")
    private String name;
    // @ApiModelProperty(value = "时间")
    @Schema(description = "时间")
    private Date date;
}
```


  