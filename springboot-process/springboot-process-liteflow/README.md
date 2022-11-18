# springboot-process-liteflow

**liteflow 简单的流程框架**

- [liteflow 官网](https://liteflow.yomahub.com/)
- [liteflow | gitee](https://gitee.com/dromara/liteFlow)


## 依赖引入

```xml
        <dependency>
            <groupId>com.yomahub</groupId>
            <artifactId>liteflow-spring-boot-starter</artifactId>
            <version>2.9.3</version>
        </dependency>
```

## 简单示例

```java
@Component("a")
public class ACmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        //do your business
        System.out.println("a");
    }
}

@Component("b")
public class BCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        System.out.println("b");
    }
}

@Component("c")
public class CCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        //do your business
        System.out.println("c");
    }
}
```

流程配置

> 新建 `flow.el.xml` 流程配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<flow>
    <chain name="chain1">
        THEN(a, b, c);
    </chain>
</flow>
```

单元测试类

```java
@SpringBootTest
public class RunFlowExecutorTests {
    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testConfig() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", null);
        System.out.println(response.isSuccess());
    }
}

```
