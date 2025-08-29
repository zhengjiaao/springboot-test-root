# springboot-process-flowable

**flowable 是开源的流程引擎、有丰富的案例管理以及规则引擎，完全支持 BPMN、CMMN 和 DMN 开放标准**

- [Flowable 官网](https://www.flowable.com/open-source)
- [Flowable github](https://github.com/flowable)
- [Flowable-UI](https://github.com/flowable/flowable-engine/releases/tag/flowable-6.8.1)

## 创建一个流程定义文件的方式有哪些？

- **推荐** 通过`Flowable-UI`创建一个流程定义文件, 如：`process/process.bpmn20.xml`
- **不推荐** 通过手动创建流程定义文件, 如：`process/process.bpmn20.xml`


## 依赖引入

```xml
        <dependency>
            <groupId>org.flowable</groupId>
            <artifactId>flowable-spring-boot-starter</artifactId>
            <version>6.8.1</version>
        </dependency>
```
