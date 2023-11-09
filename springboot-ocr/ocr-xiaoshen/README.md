# ocr-xiaoshen

**说明**

## 环境准备

- Tesseract 安装(必须)
- OCRmyPDF 安装(可选的)

## 依赖

```xml

<dependencies>
    <!--Apache Tika-->
    <dependency>
        <groupId>org.apache.tika</groupId>
        <artifactId>tika-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.tika</groupId>
        <artifactId>tika-parsers-standard-package</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.tika</groupId>
        <artifactId>tika-langdetect-tika</artifactId>
    </dependency>

    <!--tesseract-->
    <dependency>
        <groupId>net.sourceforge.tess4j</groupId>
        <artifactId>tess4j</artifactId>
        <version>5.8.0</version>
    </dependency>
</dependencies>
```



