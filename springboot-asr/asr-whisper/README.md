# asr-whisper

- [openai/whisper](https://github.com/openai/whisper)

whisper(耳语) 是一种通用语音识别模型。它是在大量不同音频数据集上进行训练的，也是一个多任务模型，可以执行多语言语音识别、语音翻译和语言识别。

## 在线安装

### windwos for whisper

> whisper 依赖于 Python环境。

1、使用以下命令下载并安装（或更新到）最新版本的 Whisper：

```shell
pip install -U openai-whisper

#或，从该存储库中提取并安装最新的提交及其 Python 依赖项
pip install git+https://github.com/openai/whisper.git 
```

2、安装 [ffmpeg](https://ffmpeg.org/) 组件

先安装chocolatey，方便后面windwos安装ffmpeg

```shell
# 进入管理员命令行执行
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

安装 ffmpeg

```shell
# on Windows using Chocolatey (https://chocolatey.org/)
choco install ffmpeg
```

3、安装 setuptools-rust

```shell
pip install setuptools-rust
```

4、命令行测试 whisper

下载测试示例文件，链接：https://pan.baidu.com/s/1g4iSXpU6dPHeoEPI6L35Qg?pwd=t0c0

进行测试：

```shell
# 以下命令将使用medium模型转录音频文件中的语音
whisper zh.wav --model medium

# 输出结果
我認為跑步最重要的就是給我帶來了身體健康

# 更多用法
whisper --help
```

> 支持多种格式：audio.flac、audio.mp3、audio.wav等
> --model 默认设置（选择模型`small`）非常适合转录英语。
> --language chinese
> 指定语言，默认自动检测。有关所有可用语言的列表: [tokenizer.py](https://github.com/openai/whisper/blob/main/whisper/tokenizer.py)
> --task translate 翻译成英语
> whisper --help 更多示例

## 模型下载位置

```shell
C:\Users\{Administrator}\.cache\whisper

#示例：
C:\Users\zhengjiaao\.cache\whisper
```