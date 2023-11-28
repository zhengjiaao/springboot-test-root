# asr-faster-whisper

- [guillaumekln/faster-whisper | github](https://github.com/guillaumekln/faster-whisper)

fast-whisper是使用CTranslate2重新实现 OpenAI 的 Whisper 模型，CTranslate2 是 Transformer 模型的快速推理引擎。

此实现速度比openai/whisper快 4 倍，并且使用更少的内存，但具有相同的精度。通过 CPU 和 GPU 上的 8 位量化，可以进一步提高效率。

## 安装

```shell
pip install faster-whisper
```

## 用法

```python
from faster_whisper import WhisperModel

model_size = "large-v2"

# Run on GPU with FP16
model = WhisperModel(model_size, device="cuda", compute_type="float16")

# or run on GPU with INT8
# model = WhisperModel(model_size, device="cuda", compute_type="int8_float16")
# or run on CPU with INT8
# model = WhisperModel(model_size, device="cpu", compute_type="int8")

segments, info = model.transcribe("audio.mp3", beam_size=5)

print("Detected language '%s' with probability %f" % (info.language, info.language_probability))

for segment in segments:
    print("[%.2fs -> %.2fs] %s" % (segment.start, segment.end, segment.text))
```

## 模型下载位置

```shell
C:\Users\{Administrator}\.cache\huggingface

#示例：
C:\Users\zhengjiaao\.cache\huggingface
```