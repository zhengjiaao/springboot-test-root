# springboot-qrcode-qrgen

> QRGen Java 二维码封装库

- [QRGen 官网](https://github.com/kenglxn/QRGen)

## 依赖引入

```xml
        <!--qrgen 二维码依赖-->
        <dependency>
            <groupId>net.glxn.qrgen</groupId>
            <artifactId>javase</artifactId>
            <version>2.0</version>
        </dependency>
```

## 简单示例

```java

    /**
     * 二维码生成 测试
     */
    @Test
    public void testQrcode() {
        ByteArrayOutputStream bout =
                QRCode.from("https://www.baidu.com")
                        .withCharset("utf-8")
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            OutputStream out = new FileOutputStream("D:\\qrCode-2.png");
            bout.writeTo(out);
            out.flush();
            out.close();
            System.out.println("***********二维码生成成功！**********");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

```
