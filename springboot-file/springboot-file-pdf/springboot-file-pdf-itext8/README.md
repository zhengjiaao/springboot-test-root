# springboot-file-pdf-itext8

- [itext 官网](https://itextpdf.com/)
- [itext 版本发行列表](https://kb.itextpdf.com/itext/releases)
- [itext8 安装指南](https://kb.itextpdf.com/itext/installation-guidelines)
- [itext7-8 iText：Java 快速入门教程](https://kb.itextpdf.com/itext/itext-jump-start-tutorial-for-java)
- [itext8 Java处理PDF文档【上】（ 全新 iText 8.0 基础入门 、元素）](https://blog.csdn.net/xiaofeng_yang/article/details/139902089?spm=1001.2014.3001.5502)
- [itext8 Java处理PDF文档【下】（ 全新 iText 8.0 画布、条形码、渲染器 ）](https://blog.csdn.net/xiaofeng_yang/article/details/139902405?spm=1001.2014.3001.5502)


## iText8 主要功能

iText8 是一个功能强大的Java库，用于创建、操作和处理PDF文档。** 它提供了丰富的API，支持各种复杂的PDF处理任务，如创建、编辑、添加内容、渲染等。

iText8的主要功能包括：

1. **创建和编辑PDF文档**：用户可以通过iText8创建新的PDF文档，并在文档中添加文本、图片、表格等多种内容1。
2. **渲染高质量的PDF内容**：iText8支持高质量的PDF渲染，适用于需要高质量输出的应用场景2。
3. **优化PDF文档**：iText8提供了优化工具，可以帮助压缩图像、删除无用对象，从而减小PDF文件的大小，提高性能2。
4. **处理复杂的PDF任务**：iText8能够处理复杂的PDF任务，如添加水印、生成条形码和二维码、处理XFA表单等
5. **创建PDF文档**： 从头开始设计每一页的内容、样式和布局。
6. **操作PDF文档**： 添加、删除和修改文本、图像、表格等内容。
7. **文本处理**： 可以对文本进行格式化、添加链接、插入特殊字符等。
8. **图像处理**： 它支持添加图像到PDF文件中，并提供了对图像进行调整、裁剪和压缩的功能。
9. **表格处理**： 创建和编辑PDF中的表格，包括设置表格的大小、行高、列宽等。
10. **字体和样式**： 控制文本的字体、大小、颜色和样式，以及添加特殊效果如加粗、斜体等。
11. **页面布局**： 允许对页面进行布局，包括添加页眉、页脚、页码等。
12. **加密与安全**： 它支持PDF文档的加密和数字签名，以确保文档的安全性和完整性。

## iText8 套件

iText8套件主要包括以下几个模块：

- 开源模块：
    - **pdfOCR**：用于在PDF文档中进行光学字符识别（OCR），从扫描的文档中提取文本内容。
    - **pdfSweep**：用于在PDF文档中进行敏感信息的清除和保护，例如删除敏感数据或隐藏个人信息。
    - **iText Core**：用于创建和处理PDF文档，提供丰富的API来创建、操作和处理PDF文件。
    - **pdfHTML**：用于将HTML内容转换为PDF文档，帮助开发人员将网页内容快速转换为可打印和分享的PDF文件。
- 闭源模块：
    - **pdfCalligraph**：用于在PDF中实现高质量的字体渲染和排版。
    - **pdfXFA**：用于处理PDF中的XFA表单数据，支持生成和填充XFA表单。
    - **pdfRender**：用于高质量的PDF渲染，支持在应用程序中显示PDF文档的内容。
    - **pdfOptimizer**：用于优化PDF文档的大小和性能，包括压缩图像、删除无用对象等。
    - **pdfOffice**：用于在Java应用程序中生成和编辑PDF文档，支持丰富的文本和图形操作。

这些模块中，开源模块可以免费使用，而闭源模块则需要购买许可才能使用。选择使用iText8套件时，需要根据具体需求选择合适的模块

