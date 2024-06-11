# ocr-opencv

- [opencv.org 官网下载](https://opencv.org/releases/)
- [maven search?q=opencv](https://mvnrepository.com/search?q=opencv)
- [haarcascades Haar级联分类器的XML文件](https://github.com/opencv/opencv/blob/4.x/data/haarcascades)

**说明**

OpenCV是一个流行的计算机视觉库，它提供了许多图像处理和计算机视觉算法的功能，包括图像读取、处理、分析和显示等。

OpenCV（Open Source Computer Vision
Library）是一个开源的计算机视觉和机器学习软件库。它提供了广泛的图像和视频处理函数和算法，包括图像滤波、特征检测和提取、物体识别、相机标定等。OpenCV使用C++编写，并提供了Python和Java等多种编程语言的绑定。

以下是OpenCV的一些主要特点和功能：

1. 图像和视频输入输出：OpenCV允许您读取和写入各种格式的图像和视频，包括常见格式如JPEG、PNG和MPEG。
2. 图像处理：OpenCV提供了丰富的图像处理函数，如图像滤波（模糊、锐化等）、阈值处理、形态学操作（腐蚀、膨胀）和颜色空间转换等。
3. 特征检测和提取：OpenCV包括用于检测和提取各种图像特征的算法，如角点（Harris角点检测、Shi-Tomasi算法）、边缘（Canny边缘检测）和关键点（SIFT、SURF、ORB等）。
4. 物体识别：OpenCV支持物体识别任务，包括模板匹配、Haar级联和基于机器学习的技术，如支持向量机（SVM）和深度学习框架（如TensorFlow和PyTorch集成）。
5. 相机标定：OpenCV提供了相机标定函数，可以估计相机的内参数和外参数矩阵、畸变系数和矫正变换等。这对于三维重建和增强现实等任务非常有用。
6. 机器学习：OpenCV与TensorFlow和PyTorch等机器学习框架集成，在各种计算机视觉任务中可以利用深度学习模型，如图像分类、目标检测和语义分割。
7. GPU加速：OpenCV通过CUDA和OpenCL等框架支持GPU加速，可以在兼容的硬件上加快计算密集型任务的执行速度。
8. OpenCV拥有庞大而活跃的社区，为其开发提供支持，并提供丰富的资源，包括教程、示例代码和支持论坛。它在学术界和工业界被广泛应用于各种计算机视觉应用，如机器人技术、监控、自动驾驶车辆、医学成像等。

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

## OpenCV在实际应用中的一些案例

1. 人脸检测和识别：OpenCV提供了强大的人脸检测和识别功能，可以用于人脸识别门禁系统、人脸标签和表情识别应用等。
2. 物体检测和跟踪：OpenCV的物体检测和跟踪算法可以用于实时视频监控、自动驾驶中的障碍物检测、运动跟踪等应用。
3. 图像拼接和全景图生成：OpenCV的图像拼接功能可以将多张图片拼接成一张大的全景图，广泛应用于虚拟旅游、地图制作等领域。
4. 视频流分析：OpenCV可以对实时视频流进行分析，例如车牌识别、行人计数、姿态估计等，可用于交通监控、人流统计等应用。
5. 增强现实（AR）：OpenCV提供了相机标定和姿态估计等功能，可以用于增强现实应用，如虚拟物体放置、人脸滤镜等。
6. 医学图像处理：OpenCV在医学图像处理中被广泛应用，如医学图像分割、肿瘤检测、病变识别等，有助于医生进行诊断和治疗决策。
7. 视觉SLAM（同时定位与地图构建）：OpenCV可以用于视觉SLAM，实现机器人和无人机的自主导航和环境建模。
8. 虹膜识别：OpenCV可以用于虹膜图像的特征提取和匹配，用于个人身份认证和安全访问控制。
9. 自动驾驶：OpenCV在自动驾驶领域中扮演着重要的角色，用于环境感知、障碍物检测、车道线识别和跟踪等任务。
10. 机器人导航：OpenCV可用于机器人导航与定位，包括地标识别、地图构建和路径规划等。
11. 人机交互：OpenCV可以用于手势识别和追踪，从而实现基于手势的人机交互，如手势控制电脑、虚拟现实交互等。
12. 虚拟化与增强现实：OpenCV可用于虚拟现实和增强现实应用，如虚拟化试衣间、虚拟游戏角色等。
13. 图像修复与恢复：OpenCV的图像修复算法可用于修复老旧照片、去除图像噪声、恢复受损图像等。
14. 文字检测与识别：OpenCV提供了文字检测和识别的功能，可应用于自动化文档处理、光学字符识别（OCR）等。
15. 视频分析与编辑：OpenCV可以用于视频分析和编辑，如视频稳定、运动检测、视频特效等。
16. 艺术创作与图像处理：OpenCV可以用于艺术创作、图像风格迁移、图像滤镜等应用，探索图像处理的创造性应用。

## OpenCV Haar级联分类器HaarCascades

OpenCV提供了一系列的Haar级联分类器（Haar cascades），用于对象检测和识别。以下是一些常见的Haar级联分类器：

- [获取OpenCV Haar级联分类器的XML文件](https://github.com/opencv/opencv/blob/4.x/data/haarcascades)

1. 关于人体检测的Haar级联分类器：

* 人脸检测 (haarcascade_frontalface_default.xml, haarcascade_frontalface_alt.xml, haarcascade_frontalface_alt2.xml):
    * 用于检测图像或视频中的人脸。
    * 应用：人脸识别、表情分析、年龄和性别识别、情绪检测。
* 眼睛检测 (haarcascade_eye.xml, haarcascade_eye_tree_eyeglasses.xml):
    * 用于检测人脸中的眼睛位置，包括戴眼镜的情况。
    * 应用：眼部追踪、眨眼检测、疲劳驾驶预警、面部表情分析。
* 眼睛和眼镜检测 (haarcascade_eye_tree_eyeglasses.xml):
    * 专门检测佩戴眼镜的人脸中的眼睛。
    * 应用：眼镜佩戴者的人脸识别和分析。
* 眉毛检测 (haarcascade_mcs_eyebrow.xml):
    * 用于检测人脸中的眉毛。
    * 应用：面部特征识别、表情分析。
* 鼻子检测 (haarcascade_mcs_nose.xml):
    * 用于检测人脸中的鼻子位置。
    * 应用：面部特征提取、表情识别，表情分析、人机交互等领域。
* 嘴巴检测 (haarcascade_mouth.xml):
    * 用于检测人脸中的嘴巴位置。
    * 应用：表情分析、语音识别配合。
* 笑脸检测 (haarcascade_smile.xml):
    * 用于检测人脸中的微笑表情。
    * 应用：情感分析、人机交互、表情识别。
* 上半身检测 (haarcascade_upperbody.xml):
    * 用于检测图像或视频中的上半身，包括头部、肩膀和胸部等。
    * 应用：行人检测、监控系统、人体姿态估计、运动分析。
* 下半身检测 (haarcascade_lowerbody.xml):
    * 用于检测图像或视频中的下半身，如腿部。
    * 应用：行人检测、人体姿态估计、行人跟踪、运动分析。
* 全身检测 (haarcascade_fullbody.xml):
    * 用于检测图像或视频中的全身部分，包括头部、肩膀、胸部和腿部等。
    * 应用：行人检测、人体姿态估计、运动分析、行人跟踪等应用。
* 头部检测 (haarcascade_head.xml):
    * 用于检测图像或视频中的头部。
    * 应用：人脸识别、头部追踪、虚拟现实、手部检测。
* 行人检测 (haarcascade_pedestrian.xml):
    * 用于检测图像或视频中的行人。
    * 应用：智能交通监控、安全系统、人流量统计。
* 耳朵检测（haarcascade_mcs_leftear.xml和haarcascade_mcs_rightear.xml）：用于检测人脸中左右耳朵的位置。
* 人脸轮廓检测（haarcascade_profileface.xml）：用于检测人脸的侧面轮廓。

2. 关于动物检测的Haar级联分类器：

* 动物检测 (haarcascade_frontalcatface.xml, haarcascade_frontaldogface.xml):
    * 用于检测图像或视频中的猫脸和狗脸。
    * 应用：宠物识别、动物行为研究、动物保护。
* 坐着的动物检测 (haarcascade_sittingcat.xml, haarcascade_sittingdog.xml):
    * 用于检测图像或视频中坐着的猫和狗。
    * 应用：宠物行为分析、动物活动监测。
* 猫脸检测 (haarcascade_frontalcatface.xml):
    * 专门用于检测图像或视频中的猫脸。
    * 应用：猫咪识别、猫的行为研究。
* 猫眼检测 (haarcascade_eye.xml):
    * 虽然通用，但也可用于检测猫脸中的眼睛位置。
    * 应用：猫的视觉行为分析、猫的面部特征识别。
* 狗脸检测 (haarcascade_frontalface_alt2.xml):
    * 用于检测图像或视频中的狗脸。
    * 应用：狗狗识别、狗的行为研究。

3. 关于车辆检测的Haar级联分类器：

* 车辆检测 (haarcascade_car.xml):
    * 用于检测图像或视频中的车辆。
    * 应用：交通监控、智能驾驶、停车场管理、车牌识别、车辆计数。
* 车牌检测 (haarcascade_russian_plate_number.xml):
    * 专门用于检测俄罗斯车牌的位置。
    * 应用：交通管理、自动收费系统、车辆追踪。

4. 关于其他物体检测的Haar级联分类器：

* 前景检测 (haarcascade_fullbody.xml):
    * 用于检测图像或视频中的前景对象，包括行人和车辆。
    * 应用：智能安防、背景减除、物体追踪。
* 飞机检测 (haarcascade_frontalface_alt2.xml):
    * 通常用于人脸检测，但可能被改编或训练以检测飞机。
    * 应用：空中交通监控、无人机检测。
* 银行卡检测 (haarcascade_creditcard.xml):
    * 专门用于检测图像中的银行卡。
    * 应用：金融安全、自助服务终端、ATM监控。
* 其他对象检测:
    * 可根据具体场景和需求训练或获取特定对象的分类器，如自行车、摩托车、书本等。
    * 应用：智能安防、库存管理、文档识别、商品识别。

## 依赖组件

OpenCV依赖以下几个组件：

1. OpenCV库：JavaCV是OpenCV的Java接口封装，因此需要安装OpenCV库。你可以从OpenCV的官方网站 [https://opencv.org/releases/](https://opencv.org/releases/)下载适用于你的操作系统的OpenCV库。
2. FFmpeg库：JavaCV还集成了FFmpeg库，用于处理音视频数据。你需要下载适用于你的操作系统的FFmpeg库，可以从FFmpeg的官方网站[https://ffmpeg.org/](https://ffmpeg.org/)获取。

### 安装OpenCV组件：

- [opencv.org 官网下载 Windows](https://opencv.org/releases/)

双击`opencv-4.9.0-windows.exe`,得到目录`opencv`

配置系统环境变量(以java为例)：`E:\App\opencv-4.9.0\build\java\x64` ，主要是把opencv_java470.dll父目录配置环境变量中。

### 安装FFmpeg组件：

- [ffmpeg.org 官网下载 Windows](https://ffmpeg.org/download.html#build-windows)

配置系统环境变量(以java为例)：`E:\App\ffmpeg-7.0.1-full_build\bin` ，主要是把ffmpeg.exe父目录配置环境变量中。

## 代码实例

### 引入依赖

```xml

<dependencies>
    <!--opencv-->
    <dependency>
        <groupId>org.openpnp</groupId>
        <artifactId>opencv</artifactId>
        <version>4.9.0-0</version>
    </dependency>
    <dependency>
        <groupId>org.bytedeco</groupId>
        <artifactId>opencv</artifactId>
        <version>4.9.0-1.5.10</version>
    </dependency>
    <dependency>
        <groupId>org.bytedeco</groupId>
        <artifactId>opencv-platform</artifactId>
        <version>4.9.0-1.5.10</version>
    </dependency>
</dependencies>
```

### 下载OpenCV Haar级联分类器：

- [haarcascades Haar级联分类器的XML文件](https://github.com/opencv/opencv/blob/4.x/data/haarcascades)

### 校验版本

```java
public class OpenCVExample {

    static {
        //先把 E:\App\opencv-4.7.0\build\java\x64\opencv_java470.dll 父目录添加到系统环境变量中。
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        if ((1 == args.length) && (0 == args[0].compareTo("--build"))) {

            System.out.println(Core.getBuildInformation());
        } else if ((1 == args.length) && (0 == args[0].compareTo("--help"))) {

            System.out.println("\t--build\n\t\tprint complete build info");
            System.out.println("\t--help\n\t\tprint this help");
        } else {

            System.out.println("Welcome to OpenCV " + Core.VERSION);
        }
    }
}
```

### 基于图像检测人脸

```java
/**
 * 人脸检测：图片、视频、摄像头
 *
 * @Author: zhengja
 * @Date: 2024-05-21 10:45
 */
public class FaceDetectionTest {

    @Test
    public void test() {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 加载人脸级联分类器
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml"); // 尽可能检测更多人脸，存在不正确检查情况
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml"); // 存在检测不全情况
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml"); // 检测效果较好
        CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml"); // 检测效果最好

        // 图片人脸检测
        detectFacesInImage("D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\Brush1.png", faceCascade);

        // 视频人脸检测
        // detectFacesInVideo("D:\\temp\\opencv\\Video\\1.mp4", faceCascade);

        // 摄像头人脸检测 todo 待测试，没条件
        // detectFacesFromCamera(faceCascade);
    }

    private static void detectFacesInImage(String imagePath, CascadeClassifier faceCascade) {
        // 读取图像文件
        Mat image = Imgcodecs.imread(imagePath);

        // 灰度转换
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 人脸检测
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(gray, faces);

        // 在检测到的人脸区域绘制矩形框
        System.out.println("Number of faces detected: " + faces.toArray().length);
        // System.out.println("Number of faces detected: " + faces.size());
        int i = 0;
        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
            i = i + 1;
            System.out.println("Face " + i + ": " + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
        }

        // 输出识别结果图片
        String outputImagePath = "target/output.png";
        Imgcodecs.imwrite(outputImagePath, image);
        System.out.println("人脸检测完成，结果保存在：" + outputImagePath);

        // 显示结果图像
        HighGui.imshow("Face Detection", image);
        HighGui.waitKey();
    }

}
```

### 基于视频检测人脸

```java
/**
 * 人脸检测：图片、视频、摄像头
 *
 * @Author: zhengja
 * @Date: 2024-05-21 10:45
 */
public class FaceDetectionTest {

    @Test
    public void test() {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 加载人脸级联分类器
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml"); // 尽可能检测更多人脸，存在不正确检查情况
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml"); // 存在检测不全情况
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml"); // 检测效果较好
        CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml"); // 检测效果最好

        // 图片人脸检测
        // detectFacesInImage("D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\Brush1.png", faceCascade);

        // 视频人脸检测
        detectFacesInVideo("D:\\temp\\opencv\\Video\\1.mp4", faceCascade);

        // 摄像头人脸检测 todo 待测试，没条件
        // detectFacesFromCamera(faceCascade);
    }

    private static void detectFacesInVideo(String videoPath, CascadeClassifier faceCascade) {
        // 打开视频文件
        VideoCapture videoCapture = new VideoCapture(videoPath);

        // 检查视频是否成功打开
        if (!videoCapture.isOpened()) {
            System.out.println("无法打开视频文件");
            return;
        }

        // 获取视频的基本信息
        // 获取视频帧率和尺寸
        double frameRate = videoCapture.get(Videoio.CAP_PROP_FPS); // fps
        int frameWidth = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH); // width
        int frameHeight = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT); // height

        // 创建输出视频的编码器和写入器,使用VideoWriter时，确保已经正确安装了FFmpeg并将其添加到系统的环境变量中。
        // int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G'); // 无效编码，输出存在问题
        int fourcc = VideoWriter.fourcc('X', '2', '6', '4');  // 使用H.264编码器
        String outputVideoPath = "target/output.mp4";
        VideoWriter videoWriter = new VideoWriter(outputVideoPath, fourcc, frameRate, new Size(frameWidth, frameHeight));

        // 创建窗口来显示结果
        HighGui.namedWindow("Face Detection");

        // 逐帧读取视频并进行人脸检测
        Mat frame = new Mat();
        while (videoCapture.read(frame)) {
            // 转换为灰度图像
            Mat grayFrame = new Mat();
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

            // 人脸检测
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(grayFrame, faces);

            // 在检测到的人脸区域绘制矩形框
            System.out.println("Frame: " + videoCapture.get(Videoio.CAP_PROP_POS_FRAMES));
            System.out.println("Number of faces detected: " + faces.toArray().length);
            // System.out.println("Number of faces detected: " + faces.size());
            int i = 0;
            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
                i = i + 1;
                System.out.println("Face " + i + ": " + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
            }

            // 写入输出视频
            videoWriter.write(frame);

            // 显示结果帧
            HighGui.imshow("Face Detection", frame);

            // 按ESC键退出
            if (HighGui.waitKey((int) Math.round(1000 / frameRate)) == 27) {
                break;
            }
        }

        // 释放视频捕获资源
        videoCapture.release();
        // 释放资源
        videoWriter.release();
        HighGui.destroyAllWindows();

        System.out.println("人脸检测完成，结果保存在：" + outputVideoPath);
    }
}
```

### 基于摄像头检测人脸

```java
/**
 * 人脸检测：图片、视频、摄像头
 *
 * @Author: zhengja
 * @Date: 2024-05-21 10:45
 */
public class FaceDetectionTest {

    @Test
    public void test() {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 加载人脸级联分类器
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml"); // 尽可能检测更多人脸，存在不正确检查情况
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml"); // 存在检测不全情况
        // CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml"); // 检测效果较好
        CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml"); // 检测效果最好

        // 图片人脸检测
        // detectFacesInImage("D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\Brush1.png", faceCascade);

        // 视频人脸检测
        // detectFacesInVideo("D:\\temp\\opencv\\Video\\1.mp4", faceCascade);

        // 摄像头人脸检测 todo 待测试，暂时没条件
        detectFacesFromCamera(faceCascade);
    }

    private static void detectFacesFromCamera(CascadeClassifier faceCascade) {
        // 打开摄像头
        VideoCapture videoCapture = new VideoCapture(0);

        // 检查摄像头是否成功打开
        if (!videoCapture.isOpened()) {
            System.out.println("无法打开摄像头");
            return;
        }

        // 获取摄像头的默认帧率和尺寸
        double frameRate = videoCapture.get(Videoio.CAP_PROP_FPS);
        int frameWidth = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH); // width
        int frameHeight = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT); // height

        // 创建窗口来显示结果
        HighGui.namedWindow("Face Detection");

        // 逐帧读取摄像头图像并进行人脸检测
        Mat frame = new Mat();
        while (true) {
            // 读取摄像头图像
            videoCapture.read(frame);

            // 转换为灰度图像
            Mat gray = new Mat();
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

            // 人脸检测
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(gray, faces);

            // 在检测到的人脸区域绘制矩形框
            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
            }

            // 显示结果帧
            HighGui.imshow("Face Detection", frame);

            // 按ESC键退出
            if (HighGui.waitKey((int) Math.round(1000 / frameRate)) == 27) {
                break;
            }
        }

        // 释放视频捕获资源
        videoCapture.release();
    }

}
```









