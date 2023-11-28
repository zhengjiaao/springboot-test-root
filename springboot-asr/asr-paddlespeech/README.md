## asr-PaddleSpeech

- [PaddleSpeech](https://github.com/PaddlePaddle/PaddleSpeech)
- [PaddleSpeech 语音合成音频示例](https://paddlespeech.readthedocs.io/en/latest/tts/demo.html)

PaddleSpeech 是 PaddlePaddle 平台上的开源工具包，用于语音中的两项关键任务 -
语音转文本（自动语音识别，ASR）和文本转语音合成（TTS），其模块涉及最先进的和有影响力的典范。

## 安装（win未成功）

> PaddleSpeech 依赖于 paddlepaddle(依赖python)

* gcc >= 4.8.5
* paddlepaddle <= 2.5.1
* python >= 3.8
* OS support: Linux(recommend), Windows, Mac OSX

```shell
# 一定要注意paddlepaddle版本 2.4.2 或 2.5.1
pip install paddlepaddle==2.5.1
pip install pytest-runner
pip install paddlespeech
```

## 快速开始

> 安装完成后，开发者可以通过命令行或者 Python 快速开始，命令行模式下改变 --input 可以尝试用自己的音频或文本测试，支持 16k
> wav 格式音频。

### 测试音频示例下载

```text
wget -c https://paddlespeech.bj.bcebos.com/PaddleAudio/zh.wav
wget -c https://paddlespeech.bj.bcebos.com/PaddleAudio/en.wav
```

### 语音识别

> 开源中文语音识别

命令行：

```shell
paddlespeech asr --lang zh --input zh.wav
```

Python API 一键预测：

```python
from paddlespeech.cli.asr.infer import ASRExecutor

asr = ASRExecutor()
result = asr(audio_file="zh.wav")
print(result)
```

## 模型了解

- [model-list](https://github.com/PaddlePaddle/PaddleSpeech#model-list)

## 遇到问题

问题：AttributeError: module 'numpy' has no attribute 'complex'. `np.complex` was

如果有numpy的方法报错，也进行降级到1.23.5后可以正常访问，程序运行过程中出现的警告信息不影响程序正常返回结果。

解决方案：降低numpy版本

```shell
# 查看已安装的NumPy版本：
pip show numpy

# 卸载NumPy：
pip uninstall numpy

# 推荐安装指定版本
pip install numpy
pip install numpy==<version>

# 这里推荐安装版本 numpy==1.22.4
pip install numpy==1.22.4

# 升级版本到最新版本
pip install --upgrade numpy

# 查看已安装列表
pip list
```

问题：assert speech.shape[0] == speech_lengths.shape[0].IndexError: list index out of range

由于paddlepaddle是根据官方推荐的安装版本，使用的2.5，引起部分库版本的不兼容。
强制降低版本到2.4.2及其他一些依赖降级，就可以跑了.

```shell
pip install "paddlepaddle<2.5" -i https://mirror.baidu.com/pypi/simple
pip install "paddlenlp<2.6" -i https://mirror.baidu.com/pypi/simple
pip install "ppdiffusers<0.16" -i https://mirror.baidu.com/pypi/simple
```
