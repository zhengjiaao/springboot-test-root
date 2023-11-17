# ocr-paddleocr

- [paddlepaddle/PaddleOCR](https://gitee.com/paddlepaddle/PaddleOCR)
- [paddlepaddle/PaddleOCR-doc_ch](https://gitee.com/paddlepaddle/PaddleOCR/blob/release/2.6/doc/doc_ch/quickstart.md)

**说明**

PaddleOCR(百度飞桨OCR)旨在打造一套丰富、领先、实用的OCR工具库，帮助开发者训练出更好的模型，并应用落地。
基于PaddlePaddle的超棒多语言OCR工具包（实用的超轻量级OCR系统，支持80+语言识别，提供数据标注和合成工具，支持在服务器、移动、嵌入式和物联网设备之间训练和部署）。

## 如何使用

> 首先需要`python`环境，然后本地pip安装`paddlepaddle`，`paddleocr`模块， 然后可以在命令行下测试`paddleocr`是否可以正常调用。

### 安装

```shell
pip install paddlepaddle paddleocr
```

校验：python_test.py

```python
import paddleocr

print(paddleocr.__version__)
```

### 命令行操作

通过OCR技术：版面恢复

```shell
# 中文测试图
paddleocr --image_dir=ppstructure/docs/table/1.png --type=structure --recovery=true
# 英文测试图
paddleocr --image_dir=ppstructure/docs/table/1.png --type=structure --recovery=true --lang='en'
# pdf测试文件
paddleocr --image_dir=ppstructure/recovery/UnrealText.pdf --type=structure --recovery=true --lang='en'
```

### python操作

paddleocr --image_dir=D:\temp\ocr\input.png --type=structure --recovery=true --lang='en'

paddleocr --image_dir=D:\temp\ocr\input.pdf --type=structure --recovery=true --lang='en'

paddleocr --image_dir=D:\temp\ocr\input.png > output.txt

## 文字识别模型

https://github.com/PaddlePaddle/Paddle-Lite-Demo

![img.png](img.png)