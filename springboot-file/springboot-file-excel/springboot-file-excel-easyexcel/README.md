# springboot-file-excel-easyexcel

- [easyexcel | 官网](https://easyexcel.opensource.alibaba.com/docs/current/)
- [easyexcel | 官网必读](https://easyexcel.opensource.alibaba.com/qa/)
- [easyexcel | github](https://github.com/alibaba/easyexcel)
- [easyexcel | github-demo](https://github.com/alibaba/easyexcel/tree/master/easyexcel-test/src/test/java/com/alibaba/easyexcel/test/demo)

**读写excel文件**

## 依赖引用

```xml

<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>easyexcel</artifactId>
  <!--jdk8 easyexcel-3.3.4 内含 poi-4.1.2 -->
  <!-- <version>3.3.4</version> -->
  <!--jdk8 easyexcel-4.0.3 内含 poi-5.2.5 -->
  <version>4.0.3</version>
</dependency>
```

### 关于版本选择

提示:

```text
如果项目中没有使用过poi,且jdk版本在8-21之间，直接使用最新版本，别犹豫。

如果项目中已经使用过poi或者jdk版本小于8的，请参看下面表格做出选择。
```

| 版本                  | poi依赖版本 (支持范围)        | jdk版本支持范围    | 备注                                          |
|---------------------|-----------------------|--------------|---------------------------------------------|
| 4.0.0+              | 5.2.5 (5.0.0 - 5.2.5) | jdk8 - jdk21 | 推荐使用，会更新的版本                                 |
| 3.1.0 - 3.3.4       | 4.1.2 (4.1.2 - 5.2.5) | jdk8 - jdk21 | 不推荐项目新引入此版本，除非超级严重bug,否则不再更新                |
| 3.0.0-beta1 - 3.0.5 | 4.1.2 (4.1.2 - 5.2.5) | jdk8 - jdk11 | 不推荐项目新引入此版本，除非超级严重bug,否则不再更新                |
| 2.0.0-beta1-2.2.11  | 3.17 (3.17 - 4.1.2)   | jdk6 - jdk11 | 不推荐项目新引入此版本，除非是jdk6否则不推荐使用，除非超级严重bug,否则不再更新 |
| 1+版本                | 3.17 (3.17 - 4.1.2)   | jdk6 - jdk11 | 不推荐项目新引入此版本，超级严重bug,也不再更新                   |

注：3+版本的的easyexcel，使用poi 5+版本时，需要自己引入poi 5+版本的包，且手动排除：poi-ooxml-schemas，例如：

```xml

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.3.4</version>
    <exclusions>
        <exclusion>
            <artifactId>poi-ooxml-schemas</artifactId>
            <groupId>org.apache.poi</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

### 出现 `NoSuchMethodException`， `ClassNotFoundException`,`NoClassDefFoundError`

[等兼容性问题](https://easyexcel.opensource.alibaba.com/qa/#出现-nosuchmethodexception-classnotfoundexception-noclassdeffounderror-等兼容性问题)

极大概率是jar冲突，可能是以下原因。

- 直接`clean`项目后查看是否可行
- `poi`
  版本冲突（项目中已经含有poi的其他版本了），参照:[关于版本选择](https://easyexcel.opensource.alibaba.com/qa/#关于版本选择)
  来指定版本。
- `ehcache` 版本冲突，打开https://mvnrepository.com/artifact/com.alibaba/easyexcel ，打开对应的版本，看下所需要的`ehcache`
  版本

### 出现
`java.lang.NoSuchMethodError: 'void org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.putArchiveEntry(org.apache.commons.compress.archivers.zip.ZipArchiveEntry)'`

[排查思路](https://easyexcel.opensource.alibaba.com/qa/#出现-javalangnosuchmethoderror-void-orgapachecommonscompressarchiverszipziparchiveoutputstreamputarchiveentryorgapachecommonscompressarchiverszipziparchiveentry)

EasyExcel 依赖于比较新的 `commons-io` 版本，这个原因是因为你的项目中引入了比较旧的 `commons-io` 版本 自己额外引入这个包或者
在 `parent` 的 `dependencyManagement` 里面加入

```xml

<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <!-- 这个版本可以继续往上升 ，但是不要低于这个 -->
    <version>2.15.0</version>
</dependency>
```

