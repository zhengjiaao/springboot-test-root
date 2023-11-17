# ocr-xiaoshen

**说明**

## 环境准备

- Tesseract 安装(必须)
- PaddleOCR 安装(必须的，依赖Python)
- OCRmyPDF 安装(可选的)

### 安装 Tesseract

1. windows for tesseract.exe 安装示例：

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

下载训练集(可选的)：https://github.com/tesseract-ocr/tessdata

验证安装成功

```shell
tesseract --version
```

另外，可以命令行验证

```shell
#示例：识别结果out.txt,输出在当前目录下
tesseract.exe ocr-test.png out -l chi_sim
```

### 安装 PaddleOCR 安装(必须的，依赖Python)

> 首先需要`python`环境，然后本地pip安装`paddlepaddle`，`paddleocr`模块， 然后可以在命令行下测试`paddleocr`是否可以正常调用。

安装

```shell
pip install paddlepaddle paddleocr
```

校验：python_test.py

```python
import paddleocr

print(paddleocr.__version__)
```

另外，可以命令行验证

```shell
paddleocr --image_dir ./imgs/11.jpg --use_angle_cls true --use_gpu false
```

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



