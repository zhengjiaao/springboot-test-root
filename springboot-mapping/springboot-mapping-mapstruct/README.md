# springboot-mapping-mapstruct

- [mapstruct 官网](https://mapstruct.org/)
- [mapstruct GitHub](https://github.com/mapstruct/mapstruct)
- [mapstruct 示例参考](https://zhuanlan.zhihu.com/p/368731266)
- [mapstruct 示例参考2](https://developer.aliyun.com/article/994957)


## 依赖引入

```xml
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>

    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                    <annotationProcessorPaths>
                        <!-- MapStruct 注解处理器 -->
                        <path><!--编译时生成 *MapperImpl类-->
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${org.mapstruct.version}</version>
                        </path>
                        <!-- Lombok 注解处理器 -->
                        <path><!--添加lombok否则编译时会找不到符号-->
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                        <path><!--添加lombok-mapstruct-binding否则编译后属性不会进行赋值set or get-->
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version>0.2.0</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

## 简单使用示例

先编写映射接口

```java
//@Mapper
@Mapper(componentModel = "spring") // 使用 spring bean 管理
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "name", source = "username")
    UserDTO toDto(User user);
}
```

测试示例
```java
@RunWith(SpringRunner.class) //注入 spring bean 对象
@SpringBootTest
public class MapstructApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("李四");
        user.setPassword("123456");
        user.setSex("男");

        UserDTO userDTO1 = UserMapper.INSTANCE.toDto(user);
        System.out.println("userDTO1=" + userDTO1);

        //需要注释掉上面，多次拷贝会抛出空指针异常
        UserDTO userDTO2 = userMapper.toDto(user);
        System.out.println("userDTO2=" + userDTO2);
    }
}
```

> 输出结果:     
> userDTO1=UserDTO(name=李四, password=123456, sex=男)     
> userDTO2=UserDTO(name=李四, password=123456, sex=男) 

