# springboot-file-pdf-itext8

- [itext 官网](https://itextpdf.com/)
- [itext 版本发行列表](https://kb.itextpdf.com/itext/releases)
- [itext8 安装指南](https://kb.itextpdf.com/itext/installation-guidelines)
- [itext7-8 iText：Java 快速入门教程](https://kb.itextpdf.com/itext/itext-jump-start-tutorial-for-java)
- [itext8 用法参考](https://blog.csdn.net/xiaofeng_yang/article/details/139902089)

## iText5 主要功能

iText5的主要功能包括：

1. **创建和编辑PDF文档**：iText可以创建新的PDF文档，并在其中添加文本、图像、表格、超链接等内容。它也可以对已有的PDF文档进行编辑，包括修改内容、添加水印、设置页面属性等12。
2. **加密和解密PDF文档**：iText支持对PDF文档进行加密和解密，提供多种加密算法和安全选项，确保文档的安全性12。
3. **数字签名**：iText可以添加数字签名到PDF文档，验证文档的完整性和真实性12。
4. **表单处理**：iText可以帮助用户创建和处理PDF表单，包括填写表单、验证表单数据等功能12。
5. **转换其他格式为PDF**：iText支持将HTML、XML等其他格式的文档转换为PDF格式，方便内容的共享和打印12。
6. **合并和拆分PDF文档**：iText可以将多个PDF文档合并为一个文档，或将一个文档拆分为多个文档12。
7. **压缩和解压缩PDF文档**：iText可以压缩和解压缩PDF文档，以减小文件大小2。
8. **自定义字体和样式**：iText支持自定义字体和样式，允许使用自己的字体、字号和样式来创建PDF文档2。
9. **支持多国语言**：iText支持多种语言，包括中文、日文、韩文等，以支持不同文化的文档12。
10. **支持多线程**：iText支持多线程，可以同时处理多个任务，以提升处理效率。
11. **支持多平台**：iText支持Windows、Linux、Mac等主流操作系统，以支持不同平台的文档处理。

此外，iText还提供了一些高级功能，如光学字符识别（OCR）、敏感信息清除和保护、高质量的字体渲染和排版等，这些功能主要通过不同的模块实现，如iText
Core、pdfHTML、pdfOCR等

## iText5 套件

iText5套件主要包括以下几个模块：

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

