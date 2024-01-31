# springboot-obfuscated-proguard

**说明**

代码混淆：proguard-maven-plugin 插件，可以对代码进行混淆，按需要进行配置，例如：支持所有包，混淆特定包，特定类，特定方法等功能，可定制化较多。

proguard混淆原理：proguard代码混淆其实是将一个原始的jar，生成一个混淆后的jar。也就是混淆后的代码覆盖原始jar里面的代码。

## proguard 简介

1. 官网:https://www.guardsquare.com/proguard
2. 文档: https://www.guardsquare.com/manual/configuration/usage
3. keep规则调试: https://playground.proguard.com/

ProGuard 是一个免费的 Java 字节码收缩器、优化器、混淆器和预验证器：

1. 它检测并删除未使用的类、字段、方法和属性。
2. 它优化字节码并删除未使用的指令。
3. 它使用简短的无意义名称重命名其余的类、字段和方法。

由此产生的应用程序和库更小、更快。功能: 1.收缩代码 2.代码优化 3.混滑 4.校验

## proguard-maven-plugin 简介

proguard 与 proguard-maven-plugin的关系：proguard官方未提供maven的插件形式,但推荐了两款开源实现,proguard-maven-plugin是其中之一。

1. 开源地址: https://github.com/wvengen/proguard-maven-plugin
2. 文档地: https://wvengen.github.io/proguard-maven-plugin/

可用option都可在proguard官方配置中能够找到,见proguard文档链接

## keep 规则区分

| 保留内容              | 防止被移除和重命名             | 防止被重命名(未使用的会移除)             |
|-------------------|-----------------------|-----------------------------|
| 类和类成员(属性或方法)      | -keep                 | -keepnames                  |
| 仅类成员              | -keepclassmembers     | -keepclasseswithmembers     |
| 若类含有某成员,则保留类和其类成员 | -keepclassmembernames | -keepclasseswithmembernames |

## 依赖引入

> 通用的配置：部分配置项，需要进行改动

```xml

<build>
    <plugins>
        <!--代码混淆插件：proguard-maven-plugin-->
        <plugin>
            <!--一定要放到打包插件之前-->
            <groupId>com.github.wvengen</groupId>
            <artifactId>proguard-maven-plugin</artifactId>
            <version>2.6.0</version>
            <executions>
                <execution>
                    <!--混淆时刻，这里是打包的时候混淆-->
                    <phase>package</phase>
                    <goals>
                        <!--使用插件的什么功能，当然是混淆-->
                        <goal>proguard</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <!-- 就是输入Jar的名称,代码混淆其实是将一个原始的jar，生成一个混淆后的jar -->
                <injar>${project.build.finalName}.jar</injar>
                <!-- 输出jar名称，输入输出jar同名的时候就是覆盖-->
                <outjar>${project.build.finalName}-proguard.jar</outjar>
                <!-- 这是输出上述jar或war路径配置，但是要注意这个路径必须要包括<injar>标签填写的jar -->
                <outputDirectory>${project.basedir}/target</outputDirectory>

                <!-- 混淆：是否混淆 默认是true -->
                <obfuscate>true</obfuscate>
                <!-- 混淆：指定混淆配置文件，通常叫做proguard.cfg,也可写在options中 -->
                <proguardInclude>${project.basedir}/proguard.cfg</proguardInclude>

                <!--添加jvm依赖：没在依赖列表中的依赖，这里你可以按你的需要修改，这里测试只需要一个JRE的Runtime包就行了-->
                <libs>
                    <lib>${java.home}/lib/rt.jar</lib>
                    <lib>${java.home}/lib/jsse.jar</lib>
                </libs>
                <!--配置混淆的一些细节选项，比如哪些类不需要混淆，哪些需要混淆-->
                <!--options：允许您直接在插件配置中指定ProGuard的选项-->
                <options>
                    <!--必改：不混淆的包，或者实体类、入参的属性名、响应的属性名-->
                    <!--实体类、入参的属性名、响应的属性名不混淆-->
                    <option>-keep class com.zja.obfuscated.proguard.model.** {*;}</option>
                    <option>-keep class com.zja.obfuscated.proguard.dao.** {*;}</option>
                    <option>-keep class com.zja.obfuscated.proguard.controller.** {*;}</option>
                    <!--<option>-keep class com.zja.obfuscated.proguard.service.** {*;}</option>-->
                </options>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## proguard.cfg 配置文件

> 通用的配置

```cfg
#JDK目标版本1.8
-target 1.8

#不预校验
#-dontpreverify

#不做压缩
-dontshrink

#不做优化
-dontoptimize

#混淆时不会采用大小写混淆类名
-dontusemixedcaseclassnames

#指定不忽略非公共的类
-dontskipnonpubliclibraryclasses

#指定不忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers

#保持目录结构：保留目录entry，避免com.example.Myclass.class.getResource("")为nu11.
-keepdirectories

#不混淆所有包名
-keeppackagenames **

#保留所有参数的名字：避免controller/mapper映射时找不到对应的参数
-keepparameternames

# 保留'main'方法不被混淆
# Keep - Applications. Keep all application classes, along with their 'main' methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

#保持源码名与行号（异常时有明确的栈信息），注解（默认会过滤掉所有注解，会影响框架的注解）
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,Synthetic,EnclosingMethod

# 保留所有的'注解'不被混淆
-keepattributes *Annotation*
-keepattributes Signature
# 保留特定的注解
#-keepattributes com.example.MyAnnotation

# 同时保留'枚举'不被混淆
# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 同时保留'若含有指定注解的成员不重命名'不被混淆
-keepclassmembers class * {
    @org.springframework.beans.factory.annotation.Autowired *;
    @org.springframework.beans.factory.annotation.Value *;
    @javax.annotation.Resource *;
    @org.springframework.context.annotation.Bean *;
}

# 同时保留[Servlet类]不被混淆
-keep class * extends javax.servlet.Servlet

# 同时保留[所有的数据库驱动实现类]不被混淆
# Also keep - Database drivers. Keep all implementations of java.sql.Driver.
-keep class * extends java.sql.Driver

# Keep - Native method names. Keep all native class/method names.
-keepclasseswithmembers,includedescriptorclasses,allowshrinking class * {
    native <methods>;
}

# 解决 Spring Boot 遇到一些问题：

# 解决 Spring AOP 的问题
-keep class org.springframework.aop.** { *; }
-keep interface org.springframework.aop.** { *; }

# 解决 Spring Data JPA 的问题
-keep interface org.springframework.data.repository.Repository
-keep interface org.springframework.data.repository.CrudRepository
-keep interface org.springframework.data.jpa.repository.JpaRepository
-keep interface org.springframework.data.jpa.repository.JpaSpecificationExecutor

# 解决 Spring MVC 的问题
-keep class org.springframework.web.** { *; }
-keep interface org.springframework.web.** { *; }

# 解决 Thymeleaf 的问题
-keep class org.thymeleaf.spring5.** { *; }
```

保留的几个配置说明：

1. -keep 选项用于保留指定类或类成员的名称，使其不被混淆。
2. -keepnames 选项用于保留类和类成员的名称，使其不被混淆，但不保留其结构信息。
3. -keepclassmembers 选项用于保留类成员的名称，包括字段、方法和构造函数。
4. -keepclasseswithmembers 选项用于保留包含指定成员的类，并混淆其他类成员。
5. -keepattributes 选项用于保留指定的类、类成员或类成员的属性，使其不被删除。

## 遇到的一些问题

### 问题1：混淆后的jar启动时，遇到找不到主清单属性

> 这个问题遇到的很难受，原因是outjar设置的输出jar名称要与injar一样，才能把混淆后的代码覆盖injar内部的原始代码。

```xml

<configuration>
    <proguardVersion>6.2.2</proguardVersion>
    <!-- proguard原理：proguard代码混淆其实是将一个原始的jar，生成一个混淆后的jar -->
    <!-- 输入jar名称：在outputDirectory目录下的原始jar-->
    <injar>${project.build.finalName}.jar</injar>
    <!-- 输出jar名称：在outputDirectory目录下的输出jar，与injar同名的时候就是覆盖，混淆后的代码覆盖原始jar里面的代码-->
    <outjar>${project.build.finalName}.jar</outjar>
    <!-- 但是要注意这个路径必须要包括<injar>标签填写的jar -->
    <outputDirectory>${project.basedir}/target</outputDirectory>
</configuration>
```

### 问题2：混淆后的jar，不包含lib下的所有依赖包

> 同上。

### bean 名称冲突情况

目录，了解到存在几种解决方案，参考下面实例：

方式一：修改bean注入策略，改为通过`路径+bean名称`方式注入spring bean

网上好多推荐这种实现方式的，但我觉得应该有更简单实现，不错，我给找到了。

```java

@SpringBootApplication
public class ProguardApplication extends SpringBootServletInitializer {


/*    public static void main(String[] args) {
        SpringApplication.run(ProguardApplication.class, args);
    }*/

    //更改注册bean的策略：按[类路径+beanname]注册策略
    public static class CustomGenerator implements BeanNameGenerator {
        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            return definition.getBeanClassName();
        }
    }

    public static void main(String[] args) {
        SpringApplicationBuilder sab = new SpringApplicationBuilder(ProguardApplication.class)
                .beanNameGenerator(new CustomGenerator());
        // 这里如果想打印你加载的Spring的bean,可以这样做：
        ApplicationContext ac = sab.run(args);
        Arrays.stream(ac.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
```

注意：其他地方getBean的用法：[com.zja.service+serviceName],因此，要注意，有类似获取bean的地方，需要进行改造。

```java
//这里的 packagePath = com.zja.service
CallBackGuiService callBackGuiService=(CallBackGuiService)SpringUtil.getBean(packagePath+serviceName);
        callBackResult=callBackGuiService.excute(convertMap);
```

方式二：定义过滤器，指定混淆的包路径及排除对底层依赖的包或类进行混淆

```xml

<configuration>
    <!--必改项：过滤器-->
    <!--输入过滤器：ProGuard 将仅对以该路径开始的类和包进行处理,可以只对特定的包或类进行混淆，而不是整个应用程序-->
    <!--匹配特定包下的所有类-->
    <inFilter>com/zja/obfuscated/proguard/**</inFilter>
    <!--排除特定包下的所有类-->
    <!--<inFilter>!com/zja/obfuscated/proguard/**</inFilter>-->
</configuration>
```

方式三：推荐，通过配置进行排除指定的包或类

```cfg

# 解决 Bean 名称冲突的问题，指定某个类不进行混淆
-keep class com.example.MyClass

# 解决 Spring Boot 遇到一些问题：

# 保留 Spring Boot 相关的类和资源
-keep class org.springframework.boot.** { *; }
-keep interface org.springframework.boot.** { *; }

# 解决 Spring MVC 的问题
-keep class org.springframework.web.** { *; }
-keep interface org.springframework.web.** { *; }

# 解决 Spring Data JPA 的问题
-keep class org.springframework.data.** { *; }
-keep interface org.springframework.data.** { *; }
```

方式四：自定义bean名称字典

在`proguard.cfg`中配置 类的名称字典：

```cfg
#类名混淆字典：仅适用于混淆类名
-classobfuscationdictionary proguard-class.txt
```

proguard-class.txt 字典配置两种方式：

自定义字典 value格式：(按每个包进行排序设置beanName，本质上没啥改变，还是会遇到冲突)

```txt
asv
afgqr
bfq
gar
```

上述示例中，com.example.MyClass 类将被随机混淆为 afgqr，com.example.OtherClass 类将被随机混淆为 gar。

自定义对应字典 key value格式：

```txt
com.example.MyClass a
com.example.OtherClass b
```

上述示例中，com.example.MyClass 类将被混淆为 a，com.example.OtherClass 类将被混淆为 b。
