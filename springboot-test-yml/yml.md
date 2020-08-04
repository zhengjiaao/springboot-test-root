# springboot-test-yml

[toc]

## 一、实开发中如何通过yml或properties文件向项目中传值

### 1.为什么要通过yml或properties文件向项目中传值???

当你所在的项目组有幸需要开发一套服务,比如说权限服务,而公司里其他项目组之后都会集成你们组的服务,这时候很可能就会有某个或某几个组对我们服务里的某些功能有个性化的需求.在这种情况下**一个比较好的方式就是,我们在我们的服务里对某些可能会产生个性化需求的功能提供一个默认实现,其他组如果想有自己的个性化设计,可以通过在配置文件里修改某个配置(也就是通过yml或properties文件向项目中传值)**,以达到不走我们提供的默认实现,而走自己的个性化设计的目的.

### 2.如何做?

按照1中所说,我们一般会在我们的服务里提供一个默认实现,这样就需要我们不在yml或properties文件里配置值时,项目里有一个与之对应的默认值,而能在项目里提供默认值的肯定要么是枚举要么是对象;

同时为了统一管理,实际开发中往往会搞一个类(就像一个大盒子),将所有可能会在yml或properties文件里配置的属性给装起来;

当然为了让代码更加具有可阅读性,往往我们会把具有某种联系的属性进一步封装到一个个的小类中,然后再将这一个个封装了某种联系的小类,装到那个我所说的"大盒子"类中.

### 3.具体代码

#### 3.1 与浏览器相关的属性

```java
package com.nrsc.properties.properties1;
import lombok.Data;
/**
 * 与浏览器相关的属性全部封装到该类里
 */
@Data
public class BrowserProperties {
    private String loginPage = "browser";
}
```

#### 3.2 与app相关的属性

```java
package com.nrsc.properties.properties1;
import lombok.Data;
/**
 * 与app相关的所有属性全部封装到该类里
 */
@Data
public class AppProperties {
    private String loginPage = "app";
}
```

#### 3.3 "大盒子"类

```java
package com.nrsc.properties.properties1;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
//将该类注入到spring容器
@Component
//指定配置文件中属性的前缀为nrsc.security是可以传递到该类
@ConfigurationProperties(prefix = "nrsc.security")
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();
    private AppProperties app = new AppProperties();
}
```

#### 3.4 "大盒子"类还有另一种配置方式

这里不将其注入到spring容器

```java
package com.nrsc.properties.properties1;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
//指定配置文件中属性的前缀为nrsc.security是可以传递到该类
@ConfigurationProperties(prefix = "nrsc.security")
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();
    private AppProperties app = new AppProperties();
}
```

用另一个配置类将SecurityProperties注入到spring容器中

```java
package com.nrsc.properties.properties2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
/**
 * 将SecurityProperties注入到spring容器,底层使用了@Import注解的方式
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {
}
```

#### 3.5 在properties里这样配置,就可以将值传到springboot项目中了

```properties
nrsc.security.app.loginPage=android
1
```

#### 3.6 在springboot项目中接收配置值的方式

```java
package com.nrsc.properties.controller;

import com.nrsc.properties.properties1.SecurityProperties;
import com.nrsc.properties.properties2.Unions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetPropertiesController {

    //从spring容器中拿出"大盒子"类
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private Unions unions;

    @GetMapping("/getProperties")
    public String getProperties() {

        //获得传到"大盒子"类中的参数值
        System.out.println("方式1--properties里没配置值,默认值browser--------->" +
                securityProperties.getBrowser().getLoginPage());
        System.out.println("方式1--properties里配置了值Android,默认值app------>" +
                securityProperties.getApp().getLoginPage());

        System.out.println("方式2---properties里没配置值,默认值yoyo------>"+unions.getPartI().getName());
        System.out.println("方式2---properties里配置了值10,默认值20------>"+unions.getPartII().getAge());

        return "ok";
    }
}
```

### 4.有一个需要注意的点

在写"大盒子"类时IDEA可能会有如下警告,经过测试不管他也可以
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190620224301715.png)
如果你看不顺眼,点击一下蓝色的Open Documentation,会跳转到如下页面,然后你在pom.xml里加上下面红框中的依赖就不会报警告了.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190620224534174.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L25yc2MyNzI0MjAxOTk=,size_16,color_FFFFFF,t_70)

## 二、yml 实体类映射map、list等

MyAttributes 实体类

```java
@Data
@Component
@ConfigurationProperties(prefix = "my-attributes")
public class MyAttributes {

    private String value;
    private String[] valueArray;
    private List<String> valueList;
    private HashMap<String, String> valueMap;
    private List<Map<String, String>> valueMapList;
}
```

*.yml

```yml
# yml 映射单个属性、数组、list、map、list<map>等
my-attributes:
  # 单个值
  value: 345
  # 数组
  valueArray: 1,2,3,4,5,6,7,8,9
  # list
  valueList:
    - 13579
    - 246810
  # map
  valueMap:
    name: 张三
    age: 20
    sex: female
  # list<map>
  valueMapList:
    - name: 李四
      age: 21
    - name: caven
      age: 31
```

YmlTest.java

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YmlApplication.class)
public class YmlTest {

    @Autowired
    private MyAttributes myAttributes;

    @Test
    public void propsTest() {
        System.out.println("myAttributes: " + myAttributes.toString());
    }
    //打印结果 myAttributes: MyAttributes(value=345, valueArray=[1, 2, 3, 4, 5, 6, 7, 8, 9], valueList=[13579, 246810], valueMap={name=张三, age=20, sex=female}, valueMapList=[{name=李四, age=21}, {name=caven, age=31}])

}
```

