# springboot-file-office-libreoffice

## LibreOffice 功能简介

LibreOffice 是一款开源的办公套件，支持文档处理、电子表格、演示文稿、矢量图形、数据库等。

在 Java 中通过 UNO Bridge（Universal Network Objects）调用其 API，可实现以下功能：

* 格式转换 ：支持 Word 到 PDF、Excel 到 PDF 等多种格式转换，且兼容性优于其他工具
* 远程/本地调用 ：可通过命令行或 API 调用，支持本地进程或远程服务模式
* 跨平台支持 ：可在 Windows、Linux、macOS 等系统运行，适合服务端部署
* 文档格式转换（如 DOCX → PDF、ODS → XLSX、PPT → HTML）
* 文档内容提取（提取文本、表格数据）
* 批量文档处理（水印添加、页眉页脚修改）
* 动态生成报告（基于模板填充数据）


## 常见应用场景

* 文档预览 ：将用户上传的 Office 文件转为 PDF 或图片，在浏览器中直接展示
* 批量转换 ：自动化处理大量文件（如合同、报告）的格式转换需求
* 服务端生成报告 ：结合模板引擎（如 FreeMarker）动态生成 Word/PDF 文档
* 企业文档服务：后台自动将用户上传的 DOCX 合同转为 PDF。
* 数据报表系统：将数据库数据导出为 Excel 或 PDF 格式。
* 在线预览功能：将 Office 文档转换为 HTML 实现网页预览。
* 批量打印服务：批量处理数百个文档并发送到打印机队列。

## 快速开始

#### 安装组件 LibreOffice

- [libreoffice 官网](https://www.libreoffice.org/)

#### 引入依赖

```xml
        <!--LibreOffice-->
        <dependency>
            <groupId>org.libreoffice</groupId>
            <artifactId>libreoffice</artifactId>
            <version>24.8.4</version>
        </dependency>
```