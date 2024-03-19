# asr-funasr

- [FunASR](https://github.com/alibaba-damo-academy/FunASR/tree/main)
- [FunASR Doc_zh](https://alibaba-damo-academy.github.io/FunASR/en/installation/installation_zh.html)
- [model 模型参考](https://github.com/alibaba-damo-academy/FunASR/tree/main#model-zoo)
- [快速入门-更多语法](https://alibaba-damo-academy.github.io/FunASR/en/funasr/quick_start_zh.html)

FunASR 是一个基本的语音识别工具包，提供多种功能，包括语音识别 (ASR)、语音活动检测 (VAD)、标点符号恢复、语言模型、说话人验证、说话人分类和多说话者
ASR。FunASR提供方便的脚本和教程，支持预训练模型的推理和微调。

## 安装

```shell
pip3 install -U funasr
```

## 实例

命令行

```shell
funasr --model paraformer-zh asr_example_zh.wav
```

Python

```python
from funasr import infer

p = infer(model="paraformer-zh", vad_model="fsmn-vad", punc_model="ct-punc", model_hub="ms")

res = p("asr_example_zh.wav", batch_size_token=5000)
print(res)
```

## 模型

- [model 参考](https://github.com/alibaba-damo-academy/FunASR/tree/main#model-zoo)

模型默认自动下载位置：
```shell
C:\Users\{Administrator}\.cache\modelscope

#示例：
C:\Users\zhengjiaao\.cache\modelscope\hub\damo
```
