# springboot-mapping-dozer

- [dozer 官网](https://dozer.sourceforge.net/)


## 依赖引入

```xml
        <!--dozer 实体类转换工具-->
        <dependency>
            <groupId>net.sf.dozer</groupId>
            <artifactId>dozer-spring</artifactId>
            <version>5.5.1</version>
        </dependency>
        <dependency>
            <groupId>net.sf.dozer</groupId>
            <artifactId>dozer</artifactId>
            <version>5.5.1</version>
        </dependency>
```

## 配置类

```java
/**
 * 对象之间属性值拷贝
 */
@Configuration
public class DozerConfig {
    @Bean(name = "org.dozer.Mapper")
    public Mapper mapper(){
        //如果要读取多个或者一个xml文件，则将写进下面地址,如果不需要xml配置则不用。
        // mapper.setMappingFiles(Arrays.asList("dozer/dozer-mapping.xml","dozer/xxx.xml"));
        return new DozerBeanMapper();
    }
}
```


## 简单示例

把`UserDTO`属性值赋值给`UserVO`的属性 

```java
@Data
public class UserDTO {
    private String username;
    private String password;
    private Long ctime;
}

@Data
public class UserVO {
    @Mapping("username")
    private String name;
    private String password;
    private Long ctime;
}
```

使用 dozer Mapper     

```java
@RestController
@RequestMapping("/dozer")
public class DozerController {

    @Autowired
    private Mapper mapper;

    /**
     * http://127.0.0.1:8080/dozer/query
     */
    @GetMapping("/query")
    public UserVO query() {
        UserDTO userDTO = new UserDTO("lisi", "123", 666L);
        //属性拷贝
        UserVO userVO = mapper.map(userDTO, UserVO.class);
        return userVO;
    }
}
```

