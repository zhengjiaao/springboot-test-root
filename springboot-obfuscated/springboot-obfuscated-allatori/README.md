# springboot-obfuscated-allatori

- [Allatori](https://allatori.com/)
- [Allatori-Doc](https://allatori.com/doc.html#keep-names-method)

**说明**

Allatori是由Smardec开发的商业Java混淆和优化工具。它旨在通过对应用程序的字节码应用各种混淆技术，保护Java应用程序免受逆向工程和篡改的影响。Allatori混淆了Java代码中的类、方法、字段和其他元素的名称，使得他人更难理解和修改原始源代码。

除了混淆之外，Allatori还提供各种优化技术，以提高Java应用程序的性能。它可以删除不必要的代码，内联方法，并应用其他优化措施，使代码更加高效。

Allatori通常在商业软件开发中被用于保护知识产权和防止对专有代码的未经授权访问。通过混淆Java字节码，攻击者更难以逆向工程或修改应用程序。

备注：

* Allatori提供了免费的教育版，非商用的版本。
* Allatori支持混淆jar、war。

## 1.通过引入依赖方式混淆代码

pom.xml 配置

```xml

<plugins>
    <!--打包插件:boot-maven-->
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
            <!--解决 jar 命令启动报错问题：no main manifest attribute, in /app.jar-->
            <mainClass>com.zja.obfuscated.allatori.AllatoriApplication</mainClass>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

    <!--一定要放到打包插件后面-->
    <!--将 Allatori 配置文件复制到“target”目录。将筛选目标文件（配置文件中使用的 Maven 属性将被解析）-->
    <plugin>
        <!--一定要放到打包插件后面-->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-resources-plugin</artifactId>
        <version>2.6</version>
        <executions>
            <execution>
                <id>copy-and-filter-allatori-config</id>
                <phase>package</phase>
                <goals>
                    <goal>copy-resources</goal>
                </goals>
                <configuration>
                    <outputDirectory>${basedir}/target</outputDirectory>
                    <resources>
                        <resource>
                            <directory>${basedir}/allatori</directory>
                            <includes>
                                <include>allatori.xml</include>
                            </includes>
                            <filtering>true</filtering>
                        </resource>
                    </resources>
                </configuration>
            </execution>
        </executions>
    </plugin>
    <!-- Running Allatori -->
    <plugin>
        <!--一定要放到打包插件后面-->
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>1.2.1</version>
        <executions>
            <execution>
                <id>run-allatori</id>
                <phase>package</phase>
                <goals>
                    <goal>exec</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <executable>java</executable>
            <arguments>
                <argument>-Xms128m</argument>
                <argument>-Xmx512m</argument>
                <argument>-jar</argument>

                <!-- Copy allatori.jar to 'allatori' directory to use the commented line -->
                <!--<argument>${basedir}/../../../lib/allatori.jar</argument>-->
                <argument>${basedir}/allatori/allatori.jar</argument>

                <argument>${basedir}/target/allatori.xml</argument>
            </arguments>
        </configuration>
    </plugin>
</plugins>
```

allatori.xml 配置文件

参考官网配置：[Allatori-Doc](https://allatori.com/doc.html#keep-names-method)

```xml

<config>
    <!-- Maven properties could be used in Allatori configuration file. -->
    <input>
        <!--这里没有读到maven属性-->
        <!--<jar in="${project.build.finalName}.jar" out="${project.build.finalName}-obfuscated.jar"/>-->
        <jar in="springboot-obfuscated-allatori-2.0-SNAPSHOT.jar" out="springboot-obfuscated-allatori-obfuscated.jar"/>
    </input>

    <!--忽略的类-->
    <ignore-classes>
        <class template="class *springframework*"/>
        <class template="class *springframework.boot*"/>
        <class template="class *javax*"/>
        <class template="class *jni*"/>
        <class template="class *alibaba*"/>
        <class template="class *persistence*"/>
    </ignore-classes>

    <!-- 不替换类名、方法名、属性名 -->
    <keep-names>
        <class access="protected+">
            <field access="protected+"/>
            <method access="protected+"/>
        </class>

        <!-- 不混淆的包或类：避免实体类和表结构映射不上-->
        <class template="class com.zja.obfuscated.allatori.model.*">
            <field access="protected+"/>
            <field access="private+"/>
            <method access="protected+"/>
            <!--参数不混淆：避免映射问题-->
            <method template="public+ *(*)" parameters="keep"/>
        </class>
        <!-- 不混淆的包或类：避免controller层接收不到映射的参数-->
        <class template="class com.zja.obfuscated.allatori.controller.*">
            <field access="protected+"/>
            <field access="private+"/>
            <method access="protected+"/>
            <method access="private+"/>
            <!--参数不混淆：避免controller层接收不到映射的参数-->
            <method template="public+ *(*)" parameters="keep"/>
        </class>

        <!-- Matches serialization members -->
        <class template="class * instanceof java.io.Serializable">
            <field template="static final long serialVersionUID"/>
            <method template="void writeObject(java.io.ObjectOutputStream)"/>
            <method template="void readObject(java.io.ObjectInputStream)"/>
            <method template="java.lang.Object writeReplace()"/>
            <method template="java.lang.Object readResolve()"/>
        </class>

        <!-- Matches applets -->
        <class template="class * instanceof java.applet.Applet"/>
        <!-- Matches servlets -->
        <class template="class * instanceof javax.servlet.Servlet"/>
    </keep-names>

    <property name="log-file" value="log.xml"/>

    <!-- 设置默认包：位于默认包中的类将按照Allatori配置文件中指定的规则进行处理，可以是混淆、保留或其他操作。-->
    <property name="default-package" value="class com.zja.obfuscated.allatori"/>
    <!-- 设置方法重命名规则-->
    <!--<property name="methods-naming" value="keywords"/>-->
    <!--<property name="fields-naming" value="keywords"/>-->

</config>

```

## 2. 通过工具方式混淆代码

可以写个demo，把需要混淆jar或war放到target下，然后修改配置allatori.xml：

```xml

<config>
    <input>
        <!-- in 指向的是 maven打包好的工具
         out 是allatori 混淆之后的jar包名称， allatori 可以混淆jar 也可以混淆war经过
     -->
        <jar in="app.jar" out="app-obf.jar"/>
    </input>
</config>
```

根据上述，可以把项目打好的jar，放进来，进行混淆。这样项目里面就不需要进行引入allatori依赖了,可以避免很多问题。

