# ocr-javacv

JavaCV是一个基于Java的计算机视觉和音视频处理库，它是OpenCV、FFmpeg等多个开源库的Java接口封装。

## JavaCV 主要功能和应用场景

JavaCV提供了丰富的功能和广泛的应用场景，以下是JavaCV的主要功能和应用场景的概述：

1. 计算机视觉：JavaCV结合了OpenCV库的功能，提供了丰富的计算机视觉功能，包括图像处理、特征提取、模式识别、物体检测、人脸识别、目标跟踪等。它可以用于开发各种计算机视觉应用，如图像处理软件、人脸识别系统、视频监控等。
2. 视频流处理：JavaCV与FFmpeg库集成，可以进行实时视频流处理和分析。它可以从摄像头、视频文件或网络摄像头捕获视频流，并提供视频解码、编码、转码、截图、流媒体传输等功能。这使得JavaCV成为开发视频处理、视频流分析和实时流媒体应用的强大工具。
3. 音频处理和分析：JavaCV结合了FFmpeg库的音频处理功能，可以进行音频解码、编码、转码等操作。它还提供了音频特征提取、频谱分析、音频合成等功能，可用于开发音频处理应用、音乐分析软件等。
4. 机器学习和深度学习：JavaCV可以与其他机器学习和深度学习库集成，如TensorFlow、DL4J等。它提供了图像和视频数据的处理和转换功能，以及与模型的集成，使得开发计算机视觉和深度学习应用更加便捷。
5. 图像和视频的IO操作：JavaCV支持图像和视频文件的读取和写入，包括各种图像格式（如JPEG、PNG）和视频格式（如AVI、MP4）。它还提供了对视频流的处理和分析，如从视频流中读取帧、写入帧等。
6. 并行计算和加速：JavaCV通过利用硬件加速和并行计算的功能，提供了对图像和视频处理的加速。它可以利用GPU进行并行计算，提高处理速度，特别是在大规模图像和视频处理任务中。
7. 移动应用开发：JavaCV可以用于开发移动应用程序，包括Android平台上的计算机视觉和音视频处理应用。它提供了与Android平台的集成，并且能够利用设备的摄像头、图像和视频处理功能。

JavaCV在计算机视觉、音视频处理和图像处理领域具有广泛的应用。它为Java开发者提供了强大的工具和接口，使得他们能够使用Java语言进行高效的视觉和音视频处理开发。

## FFmpeg 主要功能和应用场景

FFmpeg提供了广泛的功能和应用场景，以下是FFmpeg的一些主要功能和应用场景的概述：

1. 音视频编解码：FFmpeg支持多种音视频编解码器，可以解码和编码各种音视频格式，包括常见的格式如MP4、AVI、MKV、FLV等。这使得FFmpeg成为处理音视频文件和进行格式转换的强大工具。
2. 视频处理和编辑：FFmpeg提供了丰富的视频处理功能，例如裁剪、缩放、旋转、倒放、调整帧率等。它还支持视频滤镜和特效，可以进行色彩调整、去噪、模糊、水印添加等操作。
3. 音频处理和编辑：FFmpeg可以对音频文件进行处理和编辑，例如音频剪辑、混音、音量调整、音频格式转换等。它还支持音频滤波器，可以应用均衡器、回声消除、降噪等音频效果。
4. 视频流媒体和直播：FFmpeg支持实时流媒体传输，包括视频直播和音频流媒体。它可以将本地视频文件或摄像头输入进行编码并通过常用的流媒体协议（如RTMP、RTSP、HLS）进行实时传输。
5. 音视频录制：FFmpeg可以从音视频设备（如摄像头、麦克风）或屏幕捕捉进行录制，并保存为音视频文件。
6. 视频截图和缩略图生成：通过FFmpeg，可以从视频文件中抓取帧并生成静态图像，用于创建视频缩略图或截图。
7. 视频转码和压缩：FFmpeg可以转码视频文件，改变其编解码器、分辨率、比特率等参数，以及进行视频压缩以减小文件大小。
8. 音视频合并和分割：它可以将多个音视频文件合并为一个文件，或者将一个音视频文件分割成多个片段。
9. 视频流分析和元数据提取：FFmpeg可以提取音视频文件的元数据信息，如分辨率、帧率、时长、编码器等，以及进行视频流分析，如检测关键帧、计算视频质量等。

这只是FFmpeg提供的功能的一小部分。由于FFmpeg是一个功能强大且灵活的工具集，它可以在各种应用场景中使用，包括视频编辑软件、媒体转码工具、流媒体服务器、实时视频处理和分析等。许多流媒体平台、视频网站和应用程序都使用FFmpeg作为其核心音视频处理引擎。

## OpenCV 主要功能和应用场景

OpenCV（Open Source Computer Vision）是一个开源的计算机视觉库，提供了丰富的功能和算法，用于图像处理、计算机视觉和机器学习等领域。

下面是OpenCV的主要功能和应用场景的概述：

1. 图像处理：OpenCV提供了各种图像处理功能，包括图像滤波、边缘检测、图像增强、图像分割、形态学操作等。它可以用于图像修复、图像增强、目标提取等任务。
2. 物体检测和跟踪：OpenCV提供了多种物体检测和跟踪算法，如Haar特征检测、HOG特征检测、卷积神经网络（CNN）等。这使得它在目标检测、人脸识别、行人检测、运动跟踪等应用中具有广泛的应用。
3. 人脸识别和表情分析：OpenCV支持人脸检测、人脸识别和表情分析。它提供了人脸特征点检测、人脸特征描述和匹配等功能，可用于人脸识别、人脸表情分析、人脸姿态估计等应用。
4. 机器学习和深度学习：OpenCV集成了机器学习库和深度学习框架，如支持向量机（SVM）、决策树、随机森林、神经网络等。它提供了图像分类、物体识别、图像生成等机器学习和深度学习算法，可用于图像分类、目标识别、图像生成等任务。
5. 视频分析和处理：OpenCV支持视频的读取、写入和处理。它提供了视频流的处理、光流估计、背景建模、视频稳定等功能。这使得它在视频分析、视频监控、视频编辑等应用中得到广泛应用。
6. 相机标定和校正：OpenCV提供了相机标定和校正的功能，可以通过对图像和相机参数的分析，精确地估计相机的内外参数，实现图像的几何校正和立体视觉等应用。
7. 图像特征提取和描述：OpenCV提供了多种图像特征提取和描述算法，如SIFT、SURF、ORB、BRIEF等。这些算法可以用于图像匹配、图像拼接、目标识别等任务。
8. 图像配准和重建：OpenCV支持图像配准、图像融合和图像重建等功能。它可以将多个图像进行配准和融合，实现全景图像的生成和图像重建等任务。

OpenCV在计算机视觉、图像处理和机器学习等领域具有广泛的应用。它被广泛用于图像处理软件、计算机视觉研究、机器人视觉、自动驾驶、医学影像分析、工业检测等领域。由于OpenCV是一个开源库，它有一个庞大的用户群体和活跃的开发社区，提供了丰富的文档、示例代码和支持，使得开发人员能够快速应用和定制OpenCV的功能。

## 依赖组件

JavaCV依赖以下几个组件：

1. OpenCV库：JavaCV是OpenCV的Java接口封装，因此需要安装OpenCV库。你可以从OpenCV的官方网站 [https://opencv.org/releases/](https://opencv.org/releases/)下载适用于你的操作系统的OpenCV库。
2. FFmpeg库：JavaCV还集成了FFmpeg库，用于处理音视频数据。你需要下载适用于你的操作系统的FFmpeg库，可以从FFmpeg的官方网站[https://ffmpeg.org/](https://ffmpeg.org/)获取。

### 安装OpenCV组件：

- [opencv.org 官网下载 Windows](https://opencv.org/releases/)

双击`opencv-4.9.0-windows.exe`,得到目录`opencv`

配置系统环境变量(以java为例)：`E:\App\opencv-4.9.0\build\java\x64` ，主要是把opencv_java470.dll父目录配置环境变量中。

### 安装FFmpeg组件：

- [ffmpeg.org 官网下载 Windows](https://ffmpeg.org/download.html#build-windows)

配置系统环境变量(以java为例)：`E:\App\ffmpeg-7.0.1-full_build\bin` ，主要是把ffmpeg.exe父目录配置环境变量中。

## 依赖引入

```xml

<dependencies>
    <!--ffmpeg 音视频库，底层依赖：ffmpeg、opencv-->
    <!--需安装ffmpeg组件-->
    <dependency>
        <groupId>org.bytedeco</groupId>
        <artifactId>javacv-platform</artifactId>
        <version>1.5.9</version>
    </dependency>
</dependencies>
```

## 代码示例

### 使用JavaCV执行视频转换操作

```java
import org.bytedeco.javacv.*;
import org.bytedeco.ffmpeg.*;

public class FFmpegExample {
    public static void main(String[] args) {
        // 创建了一个FFmpegFrameGrabber对象来读取输入视频文件
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("input.mp4");
        // 创建了一个FFmpegFrameRecorder对象来写入输出视频文件
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("output.mp4", 1280, 720);

        try {
            grabber.start();
            recorder.start();

            // 使用grabFrame()方法从输入视频中逐帧读取，并使用record()方法将帧写入输出视频
            Frame frame;
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }

            recorder.stop();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 使用JavaCV读取和处理图像

```java
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgcodecs.*;
import org.bytedeco.javacpp.opencv_imgproc.*;

public class OpenCVExample {
    public static void main(String[] args) {
        // 使用imread()函数读取输入图像文件，并将其存储在Mat对象中
        Mat image = imread("input.jpg", IMREAD_COLOR);

        if (image.empty()) {
            System.out.println("Failed to read image");
            return;
        }

        // 使用cvtColor()函数将彩色图像转换为灰度图像
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, COLOR_BGR2GRAY);

        // 使用imwrite()函数将处理后的图像保存到输出文件
        imwrite("output.jpg", grayImage);
    }
}
```

