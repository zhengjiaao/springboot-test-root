## ocr-tess4J

**说明**

Tesseract OCR：Tesseract是一个开源的OCR引擎，由Google开发。它支持多种语言，并且可用于将图像中的文本转换为可编辑文本。
它提供命令行界面，也有许多第三方库和界面可以与其集成。

## Tesseract OCR 常用功能

Tesseract OCR 是一个开源的光学字符识别引擎，可以用于识别图像中的文本内容。

1. 文本识别（OCR）：Tesseract 最主要的功能是将图像中的文本转换为可编辑的文本格式。你可以将图像文件或图像数据传递给
   Tesseract，并使用其识别功能来提取和获取图像中的文本内容。
2. 多语言支持：Tesseract 支持多种语言的文本识别，包括英语、中文、日语、法语等。你可以通过配置 Tesseract
   来选择适当的语言模型，以便识别特定语言的文本。
3. 图像预处理：为了提高识别的准确性，Tesseract 提供了一些图像预处理功能，如图像二值化、降噪、去除倾斜等。这些预处理步骤可以帮助优化图像质量，提高识别结果的准确性。
4. 区域识别：Tesseract 允许你指定感兴趣区域（ROI）进行识别。这对于处理包含多个文本区域的图像或仅关注特定区域的文本识别非常有用。
5. 文字方向检测：Tesseract 可以自动检测图像中的文字方向，以确保正确识别和排列文本。这对于处理旋转或倾斜的图像非常有用。
6. 输出格式：Tesseract 提供多种输出格式选项，包括文本文件、HOCR（HTML）格式、ALTO（XML）格式等。你可以根据需要选择适当的输出格式。
7. 自定义字典：Tesseract 允许你使用自定义字典来提高特定术语或单词的识别准确性。通过添加自定义字典，你可以指导 Tesseract
   在识别过程中优先考虑特定的单词或术语。

## 先安装 tesseract

1. win tesseract.exe 安装示例：

```text
下载：
https://github.com/UB-Mannheim/tesseract/wiki
```

> 说明：tesseract-ocr目前训练的数据是放在tessdata文件夹下，后缀为traineddata文件，目前支持100多种语言(
> 安装时勾选，不然默认仅支持英文)。今天安装的为第三方支持的安装包。

2. 添加环境变量：

```text
# 全局环境变量
TESSERACT_OCR=E:\App\tesseract-ocr

# 训练集位置
TESSDATA_PREFIX=E:\App\tesseract-ocr\tessdata
```

验证安装成功

```shell
tesseract --version
```

下载训练集(可选的)：https://github.com/tesseract-ocr/tessdata

3. 命令行进行文字识别：

```shell
#格式：
tesseract imagename outputbase [-l lang] [--oem ocrenginemode] [--psm pagesegmode] [configfiles...]

#示例：识别结果out.txt,输出在当前目录下
tesseract.exe ocr-test.png out -l chi_sim
```

* imagename ：图片的位置
* outputbase：输出的文件名字，不加后缀保存为txt格式
* -l lang：识别的语言类型，不加默认为英文，中文使用-l chi_sim,使用多种语言识别如 -l eng+deu
* –psm pagesegmode:参数
* –oem:使用识别的引擎，新版都是用 --oem 1（LSTM模式）
* configfiles: 可以执行输入的格式如 pdf, hocr ,tsv

```text
-psm 后携带的参数:
0 =方向和脚本检测(OSD)。
1 =自动页面分割与OSD。
2 =自动页面分割，但没有OSD，或OCR
3 =全自动页面分割，无OSD。(默认)
4 =假设一列文本大小可变。
5 =假设有一个垂直对齐的文本块。
6 =假设有一个统一的文本块。
7 =将图像视为单个文本行。
8 =把图像看成一个单词。
9 =把图像看成一个圆圈里的单个单词。
10 =将图像视为单个字符。
-l lang和/或-psm pagesegmode必须出现在任何configfile之前。
```

## 依赖

Tesseract OCR for Java：Tesseract OCR是一个开源的OCR引擎，你可以使用它的Java版本来进行图像文本识别。
Tesseract OCR for Java提供了Java API和相关的文档，使你可以轻松地将其集成到Java项目中。

```xml

<dependency>
    <groupId>net.sourceforge.tess4j</groupId>
    <artifactId>tess4j</artifactId>
    <version>5.8.0</version>
</dependency>
```

## 实例

实例1：文字识别

```java
public class ImageTest {
    @Test
    public void png_test() throws IOException {
        Tesseract tesseract = new Tesseract();
        //设置指定训练集位置，推荐配置为全局环境变量：TESSDATA_PREFIX=E:\App\tesseract-ocr\tessdata
        tesseract.setDatapath("E:\\App\\tesseract-ocr\\tessdata");
        //设置识别语言(默认是英文识别)
        tesseract.setLanguage("chi_sim"); //chi_sim 是中文

        //需要识别的图
        File imageFile = new File("D:\\temp\\ocr\\input.png");

        String result = null;
        try {
            result = tesseract.doOCR(imageFile);    //文字识别
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
        //输出结果：要想在其他目录使用tesseract.exe工具，那么奖该工具父目录添加到环境变量中
    }
}
```

输出结果：

```text
要想在其他目录使用tesseract.exe工具，那么奖该工具父目录添加到环境变量中
```

## 其他配置项

### 识别多语言支持

多语言支持，需要下载所需语言的数据文件。

* 首先，下载地址：https://github.com/tesseract-ocr/tessdata
* 将下载的语言数据文件放置在Tesseract OCR的数据文件路径中，通过setDatapath()方法指定该路径。
* 使用setLanguage()方法设置要识别的语言。你可以指定一个或多个语言，用逗号分隔，例如："eng"表示英语，"chi_sim"表示简体中文。
* 在进行图像文本识别时，Tesseract OCR将会根据指定的语言进行识别。

```text
//设置识别语言(默认是英文识别):指定一个或多个语言，用逗号分隔，例如："eng"表示英语，"chi_sim"表示简体中文。
tesseract.setLanguage("eng,chi_sim"); //chi_sim 是中文
```

### 添加自定义词库

* Tesseract OCR允许你添加自定义的词库，以提高特定词汇的识别准确性。
* 创建一个纯文本文件，每行包含一个词汇。
* 使用setTessVariable()方法，指定user_words参数和自定义词库文件的路径。
* 在进行图像文本识别时，Tesseract OCR将会使用自定义词库进行识别。

```text
// 添加自定义词库
String customDictionaryPath = "C:\\tesseract-ocr\\custom_dictionary.txt";
tesseract.setTessVariable("user_words", customDictionaryPath);
```

自定义词库文件 custom_dictionary.txt 的内容：

```text
OpenAI
ChatGPT
Tesseract OCR
文本识别
图像处理
```

## 问题汇总

### Tesseract OCR 能处理PDF吗？

Tesseract OCR 本身并不能直接处理 PDF 文件。然而，你可以使用其他工具和库结合 Tesseract 来处理 PDF 文件。

方案：

一种常见的方法是将 PDF 文件转换为图像，然后使用 Tesseract 对图像进行 OCR。你可以使用图像处理库（如PIL或OpenCV）将 PDF
页面转换为图像文件，然后将这些图像文件传递给 Tesseract 进行文本识别。这种方法需要将 PDF 文件分割为单个页面，并逐页进行处理。

另一种方法是使用第三方库或工具，如pdf2image或PyPDF2（对于Python环境）来提取 PDF 中的文本，并将提取的文本传递给 Tesseract
进行进一步处理。这些工具可以将 PDF 文本内容提取为字符串，然后你可以将这些字符串作为输入传递给 Tesseract 进行识别。

需要注意的是，PDF 文件中的文本提取可能会面临一些挑战，例如复杂的布局、图像内嵌的文本等。因此，在进行 PDF 文本提取和 OCR
处理时，你可能需要根据具体的 PDF 结构和内容进行适当的预处理和后处理。
