# ocr-aspose

- [aspose-ocr](https://releases.aspose.com/ocr/)
- [aspose-ocr-java-developer](https://docs.aspose.com/ocr/java/developer-reference/)

**说明**

Aspose.OCR 是商业的，收费的。
Aspose.OCR for Java 是一种光学字符 API，允许开发人员将 OCR 功能添加到其 Java 应用程序中，而无需任何其他工具或 API。
Aspose.OCR for Java 允许从具有不同字体和样式的图像中提取文本 - 节省从头开始开发 OCR 解决方案所需的时间和精力。

## 通过Java将图像转换为文本

* 从图像中读取字符。
* OCR 支持 PDF、JPG、PNG、GIF、BMP、WBMP 和 TIFF 图像文件格式。
* 支持英语、法语、西班牙语和葡萄牙语及中文等(26 种语言)。
* 阅读流行字体，包括 Arial、Times New Roman、Courier New、Verdana、Tahoma 和 Calibri。
* 支持常规、粗体和斜体字体样式。
* 扫描整个图像或图像的任何部分。
* 扫描旋转图像。
* 可以在图像识别之前应用不同的噪声去除滤波器。
* 从扫描图像中提取光学标记元素。
* 支持各种光学标记元素，例如复选框、选择框、网格、条形码和 OCR。

## 识别结果

识别结果以最流行的文档和数据交换格式返回：

结果存储为：TXT、HTML、RTF、DOCX、XLSX、PDF、EPUB、JSON、XML

## 特定的应用场景

Aspose.OCR for Java 提供了微调的 OCR 方法，用于从某些类型的图像中提取文本：

* 从身份证中提取文本：自动读取扫描或拍照的身份证中的文本。
* 从护照中提取文本：将护照的扫描件或照片数字化。
* 从车辆牌照中提取文本：无需手动重新输入即可读取车辆牌照。
* 从发票中提取文本：将扫描的发票数字化，无需手动重新打字。
* 从收据中提取文本：将扫描的收据数字化，无需手动重新打字。
* 从街道照片中提取文本：从街道照片、价格标签、菜单、目录和其他具有稀疏文本和嘈杂/彩色背景的图像中提取文本。

## 引入依赖

```xml

<dependency>
    <groupId>com.aspose</groupId>
    <artifactId>aspose-ocr</artifactId>
    <version>23.10.0</version>
</dependency>
```

## 实例

### 提取图像中文字

```java
public class OCRTest {
    @Test
    public void ocr_png_test() throws IOException {
        AsposeOCR api = new AsposeOCR();
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setLinesFiltration(false);
        recognitionSettings.setDetectAreas(true);
        recognitionSettings.setAllowedCharacters(CharactersAllowedType.ALL);
        recognitionSettings.setLanguage(Language.Chi);
        RecognitionResult result = api.RecognizePage("D:\\temp\\ocr\\input.png", recognitionSettings);
        System.out.println("Recognition result:\n" + result.recognitionText + "\n\n");
    }
}
```

### 从图像上的特定区域提取文本

```java
public class OCRTest {
    @Test
    public void ocr_png_test() throws IOException {
        // create API instance
        AsposeOCR api = new AsposeOCR();
        // prepare rectangles with texts.
        ArrayList rectArray = new ArrayList();
        rectArray.add(new Rectangle(138, 352, 2033, 537));
        rectArray.add(new Rectangle(147, 890, 2033, 1157));
        String result = api.RecognizePage("template.png", rectArray);

        System.out.println("Result with rect: " + result);
    }
}
```

###   