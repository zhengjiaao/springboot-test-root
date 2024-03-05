# springboot-file-preview-libreoffice

- [libreoffice 官网](https://www.libreoffice.org/)
- [libreoffice github](https://github.com/LibreOffice/core)

**说明**

libreoffice 转换为pdf时，效率不高，但是显示效果很好。

## 安装 LibreOffice

- [libreoffice 官网下载](https://www.libreoffice.org/download/download-libreoffice/)

一般选择默认安装就行。

## 依赖引入

```xml

<dependencies>
    <!--jodconverter-->
    <dependency>
        <groupId>org.jodconverter</groupId>
        <artifactId>jodconverter-local</artifactId>
        <version>4.4.7</version>
    </dependency>
    <!--LibreOffice-->
    <dependency>
        <groupId>org.libreoffice</groupId>
        <artifactId>libreoffice</artifactId>
        <version>7.6.4</version>
    </dependency>
</dependencies>
```

## 代码实例

```java
public class LibreOfficeJodconverterTest {

    @Test
    public void test() {
        File sourceFile = new File("D:\\temp\\word\\test.docx");
        File targetFile = new File("D:\\temp\\word\\test.docx.pdf");
        convertOffice2PDFSyncIsSuccess(sourceFile, targetFile);
    }

    // 伪代码
    public static boolean convertOffice2PDFSyncIsSuccess(File sourceFile, File targetFile) {
        try {
            LocalOfficeManager.Builder builder = LocalOfficeManager.builder();
            builder.officeHome("C:\\Program Files\\LibreOffice");
            builder.portNumbers(8100);
            long taskExecutionTimeout = 5 * 1000 * 60;
            builder.taskExecutionTimeout(taskExecutionTimeout); // minute
            long taskQueueTimeout = 1000 * 60 * 60;
            builder.taskQueueTimeout(taskQueueTimeout); // hour

            OfficeManager build = builder.build();
            build.start();
            LocalConverter make = LocalConverter.make(build);  
            make.convert(sourceFile).to(targetFile).execute(); // 转换为 pdf
            build.stop();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
```
