# ocr-ocrmypdf

- [OCRmyPDF官网](https://ocrmypdf.readthedocs.io/en/latest/introduction.html)
- [OCRmyPDF官网-安装](https://ocrmypdf.readthedocs.io/en/latest/installation.html)
- [OCRmyPDF官网-docker安装版](https://ocrmypdf.readthedocs.io/en/latest/docker.html)
- [OCRmyPDF官网-用法](https://ocrmypdf.readthedocs.io/en/latest/cookbook.html)

**说明**

OCRmyPDF是一个开源的命令行工具，用于将扫描的PDF文档转换为可搜索的PDF文档。它使用OCR技术自动识别图像中的文字，并将其添加到PDF文档中，从而使文档中的文本可搜索和可选。

## OCRmyPDF的常用特性和用法

**常用特性：**

1. 文字识别：OCRmyPDF使用Tesseract OCR引擎进行文字识别，支持多种语言。它可以处理扫描的PDF文档、图像文件（如JPEG、PNG）以及包含图像的PDF文件。
2. 自动裁剪和修复：OCRmyPDF具有自动裁剪和修复的功能，可以检测和修复扫描中的边缘、黑边和旋转问题，提高识别准确性。
3. 文字层优化：OCRmyPDF会将识别的文字添加到PDF文档中的文字层，以便进行搜索、复制和粘贴。它可以保留原始PDF文档的布局和格式，并添加一个隐藏的文字层。
4. 可选的PDF优化：OCRmyPDF提供一些可选的PDF优化选项，如减少文件大小、压缩图像、优化图像质量等。
5. 命令行接口：OCRmyPDF是一个命令行工具，可以在Windows、Mac和Linux操作系统上使用。它提供了各种命令参数，用于控制OCR过程和输出结果。

**基本用法：**

参考官网：https://ocrmypdf.readthedocs.io/en/latest/cookbook.html

```shell
ocrmypdf input.pdf output.pdf
```

上述命令将对input.pdf文件执行OCR，并将结果保存为output.pdf文件。OCRmyPDF将自动识别文本并将其添加到输出PDF文档中。

您可以使用不同的命令参数来自定义OCRmyPDF的行为，例如指定OCR语言、启用自动裁剪、选择PDF优化选项等。使用以下命令获取有关OCRmyPDF的完整帮助文档和命令参数信息：

```shell
ocrmypdf --help
```

## OCRmyPDF 部署安装

OCRmyPDF是一个命令行工具，可以在Windows、Mac和Linux操作系统上使用，也可以在Docker中应用。

> 请注意，OCRmyPDF依赖于`Python`、`Tesseract OCR`和`Ghostscript`组件。在使用OCRmyPDF之前，请确保已正确安装和配置了这些依赖项。

### 安装 Python

安装Python：

1. 访问Python官方网站：https://www.python.org
2. 点击 "Downloads"（下载）选项卡，并选择适用于您的操作系统的Python安装程序。通常，您可以选择最新的稳定版本。
3. 下载并运行安装程序。
4. 在安装向导中，选择 "Add Python to PATH"（将Python添加到系统路径）选项，以确保Python可从命令行访问。
5. 完成安装过程。

验证Python安装：

1. 打开命令提示符（Windows）或终端（Mac/Linux）。
2. 输入以下命令来检查Python是否成功安装以及版本号：

```shell
python --version
```

### 安装 Tesseract OCR 引擎

安装Tesseract OCR引擎：

1. 访问Tesseract OCR官方GitHub页面：https://github.com/tesseract-ocr/tesseract
2. 在页面上找到适用于Windows的最新版本的Tesseract OCR安装程序，并下载。
3. 运行安装程序，并按照安装向导进行安装。
4. 在安装过程中，选择将Tesseract添加到系统路径中，以便在命令行中访问它。
5. 完成安装过程。

验证Tesseract OCR安装：

1. 打开命令提示符（Windows）或终端（Mac/Linux）。
2. 输入以下命令来检查Tesseract OCR是否成功安装以及版本号：

```shell
tesseract --version
```

### 安装 Ghostscript

安装Ghostscript：

1. 访问Ghostscript官方网站：https://www.ghostscript.com
2. 在网站上找到适用于您的操作系统的Ghostscript安装程序，并下载。
3. 运行安装程序，并按照安装向导进行安装。
4. 在安装过程中，选择将Ghostscript添加到系统路径中，以便在命令行中访问它。
5. 完成安装过程。

### 安装 QPDF(可选的)

1. 访问QPDF官方网站：https://qpdf.sourceforge.io
2. 在网站上找到适用于您的操作系统的QPDF安装程序，并下载。
3. 运行安装程序，并按照安装向导进行安装。
4. 在安装过程中，选择将QPDF添加到系统路径中，以便在命令行中访问它。
5. 完成安装过程。

### 安装 Leptonica(可选的)

1. 访问Leptonica官方GitHub页面：https://github.com/DanBloomberg/leptonica
2. 根据您的操作系统，按照页面上的说明下载并安装Leptonica源代码或二进制文件。
3. 遵循Leptonica的安装说明进行安装。
4. 完成安装过程。

### 安装 OCRmyPDF

安装OCRmyPDF：

```shell
pip install ocrmypdf

```

验证

```shell
ocrmypdf --version
```

帮助命令

```shell
ocrmypdf --help
```

### OCRmyPDF 命令行示例

OCRmyPDF用于处理图片：

将图片转换为PDF：您可以使用OCRmyPDF将图片转换为PDF，并在转换过程中执行OCR操作。。运行以下命令：

```shell
ocrmypdf input_image.jpg output_pdf.pdf
```

执行OCR而不转换为PDF：如果您只想对图片执行OCR，而不需要将其转换为PDF，可以使用--image-only选项。运行以下命令：

```shell
ocrmypdf --image-only input_image.jpg output_image.jpg
```

OCRmyPDF用于处理PDF文件：

执行OCR操作：您可以使用OCRmyPDF对现有的PDF文档执行OCR操作。运行以下命令：

```shell
ocrmypdf input_pdf.pdf output_pdf.pdf
```

调整OCR参数：OCRmyPDF支持一些参数来调整OCR的行为。例如，您可以使用--deskew选项进行自动矫正倾斜页面，或使用--language选项指定要识别的语言。运行以下命令：

```shell
ocrmypdf --deskew --language eng input_pdf.pdf output_pdf.pdf
```

## 实例

### Java 接入 OCRmyPDF 实例

```java
public class PdfOCRmyPDFExample {

    @Test
    public void ocr_pdf_to_pdf() throws IOException, InterruptedException {
        // 输入PDF文件路径
        String inputPdfPath = "D:\\temp\\ocr\\input.pdf";
        // 输出PDF文件路径
        String outputPdfPath = "D:\\temp\\ocr\\output.pdf";

        // OCRmyPDF命令
        String command = "ocrmypdf -l eng+chi_sim " + inputPdfPath + " " + outputPdfPath;

        // 执行OCRmyPDF命令
        ocrCommand(command);
    }

    /**
     * 执行 OCRmyPDF 命令
     */
    public static void ocrCommand(String command) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec(command);

        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            //命令执行失败
            String errorMessage = "OCRmyPDF 命令：" + command + "\n" +
                    "OCRmyPDF 执行命令错误信息：" +
                    getErrorMessage(process.getErrorStream());

            throw new RuntimeException(errorMessage);
        }
    }

    private static StringBuilder getErrorMessage(InputStream errorStream) throws IOException {
        // 获取错误输出流
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));

        String line;
        StringBuilder errorMessage = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            errorMessage.append(line).append("\n");
        }

        return errorMessage;
    }
}
```
